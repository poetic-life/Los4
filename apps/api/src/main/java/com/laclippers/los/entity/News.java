package com.laclippers.los.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 255)
    private String image;

    @Column(length = 50)
    private String category;

    @Column(length = 30)
    private String date;

    @Column(length = 20)
    private String season;

    @Column(length = 255)
    private String sourceUrl;
}