package com.laclippers.los.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private Long productId;

    @Column(length = 100)
    private String productName;

    @Column(length = 255)
    private String productImage;

    private Integer price;

    private Integer quantity;
}