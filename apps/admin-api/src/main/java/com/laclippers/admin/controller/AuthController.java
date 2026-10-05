package com.laclippers.admin.controller;

import com.laclippers.admin.common.BusinessException;
import com.laclippers.admin.common.Result;
import com.laclippers.admin.dto.LoginRequest;
import com.laclippers.admin.entity.User;
import com.laclippers.admin.repository.UserRepository;
import com.laclippers.admin.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginRequest req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new BusinessException(400, "用户不存在"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BusinessException(400, "密码错误");
        }
        if (!"ADMIN".equals(user.getRole())) {
            throw new BusinessException(403, "当前账号不是管理员");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);
        return Result.ok(data);
    }
}