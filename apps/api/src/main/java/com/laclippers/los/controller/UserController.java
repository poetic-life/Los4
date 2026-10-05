package com.laclippers.los.controller;

import com.laclippers.los.common.BusinessException;
import com.laclippers.los.common.Result;
import com.laclippers.los.dto.ChangePasswordRequest;
import com.laclippers.los.dto.UpdateProfileRequest;
import com.laclippers.los.entity.User;
import com.laclippers.los.repository.PostRepository;
import com.laclippers.los.repository.UserRepository;
import com.laclippers.los.security.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/me")
    public Result<User> me() {
        return Result.ok(requireUser());
    }

    @GetMapping("/{id}")
    public Result<Map<String, Object>> profile(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("avatar", user.getAvatar());
        data.put("level", user.getLevel());
        data.put("bio", user.getBio());
        data.put("createdAt", user.getCreatedAt());
        data.put("postCount", postRepository.countByUserId(id));
        data.put("likeCount", postRepository.sumLikesByUserId(id));
        data.put("points", user.getPoints());
        return Result.ok(data);
    }

    @PutMapping("/me")
    public Result<User> updateProfile(@Valid @RequestBody UpdateProfileRequest req) {
        User user = requireUser();
        if (req.getUsername() != null && !req.getUsername().trim().isEmpty()) {
            String username = req.getUsername().trim();
            if (!username.equals(user.getUsername()) && userRepository.existsByUsername(username)) {
                throw new BusinessException(400, "该用户名已被占用");
            }
            user.setUsername(username);
        }
        if (req.getEmail() != null && !req.getEmail().trim().isEmpty()) {
            user.setEmail(req.getEmail().trim());
        }
        if (req.getAvatar() != null) {
            user.setAvatar(req.getAvatar());
        }
        if (req.getBio() != null) {
            user.setBio(req.getBio().trim());
        }
        return Result.ok(userRepository.save(user));
    }

    @PutMapping("/password")
    public Result<Void> changePassword(@Valid @RequestBody ChangePasswordRequest req) {
        User user = requireUser();
        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            throw new BusinessException(400, "原密码不正确");
        }
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);
        return Result.ok();
    }

    private User requireUser() {
        Long userId = AuthUtil.currentUserId();
        if (userId == null) {
            throw new BusinessException(401, "未登录");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));
    }
}