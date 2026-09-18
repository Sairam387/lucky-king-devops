package com.sairam.game;

import java.util.ArrayList;
import java.util.List;

public class GameHistory {

    private List<String> history;

    public GameHistory() {
        history = new ArrayList<>();
    }

    public void addResult(int number, String color, boolean won) {

        String status;

        if (won) {
            status = "WON";
        } else {
            status = "LOST";
        }

        String record = "Number: " + number
                + " | Color: " + color
                + " | Result: " + status;

        history.add(record);
    }

    public void showHistory() {

        System.out.println("\n========== GAME HISTORY ==========");

        if (history.isEmpty()) {
            System.out.println("No games played yet.");
            return;
        }

        for (int i = 0; i < history.size(); i++) {
            System.out.println((i + 1) + ". " + history.get(i));
        }
    }
}
