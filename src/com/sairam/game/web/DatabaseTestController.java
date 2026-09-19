package com.sairam.game.web;

import java.sql.Connection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sairam.game.DatabaseConnection;

@RestController
public class DatabaseTestController {

    @GetMapping("/api/database-test")
    public String databaseTest() {

        try (Connection connection = DatabaseConnection.getConnection()) {

            if (connection != null && !connection.isClosed()) {
                return "Lucky King Web Application + PostgreSQL connection successful!";
            }

            return "Database connection failed!";

        } catch (Exception e) {
            return "Database connection error: " + e.getMessage();
        }
    }
}