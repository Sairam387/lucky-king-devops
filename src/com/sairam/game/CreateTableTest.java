package com.sairam.game;

import java.sql.Connection;
import java.sql.Statement;

public class CreateTableTest {

    public static void main(String[] args) {

        String sql =
                "CREATE TABLE IF NOT EXISTS game_history ("
                + "game_id SERIAL PRIMARY KEY, "
                + "player_name VARCHAR(100) NOT NULL, "
                + "selected_type VARCHAR(20) NOT NULL, "
                + "selected_value VARCHAR(20) NOT NULL, "
                + "result_number INT NOT NULL, "
                + "result_color VARCHAR(20) NOT NULL, "
                + "bet_amount INT NOT NULL, "
                + "payout INT NOT NULL, "
                + "status VARCHAR(10) NOT NULL, "
                + "played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ")";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(sql);

            System.out.println("game_history table created successfully!");

        } catch (Exception e) {

            System.out.println("Table creation failed!");
            e.printStackTrace();
        }
    }
}