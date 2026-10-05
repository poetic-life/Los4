package com.laclippers.los.controller;

import com.laclippers.los.common.BusinessException;
import com.laclippers.los.common.Result;
import com.laclippers.los.entity.Player;
import com.laclippers.los.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping
    public Result<List<Player>> list(@RequestParam(required = false) String season) {
        Sort sort = Sort.by(Sort.Direction.ASC, "number");
        List<Player> players = (season == null || season.isEmpty())
                ? playerRepository.findAll(sort)
                : playerRepository.findBySeason(season, sort);
        return Result.ok(players);
    }

    @GetMapping("/{id}")
    public Result<Player> detail(@PathVariable Long id) {
        return Result.ok(playerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "球员不存在")));
    }
}