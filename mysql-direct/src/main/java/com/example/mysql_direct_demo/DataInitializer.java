package com.example.mysql_direct_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// @Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("=== Starting bulk test data initialization ===");

        // Clear table
        jdbcTemplate.execute("DELETE FROM t_order");
        System.out.println("t_order table cleared");

        // 批量插入 8000 条
        String sql = "INSERT INTO t_order(order_id, user_id, product_id, amount, status, create_time) VALUES(?, ?, ?, ?, 'PAID', NOW())";
        int total = 8000;
        int batchSize = 1000;
        List<Object[]> batchArgs = new ArrayList<>(batchSize);

        long start = System.currentTimeMillis();
        for (int i = 1; i <= total; i++) {
            batchArgs.add(new Object[]{
                    (long) i,
                    (long) (i % 1000 + 1),
                    (long) (i % 500 + 1),
                    10.0 + (i % 990)
            });
            if (i % batchSize == 0) {
                jdbcTemplate.batchUpdate(sql, batchArgs);
                batchArgs.clear();
                System.out.println("Inserted " + i + " records...");
            }
        }
        if (!batchArgs.isEmpty()) {
            jdbcTemplate.batchUpdate(sql, batchArgs);
        }

        long end = System.currentTimeMillis();
        System.out.println("Batch insert completed, total time: " + (end - start) / 1000 + " seconds");
        System.out.println("=== Data initialization finished ===");
    }
}