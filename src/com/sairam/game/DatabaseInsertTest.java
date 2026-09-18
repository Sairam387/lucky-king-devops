package com.sairam.game;

public class DatabaseInsertTest {

    public static void main(String[] args) {

        GameHistoryDAO dao = new GameHistoryDAO();

        try {

            dao.saveGame(
                    "Sairam",
                    "NUMBER",
                    "5",
                    5,
                    "BLUE",
                    100,
                    600,
                    "WON"
            );

            System.out.println("Game saved successfully!");

        } catch (Exception e) {

            System.out.println("Game save failed!");
            e.printStackTrace();
        }
    }
}