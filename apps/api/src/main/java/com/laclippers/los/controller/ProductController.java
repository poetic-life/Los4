package com.laclippers.los.controller;

import com.laclippers.los.common.PageResult;
import com.laclippers.los.common.Result;
import com.laclippers.los.entity.Product;
import com.laclippers.los.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public Result<PageResult<Product>> list(@RequestParam(required = false) Integer categoryId,
                                            @RequestParam(required = false) String keyword,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, Sort.by(Sort.Direction.ASC, "id"));
        boolean hasKw = keyword != null && !keyword.trim().isEmpty();

        Page<Product> result;
        if (hasKw && categoryId != null) {
            result = productRepository.findByCategoryIdAndNameContaining(categoryId, keyword.trim(), pageable);
        } else if (hasKw) {
            result = productRepository.findByNameContaining(keyword.trim(), pageable);
        } else if (categoryId != null) {
            result = productRepository.findByCategoryId(categoryId, pageable);
        } else {
            result = productRepository.findAll(pageable);
        }
        return Result.ok(PageResult.of(result));
    }

    @GetMapping("/{id}")
    public Result<Product> detail(@PathVariable Long id) {
        return Result.ok(productRepository.findById(id).orElse(null));
    }
}