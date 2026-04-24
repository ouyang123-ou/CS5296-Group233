# CS5296-CC
# Distributed Database Middleware Benchmark: ShardingSphere-JDBC vs. Direct MySQL

## Overview
This project evaluates the performance impact of introducing **ShardingSphere-JDBC** (client-side sharding) by comparing it against a **direct MySQL** baseline.  

## Test Objects
- **ShardingSphere-JDBC** – client-side sharding across two RDS MySQL 8.0 instances.
- **Direct MySQL** – a single RDS MySQL 8.0 instance without any middleware.

## Environment
- **Cloud**: Alibaba Cloud (Hong Kong region)
- **Compute**: 1 ECS (4 vCPU, 8 GiB) as jump host / MyCat host (stopped)
- **Database**: 2 RDS MySQL 8.0 instances (4 vCPU, 8 GiB, 50 GB SSD)
- **Application**: Spring Boot 2.7.18, JDK 8, ShardingSphere-JDBC 5.1.2
- **Load Generator**: Apache JMeter 5.6.3
- **Data**: 8,000 rows in `t_order` table, sharded by `order_id % 2`

## Test Plan
Two workload patterns:
- **Point Query** (`SELECT * FROM t_order WHERE order_id = ?`)
- **Data Insertion** (`INSERT INTO t_order(...)`)

Concurrency levels: 100, 300, 500 concurrent threads.  
Each test runs for 5 minutes, repeated 3 times.


## How to Reproduce
1. Provision Alibaba Cloud ECS and two RDS MySQL 8.0 instances in the same VPC.
2. Create `t_order` table on both shards.
3. Build and run `sharding/` application with SSH tunnel.
4. Execute JMeter test plan `ShardingSphere_HTTP_Test.jmx`.
5. Build and run `mysql-direct/` application with another SSH tunnel.
6. Execute JMeter test plan `MySQL_Direct_HTTP_Test.jmx`.
7. Collect CSV data and chart results.

## Contributors
- Shulei PENG
- Yumeng SI
- Jiani OUYANG
