package com.laclippers.admin.controller;

import com.laclippers.admin.common.BusinessException;
import com.laclippers.admin.common.Result;
import com.laclippers.admin.entity.Comment;
import com.laclippers.admin.entity.Order;
import com.laclippers.admin.entity.Post;
import com.laclippers.admin.entity.Product;
import com.laclippers.admin.entity.User;
import com.laclippers.admin.repository.CommentRepository;
import com.laclippers.admin.repository.OrderRepository;
import com.laclippers.admin.repository.PostRepository;
import com.laclippers.admin.repository.ProductRepository;
import com.laclippers.admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理员后台管理接口（仅 ROLE_ADMIN 可访问，由 SecurityConfig 保护）。
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    // ---------------- 仪表盘 ----------------

    @GetMapping("/dashboard")
    public Result<Map<String, Object>> dashboard() {
        Map<String, Object> data = new HashMap<>();
        data.put("userCount", userRepository.count());
        data.put("postCount", postRepository.count());
        data.put("commentCount", commentRepository.count());
        data.put("productCount", productRepository.count());

        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        data.put("orderCount", orders.size());
        long pending = orders.stream().filter(o -> "待发货".equals(o.getStatus())).count();
        data.put("pendingShipCount", pending);
        long revenue = orders.stream()
                .filter(o -> !"已取消".equals(o.getStatus()))
                .mapToLong(o -> o.getTotalAmount() == null ? 0 : o.getTotalAmount())
                .sum();
        data.put("totalRevenue", revenue);

        List<Order> recent = orders.size() > 6 ? orders.subList(0, 6) : new ArrayList<>(orders);
        data.put("recentOrders", recent);
        return Result.ok(data);
    }

    @GetMapping("/trends")
    public Result<Map<String, Object>> trends() {
        Map<String, Object> data = new HashMap<>();

        LocalDate today = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd");
        List<String> dates = new ArrayList<>();
        LocalDate[] days = new LocalDate[7];
        for (int i = 0; i < 7; i++) {
            days[i] = today.minusDays(6 - i);
            dates.add(days[i].format(fmt));
        }
        data.put("dates", dates);

        List<Order> orders = orderRepository.findAll();

        // 按真实 createdAt 对近 7 日做逐日聚合（用户新增 / 订单新增 / 营收）
        Map<LocalDate, Long> userByDay = userRepository.findAll().stream()
                .filter(u -> u.getCreatedAt() != null)
                .collect(Collectors.groupingBy(u -> u.getCreatedAt().toLocalDate(), Collectors.counting()));
        Map<LocalDate, Long> orderByDay = orders.stream()
                .filter(o -> o.getCreatedAt() != null)
                .collect(Collectors.groupingBy(o -> o.getCreatedAt().toLocalDate(), Collectors.counting()));
        Map<LocalDate, Long> revenueByDay = orders.stream()
                .filter(o -> o.getCreatedAt() != null && !"已取消".equals(o.getStatus()))
                .collect(Collectors.groupingBy(o -> o.getCreatedAt().toLocalDate(),
                        Collectors.summingLong(o -> o.getTotalAmount() == null ? 0 : o.getTotalAmount())));

        List<Integer> userNew = new ArrayList<>();
        List<Integer> salesNew = new ArrayList<>();
        List<Long> revenueTrend = new ArrayList<>();
        List<Integer> userGrowth = new ArrayList<>();
        int acc = 0;
        for (LocalDate d : days) {
            int n = userByDay.getOrDefault(d, 0L).intValue();
            userNew.add(n);
            acc += n;
            userGrowth.add(acc);
            salesNew.add(orderByDay.getOrDefault(d, 0L).intValue());
            revenueTrend.add(revenueByDay.getOrDefault(d, 0L));
        }
        data.put("userNew", userNew);
        data.put("userGrowth", userGrowth);
        data.put("salesNew", salesNew);
        data.put("revenueTrend", revenueTrend);

        // 真实分布：商品销量TOP / 分类 / 订单状态 / 用户角色
        List<Map<String, Object>> topProducts = productRepository.findAll(Sort.by(Sort.Direction.DESC, "sales"))
                .stream().limit(6).map(p -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("name", p.getName());
                    m.put("sales", p.getSales());
                    return m;
                }).collect(Collectors.toList());
        data.put("topProducts", topProducts);

        data.put("categoryDist", toNameValue(productRepository.findAll().stream()
                .collect(Collectors.groupingBy(p -> p.getCategoryName() == null ? "其他" : p.getCategoryName(), Collectors.counting()))));
        data.put("statusDist", toNameValue(orders.stream()
                .collect(Collectors.groupingBy(o -> o.getStatus() == null ? "未知" : o.getStatus(), Collectors.counting()))));
        data.put("roleDist", toNameValue(userRepository.findAll().stream()
                .collect(Collectors.groupingBy(u -> "ADMIN".equals(u.getRole()) ? "管理员" : "普通用户", Collectors.counting()))));

        return Result.ok(data);
    }

    private List<Map<String, Object>> toNameValue(Map<String, Long> map) {
        List<Map<String, Object>> list = new ArrayList<>();
        map.forEach((k, v) -> {
            Map<String, Object> m = new HashMap<>();
            m.put("name", k);
            m.put("value", v);
            list.add(m);
        });
        return list;
    }

    // ---------------- 用户管理 ----------------

    @GetMapping("/users")
    public Result<List<User>> listUsers() {
        return Result.ok(userRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
    }

    @PutMapping("/users/{id}/role")
    public Result<User> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));
        String role = body.get("role");
        if (role == null || (!"USER".equals(role) && !"ADMIN".equals(role))) {
            throw new BusinessException(400, "非法角色");
        }
        user.setRole(role);
        return Result.ok(userRepository.save(user));
    }

    @DeleteMapping("/users/{id}")
    @Transactional
    public Result<Void> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));
        for (Post post : postRepository.findByUserIdOrderByCreatedAtDesc(id)) {
            commentRepository.deleteAll(commentRepository.findByPostId(post.getId()));
        }
        postRepository.deleteAll(postRepository.findByUserIdOrderByCreatedAtDesc(id));
        commentRepository.deleteAll(commentRepository.findByUserId(id));
        userRepository.delete(user);
        return Result.ok();
    }

    // ---------------- 帖子管理 ----------------

    @GetMapping("/posts")
    public Result<List<Post>> listPosts() {
        return Result.ok(postRepository.findAllByOrderByCreatedAtDesc());
    }

    @DeleteMapping("/posts/{id}")
    @Transactional
    public Result<Void> deletePost(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "帖子不存在"));
        commentRepository.deleteAll(commentRepository.findByPostId(id));
        postRepository.delete(post);
        return Result.ok();
    }

    // ---------------- 评论管理 ----------------

    @GetMapping("/comments")
    public Result<List<Comment>> listComments() {
        return Result.ok(commentRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    @DeleteMapping("/comments/{id}")
    public Result<Void> deleteComment(@PathVariable Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "评论不存在"));
        commentRepository.delete(comment);
        return Result.ok();
    }

    // ---------------- 商品管理 ----------------

    @GetMapping("/products")
    public Result<List<Product>> listProducts() {
        return Result.ok(productRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
    }

    @PostMapping("/products")
    public Result<Product> createProduct(@RequestBody Product product) {
        product.setId(null);
        if (product.getSales() == null) product.setSales(0);
        if (product.getReviews() == null) product.setReviews(0);
        if (product.getStock() == null) product.setStock(100);
        return Result.ok(productRepository.save(product));
    }

    @PutMapping("/products/{id}")
    public Result<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "商品不存在"));
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setImage(product.getImage());
        existing.setCategoryId(product.getCategoryId());
        existing.setCategoryName(product.getCategoryName());
        existing.setSales(product.getSales());
        existing.setReviews(product.getReviews());
        existing.setDescription(product.getDescription());
        existing.setStock(product.getStock());
        return Result.ok(productRepository.save(existing));
    }

    @DeleteMapping("/products/{id}")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "商品不存在"));
        productRepository.delete(product);
        return Result.ok();
    }

    // ---------------- 订单管理 ----------------

    @GetMapping("/orders")
    public Result<List<Order>> listOrders() {
        return Result.ok(orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    @PostMapping("/orders/{id}/ship")
    public Result<Order> shipOrder(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "订单不存在"));
        if (!"待发货".equals(order.getStatus())) {
            throw new BusinessException(400, "当前状态不可发货");
        }
        order.setStatus("已发货");
        order.setShippedAt(LocalDateTime.now());
        return Result.ok(orderRepository.save(order));
    }

    @PostMapping("/orders/{id}/cancel")
    public Result<Order> cancelOrder(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "订单不存在"));
        if ("已完成".equals(order.getStatus()) || "已取消".equals(order.getStatus())) {
            throw new BusinessException(400, "当前订单不可取消");
        }
        order.setStatus("已取消");
        return Result.ok(orderRepository.save(order));
    }
}