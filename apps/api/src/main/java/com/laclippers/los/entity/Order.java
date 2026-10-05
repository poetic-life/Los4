package com.laclippers.los.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 40)
    private String orderNo;

    private Long userId;

    @Column(length = 50)
    private String name;

    @Column(length = 30)
    private String phone;

    @Column(length = 255)
    private String address;

    private Integer totalAmount;

    @Column(length = 20)
    private String paymentMethod;

    @Column(length = 30)
    private String status = "待发货";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "shipped_at")
    private LocalDateTime shippedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}