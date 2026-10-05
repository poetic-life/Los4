package com.laclippers.admin.config;

import com.laclippers.admin.entity.User;
import com.laclippers.admin.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 首次启动时确保存在内置管理员账号（与主站共享同一数据库，幂等）。
 */
@Component
public class AdminDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminDataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.existsByUsername("admin")) return;
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@laclippers.com");
        admin.setPassword(passwordEncoder.encode("123456"));
        admin.setBio("网站审核管理员");
        admin.setPoints(0);
        admin.setLevel("金卡会员");
        admin.setRole("ADMIN");
        admin.setCreatedAt(LocalDateTime.now());
        userRepository.save(admin);
    }
}