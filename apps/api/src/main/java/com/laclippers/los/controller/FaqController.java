package com.laclippers.los.controller;

import com.laclippers.los.common.Result;
import com.laclippers.los.entity.Faq;
import com.laclippers.los.repository.FaqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/faqs")
public class FaqController {

    @Autowired
    private FaqRepository faqRepository;

    @GetMapping
    public Result<List<Faq>> list() {
        return Result.ok(faqRepository.findAll());
    }

    @GetMapping("/category/{category}")
    public Result<List<Faq>> byCategory(@PathVariable String category) {
        return Result.ok(faqRepository.findByCategory(category));
    }
}