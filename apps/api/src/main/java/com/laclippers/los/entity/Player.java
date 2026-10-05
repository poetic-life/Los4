package com.laclippers.los.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 50)
    private String enName;

    private Integer number;

    @Column(length = 50)
    private String position;

    @Column(length = 50)
    private String positionEn;

    @Column(length = 20)
    private String height;

    @Column(length = 20)
    private String weight;

    @Column(length = 255)
    private String image;

    @Column(length = 30)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 50)
    private String college;

    @Column(length = 50)
    private String draft;

    @Column(length = 20)
    private String experience;

    @Column(length = 20)
    private String season;

    // 虎扑球员 ID，用于联网同步时稳定匹配（upsert）
    @Column(length = 20)
    private String externalId;

    // 最近一次联网更新场均数据的时间，用于限制抓取频率
    private java.time.LocalDateTime statsUpdated;

    // 赛季场均数据
    private Double pointsAvg;

    private Double reboundsAvg;

    private Double assistsAvg;

    private Double stealsAvg;

    private Double blocksAvg;

    private Double fieldGoalPct;

    private Double threePct;

    private Double freeThrowPct;
}