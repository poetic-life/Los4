package com.laclippers.los.controller;

import com.laclippers.los.common.BusinessException;
import com.laclippers.los.common.Result;
import com.laclippers.los.entity.Favorite;
import com.laclippers.los.entity.Product;
import com.laclippers.los.repository.FavoriteRepository;
import com.laclippers.los.repository.ProductRepository;
import com.laclippers.los.security.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public Result<List<Product>> list() {
        List<Favorite> favorites = favoriteRepository.findByUserId(requireUserId());
        List<Product> products = new ArrayList<>();
        for (Favorite f : favorites) {
            productRepository.findById(f.getProductId()).ifPresent(products::add);
        }
        return Result.ok(products);
    }

    @PostMapping
    public Result<Void> add(@RequestBody Map<String, Long> body) {
        Long userId = requireUserId();
        Long productId = body.get("productId");
        if (productId == null) {
            throw new BusinessException(400, "缺少商品ID");
        }
        if (!favoriteRepository.findByUserIdAndProductId(userId, productId).isPresent()) {
            Favorite f = new Favorite();
            f.setUserId(userId);
            f.setProductId(productId);
            f.setCreatedAt(LocalDateTime.now());
            favoriteRepository.save(f);
        }
        return Result.ok();
    }

    @DeleteMapping("/{productId}")
    public Result<Void> remove(@PathVariable Long productId) {
        favoriteRepository.deleteByUserIdAndProductId(requireUserId(), productId);
        return Result.ok();
    }

    private Long requireUserId() {
        Long userId = AuthUtil.currentUserId();
        if (userId == null) {
            throw new BusinessException(401, "请先登录");
        }
        return userId;
    }
}