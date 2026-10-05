package com.laclippers.los.controller;

import com.laclippers.los.common.BusinessException;
import com.laclippers.los.common.Result;
import com.laclippers.los.dto.LoginRequest;
import com.laclippers.los.dto.RegisterRequest;
import com.laclippers.los.entity.User;
import com.laclippers.los.repository.UserRepository;
import com.laclippers.los.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
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

    @PostMapping("/register")
    public Result<Map<String, Object>> register(@Valid @RequestBody RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new BusinessException(400, "用户名已存在");
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setPoints(0);
        user.setLevel("银卡会员");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        return Result.ok(data);
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginRequest req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new BusinessException(400, "用户不存在"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BusinessException(400, "密码错误");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getEmail());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);
        return Result.ok(data);
    }
}