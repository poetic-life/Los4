package com.laclippers.los.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String homeTeam;

    @Column(length = 20)
    private String homeRecord;

    @Column(length = 50)
    private String awayTeam;

    @Column(length = 20)
    private String awayRecord;

    @Column(length = 30)
    private String date;

    @Column(length = 20)
    private String time;

    @Column(length = 100)
    private String venue;

    private Boolean isHome = false;

    @Column(length = 30)
    private String status;

    @Column(length = 20)
    private String season;

    // 虎扑比赛 ID（boxscore），用于联网同步时稳定匹配（upsert）
    @Column(length = 20)
    private String externalId;

    private Integer homeScore;

    private Integer awayScore;
}