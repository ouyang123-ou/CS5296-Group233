package com.example.sharding_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

// @Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== Starting test data initialization ===");

        // 1. Clear the table to avoid primary key conflicts
        System.out.println("Clearing t_order table...");
        jdbcTemplate.execute("DELETE FROM t_order");

        // 2. Bulk insert 8000 orders
        System.out.println("Starting bulk insert of 8000 test orders...");
        long start = System.currentTimeMillis();
        for (int i = 1; i <= 8000; i++) {
            jdbcTemplate.update(
                "INSERT INTO t_order(order_id, user_id, product_id, amount, status, create_time) VALUES(?, ?, ?, ?, ?, NOW())",
                (long) i,                          // order_id 从 1 到 8000
                (long) (i % 1000 + 1),             // user_id cycles through 1~1000
                (long) (i % 500 + 1),              // product_id cycles through 1~500
                10.0 + (i % 990),                  // amount between 10~1000
                "PAID"
            );
            if (i % 1000 == 0) {
                System.out.println("Inserted " + i + " records...");
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("Bulk insert completed, total time: " + (end - start) / 1000 + " seconds");
        System.out.println("=== Data initialization finished ===");
    }
}