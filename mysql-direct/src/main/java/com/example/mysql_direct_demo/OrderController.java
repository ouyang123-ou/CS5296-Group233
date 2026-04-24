package com.example.mysql_direct_demo;  // Adjust package name above if necessary.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class OrderController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Get order by ID endpoint
    @GetMapping("/mysql/order/{id}")
    public Map<String, Object> getOrder(@PathVariable Long id) {
        return jdbcTemplate.queryForMap("SELECT * FROM t_order WHERE order_id = ?", id);
    }

    // Insert endpoint (note: uses @RequestParam, not @RequestBody)
    @PostMapping("/mysql/order")
    public String insertOrder(@RequestParam Long orderId,
                              @RequestParam Long userId,
                              @RequestParam Long productId,
                              @RequestParam Double amount) {
        jdbcTemplate.update(
            "INSERT INTO t_order(order_id, user_id, product_id, amount, status, create_time) VALUES(?, ?, ?, ?, 'PAID', NOW())",
            orderId, userId, productId, amount
        );
        return "OK";
    }
}