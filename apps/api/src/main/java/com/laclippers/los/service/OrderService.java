package com.laclippers.los.service;

import com.laclippers.los.common.BusinessException;
import com.laclippers.los.dto.OrderRequest;
import com.laclippers.los.entity.Order;
import com.laclippers.los.entity.OrderItem;
import com.laclippers.los.entity.Product;
import com.laclippers.los.repository.OrderItemRepository;
import com.laclippers.los.repository.OrderRepository;
import com.laclippers.los.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order create(Long userId, OrderRequest req) {
        if (req.getItems() == null || req.getItems().isEmpty()) {
            throw new BusinessException(400, "订单商品不能为空");
        }

        List<OrderItem> items = new ArrayList<>();
        int total = 0;
        for (OrderRequest.Item it : req.getItems()) {
            Product product = productRepository.findById(it.getProductId())
                    .orElseThrow(() -> new BusinessException(404, "商品不存在"));
            int quantity = it.getQuantity() == null || it.getQuantity() < 1 ? 1 : it.getQuantity();
            OrderItem oi = new OrderItem();
            oi.setProductId(product.getId());
            oi.setProductName(product.getName());
            oi.setProductImage(product.getImage());
            oi.setPrice(product.getPrice());
            oi.setQuantity(quantity);
            items.add(oi);
            total += product.getPrice() * quantity;
        }

        Order order = new Order();
        order.setOrderNo("ORD" + System.currentTimeMillis());
        order.setUserId(userId);
        order.setName(req.getName());
        order.setPhone(req.getPhone());
        order.setAddress(req.getAddress());
        order.setPaymentMethod(req.getPaymentMethod() == null ? "alipay" : req.getPaymentMethod());
        order.setTotalAmount(total);
        order.setStatus("待发货");
        order.setCreatedAt(LocalDateTime.now());
        order = orderRepository.save(order);

        for (OrderItem oi : items) {
            oi.setOrderId(order.getId());
            orderItemRepository.save(oi);
        }
        return order;
    }

    public List<Order> listByUser(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<OrderItem> itemsOf(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    public Order ship(Long userId, Long orderId) {
        Order order = getOwned(userId, orderId);
        if (!"待发货".equals(order.getStatus())) {
            throw new BusinessException(400, "当前状态不可发货");
        }
        order.setStatus("已发货");
        order.setShippedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    public Order confirm(Long userId, Long orderId) {
        Order order = getOwned(userId, orderId);
        if (!"已发货".equals(order.getStatus()) && !"待发货".equals(order.getStatus())) {
            throw new BusinessException(400, "当前状态不可确认收货");
        }
        order.setStatus("已完成");
        order.setCompletedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    public Order cancel(Long userId, Long orderId) {
        Order order = getOwned(userId, orderId);
        if ("已完成".equals(order.getStatus()) || "已取消".equals(order.getStatus())) {
            throw new BusinessException(400, "当前订单不可取消");
        }
        order.setStatus("已取消");
        return orderRepository.save(order);
    }

    private Order getOwned(Long userId, Long orderId) {
        return orderRepository.findById(orderId)
                .filter(o -> o.getUserId() != null && o.getUserId().equals(userId))
                .orElseThrow(() -> new BusinessException(404, "订单不存在"));
    }
}