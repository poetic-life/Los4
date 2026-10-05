package com.laclippers.los.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    private Integer price;

    @Column(length = 255)
    private String image;

    private Integer categoryId;

    @Column(length = 50)
    private String categoryName;

    private Integer sales = 0;

    private Integer reviews = 0;

    @Column(length = 500)
    private String description;

    private Integer stock = 100;
}