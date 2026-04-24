package com.example.sharding_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

// @Component
public class ShardingTestRunner implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== ShardingTestRunner starting ===");

        try {
            // 1. Delete any existing old data to avoid primary key conflicts
            jdbcTemplate.execute("DELETE FROM t_order WHERE order_id IN (1, 2)");
            System.out.println("Cleaned old data for order_id=1 and order_id=2");

            // 2. Insert order_id=1 (expected to route to ds1)
            jdbcTemplate.update(
                "INSERT INTO t_order(order_id, user_id, product_id, amount, status, create_time) " +
                "VALUES(?, ?, ?, ?, ?, NOW())",
                1L, 100L, 200L, 99.9, "PAID"
            );
            System.out.println("Inserted order_id=1");

            // 3. Insert order_id=2 (expected to route to ds0)
            jdbcTemplate.update(
                "INSERT INTO t_order(order_id, user_id, product_id, amount, status, create_time) " +
                "VALUES(?, ?, ?, ?, ?, NOW())",
                2L, 101L, 201L, 199.9, "PAID"
            );
            System.out.println("Inserted order_id=2");

            // 4. Query verification
            System.out.println("Query order_id=1: " + 
                jdbcTemplate.queryForMap("SELECT * FROM t_order WHERE order_id = 1"));
            System.out.println("Query order_id=2: " + 
                jdbcTemplate.queryForMap("SELECT * FROM t_order WHERE order_id = 2"));

        } catch (Exception e) {
            System.err.println("Database operation failed: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== ShardingTestRunner finished ===");
    }
}