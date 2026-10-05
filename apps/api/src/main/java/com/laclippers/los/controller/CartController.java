package com.laclippers.los.controller;

import com.laclippers.los.common.BusinessException;
import com.laclippers.los.common.Result;
import com.laclippers.los.dto.CartRequest;
import com.laclippers.los.entity.CartItem;
import com.laclippers.los.entity.Product;
import com.laclippers.los.repository.CartItemRepository;
import com.laclippers.los.repository.ProductRepository;
import com.laclippers.los.security.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public Result<List<CartItem>> list() {
        return Result.ok(cartItemRepository.findByUserId(requireUserId()));
    }

    @PostMapping
    public Result<CartItem> add(@RequestBody CartRequest req) {
        Long userId = requireUserId();
        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new BusinessException(404, "商品不存在"));
        int qty = req.getQuantity() == null || req.getQuantity() < 1 ? 1 : req.getQuantity();

        CartItem item = cartItemRepository.findByUserIdAndProductId(userId, product.getId()).orElse(null);
        if (item == null) {
            item = new CartItem();
            item.setUserId(userId);
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setProductImage(product.getImage());
            item.setPrice(product.getPrice());
            item.setQuantity(qty);
        } else {
            item.setQuantity(item.getQuantity() + qty);
        }
        return Result.ok(cartItemRepository.save(item));
    }

    @PutMapping("/{id}")
    public Result<CartItem> update(@PathVariable Long id, @RequestParam Integer quantity) {
        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "购物车项不存在"));
        item.setQuantity(quantity == null || quantity < 1 ? 1 : quantity);
        return Result.ok(cartItemRepository.save(item));
    }

    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        cartItemRepository.deleteById(id);
        return Result.ok();
    }

    @DeleteMapping
    public Result<Void> clear() {
        cartItemRepository.deleteByUserId(requireUserId());
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