package com.example.sharding_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Single record query endpoint: GET /order/{id}
    @GetMapping("/order/{id}")
    public Map<String, Object> getOrder(@PathVariable Long id) {
        return jdbcTemplate.queryForMap("SELECT * FROM t_order WHERE order_id = ?", id);
    }
    // Temporary cleanup endpoint; can be commented out or removed after use
    @GetMapping("/clean")
    public String cleanData() {
        int deleted = jdbcTemplate.update("DELETE FROM t_order WHERE order_id > 8000");
        return "Deleted " + deleted + " records.";
    }

    // Insert endpoint: POST /order
    @PostMapping("/order")
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