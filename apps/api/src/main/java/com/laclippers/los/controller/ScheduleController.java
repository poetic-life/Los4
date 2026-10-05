package com.laclippers.los.controller;

import com.laclippers.los.common.Result;
import com.laclippers.los.entity.Game;
import com.laclippers.los.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private GameRepository gameRepository;

    @GetMapping
    public Result<List<Game>> list(@RequestParam(required = false) String season) {
        Sort sort = Sort.by(Sort.Direction.ASC, "date");
        List<Game> games = (season == null || season.isEmpty())
                ? gameRepository.findAll(sort)
                : gameRepository.findBySeason(season, sort);
        return Result.ok(games);
    }
}