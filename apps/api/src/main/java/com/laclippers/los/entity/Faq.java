package com.laclippers.los.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_faq")
public class Faq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String category;

    @Column(length = 50)
    private String categoryName;

    @Column(length = 200)
    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;
}