package com.sairam.game;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            String playerName = JOptionPane.showInputDialog(
                    null,
                    "Enter player name:",
                    "Number Color Game",
                    JOptionPane.QUESTION_MESSAGE
            );

            // Check player name
            if (playerName == null || playerName.trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        null,
                        "Player name is required.",
                        "Invalid Player",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            playerName = playerName.trim();

            try {

                // Create Player DAO
                PlayerDao playerDAO = new PlayerDao();

                // Get existing player or create new player
                Player player =
                        playerDAO.getOrCreatePlayer(playerName);

                // Open Game UI with database credits
                GameUi gameUi =
                        new GameUi(
                                player.getName(),
                                player.getCredits()
                        );

                gameUi.setVisible(true);

            } catch (Exception e) {

                e.printStackTrace();

                JOptionPane.showMessageDialog(
                        null,
                        "Unable to start the game.\n\n"
                                + "Database Error:\n"
                                + e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}