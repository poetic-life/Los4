package com.laclippers.admin.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(length = 50)
    private String username;

    @Column(length = 30)
    private String time;

    @Column(length = 30)
    private String tag;

    @Column(length = 30)
    private String tagClass;

    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Integer likes = 0;

    private Integer comments = 0;

    private Integer views = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}