package com.laclippers.los.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 赛季元数据（label/排名/备注），战绩由赛程数据动态聚合，避免冗余不一致。
 */
@Data
@Entity
@Table(name = "t_season")
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, unique = true, nullable = false)
    private String season;

    @Column(length = 30)
    private String label;

    @Column(length = 40)
    private String standing;

    @Column(length = 50)
    private String note;

    private Integer wins;

    private Integer losses;
}