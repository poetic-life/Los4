package com.laclippers.los.controller;

import com.laclippers.los.common.PageResult;
import com.laclippers.los.common.Result;
import com.laclippers.los.entity.News;
import com.laclippers.los.repository.NewsRepository;
import com.laclippers.los.service.NewsSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsSyncService newsSyncService;

    /** 手动触发一次新闻同步（GET 公开放行，便于免认证触发）。 */
    @GetMapping("/sync")
    public Result<Integer> sync() {
        return Result.ok(newsSyncService.syncNews());
    }

    @GetMapping
    public Result<PageResult<News>> list(@RequestParam(required = false) String season,
                                         @RequestParam(required = false) String category,
                                         @RequestParam(required = false) String keyword,
                                         @RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "9") int size) {
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, Sort.by(Sort.Direction.DESC, "date"));
        boolean hasSeason = season != null && !season.isEmpty();
        boolean hasKw = keyword != null && !keyword.trim().isEmpty();
        boolean hasCat = category != null && !category.isEmpty();

        Page<News> result;
        if (hasSeason) {
            if (hasKw && hasCat) {
                result = newsRepository.findBySeasonAndCategoryAndTitleContaining(season, category, keyword.trim(), pageable);
            } else if (hasKw) {
                result = newsRepository.findBySeasonAndTitleContaining(season, keyword.trim(), pageable);
            } else if (hasCat) {
                result = newsRepository.findBySeasonAndCategory(season, category, pageable);
            } else {
                result = newsRepository.findBySeason(season, pageable);
            }
        } else {
            if (hasKw && hasCat) {
                result = newsRepository.findByCategoryAndTitleContaining(category, keyword.trim(), pageable);
            } else if (hasKw) {
                result = newsRepository.findByTitleContaining(keyword.trim(), pageable);
            } else if (hasCat) {
                result = newsRepository.findByCategory(category, pageable);
            } else {
                result = newsRepository.findAll(pageable);
            }
        }
        return Result.ok(PageResult.of(result));
    }

    @GetMapping("/{id}")
    public Result<News> detail(@PathVariable Long id) {
        return Result.ok(newsRepository.findById(id).orElse(null));
    }
}