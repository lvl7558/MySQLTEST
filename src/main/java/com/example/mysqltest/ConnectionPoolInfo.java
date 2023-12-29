package com.example.mysqltest;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class ConnectionPoolInfo implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) {
        if (dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;

            int maximumPoolSize = hikariDataSource.getMaximumPoolSize();
            int minimumIdle = hikariDataSource.getMinimumIdle();

            System.out.println("Maximum Pool Size: " + maximumPoolSize);
            System.out.println("Minimum Idle Connections: " + minimumIdle);
        } else {
            System.out.println("Not using HikariCP as the connection pool.");
        }
    }
}