package com.sairam.game;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseTest {

    public static void main(String[] args) {

        String sql =
                "SELECT current_database(), current_schema(), "
                + "inet_server_port()";

        String playerSql =
                "SELECT player_name, credits "
                + "FROM players "
                + "WHERE player_name = 'Sairam'";

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            System.out.println("Database connection successful!");

            try (Statement statement =
                         connection.createStatement();
                 ResultSet resultSet =
                         statement.executeQuery(sql)) {

                if (resultSet.next()) {

                    System.out.println(
                            "Database: "
                            + resultSet.getString(1)
                    );

                    System.out.println(
                            "Schema: "
                            + resultSet.getString(2)
                    );

                    System.out.println(
                            "Port: "
                            + resultSet.getInt(3)
                    );
                }
            }

            try (Statement statement =
                         connection.createStatement();
                 ResultSet resultSet =
                         statement.executeQuery(playerSql)) {

                if (resultSet.next()) {

                    System.out.println(
                            "Player: "
                            + resultSet.getString("player_name")
                    );

                    System.out.println(
                            "Credits: "
                            + resultSet.getInt("credits")
                    );

                } else {

                    System.out.println(
                            "Sairam player not found!"
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}