package com.laclippers.los.controller;

import com.laclippers.los.common.Result;
import com.laclippers.los.entity.Season;
import com.laclippers.los.repository.SeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seasons")
public class SeasonController {

    @Autowired
    private SeasonRepository seasonRepository;

    @GetMapping
    public Result<List<Map<String, Object>>> list() {
        List<Season> seasons = seasonRepository.findAll(Sort.by(Sort.Direction.DESC, "season"));
        List<Map<String, Object>> result = new ArrayList<>();
        for (Season s : seasons) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("season", s.getSeason());
            item.put("label", s.getLabel());
            item.put("standing", s.getStanding());
            item.put("note", s.getNote());
            item.put("wins", s.getWins());
            item.put("losses", s.getLosses());
            result.add(item);
        }
        return Result.ok(result);
    }
}