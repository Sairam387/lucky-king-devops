package com.sairam.game;

public class AddPlayerTest {

    public static void main(String[] args) {

        AdminDao adminDao = new AdminDao();

        try {

            adminDao.addPlayer(
                    "TestPlayer",
                    1000
            );

            System.out.println(
                    "ADD PLAYER TEST SUCCESSFUL!"
            );

        } catch (Exception e) {

            e.printStackTrace();

            System.out.println(
                    "ADD PLAYER TEST FAILED!"
            );
        }
    }
}