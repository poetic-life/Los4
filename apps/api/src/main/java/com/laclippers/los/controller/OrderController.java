package com.laclippers.los.controller;

import com.laclippers.los.common.BusinessException;
import com.laclippers.los.common.Result;
import com.laclippers.los.dto.OrderRequest;
import com.laclippers.los.entity.Order;
import com.laclippers.los.security.AuthUtil;
import com.laclippers.los.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public Result<List<Order>> list() {
        return Result.ok(orderService.listByUser(requireUserId()));
    }

    @GetMapping("/{id}")
    public Result<Map<String, Object>> detail(@PathVariable Long id) {
        Order order = orderService.listByUser(requireUserId()).stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BusinessException(404, "订单不存在"));
        Map<String, Object> data = new HashMap<>();
        data.put("order", order);
        data.put("items", orderService.itemsOf(id));
        return Result.ok(data);
    }

    @PostMapping
    public Result<Order> create(@RequestBody OrderRequest req) {
        return Result.ok(orderService.create(requireUserId(), req));
    }

    @PostMapping("/{id}/ship")
    public Result<Order> ship(@PathVariable Long id) {
        return Result.ok(orderService.ship(requireUserId(), id));
    }

    @PostMapping("/{id}/confirm")
    public Result<Order> confirm(@PathVariable Long id) {
        return Result.ok(orderService.confirm(requireUserId(), id));
    }

    @PostMapping("/{id}/cancel")
    public Result<Order> cancel(@PathVariable Long id) {
        return Result.ok(orderService.cancel(requireUserId(), id));
    }

    private Long requireUserId() {
        Long userId = AuthUtil.currentUserId();
        if (userId == null) {
            throw new BusinessException(401, "请先登录");
        }
        return userId;
    }
}