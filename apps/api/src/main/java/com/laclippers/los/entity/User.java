package com.laclippers.los.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(length = 100)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "TEXT")
    private String avatar;

    @Column(length = 200)
    private String bio;

    private Integer points = 0;

    @Column(length = 50)
    private String level = "银卡会员";

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}