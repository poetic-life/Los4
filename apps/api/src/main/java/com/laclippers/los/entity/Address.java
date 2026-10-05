package com.laclippers.los.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(length = 50)
    private String name;

    @Column(length = 30)
    private String phone;

    @Column(length = 255)
    private String address;

    private Boolean isDefault = false;
}