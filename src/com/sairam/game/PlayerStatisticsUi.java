package com.sairam.game;

import javax.swing.*;
import java.awt.*;

public class PlayerStatisticsUi extends JFrame {

    private String playerName;

    private JLabel playerNameLabel;
    private JLabel walletLabel;
    private JLabel gamesLabel;
    private JLabel winsLabel;
    private JLabel lossesLabel;
    private JLabel winPercentageLabel;
    private JLabel totalBetLabel;
    private JLabel totalPayoutLabel;
    private JLabel profitLossLabel;

    private PlayerStatisticsDao statisticsDao;

    public PlayerStatisticsUi(String playerName) {

        this.playerName = playerName;
        this.statisticsDao = new PlayerStatisticsDao();

        setTitle("Player Statistics - " + playerName);
        setSize(650, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createUI();
        loadStatistics();
    }

    private void createUI() {

        JPanel mainPanel = new JPanel(
                new BorderLayout(15, 15)
        );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        JPanel headerPanel = new JPanel(
                new BorderLayout()
        );

        JLabel titleLabel = new JLabel(
                "PLAYER STATISTICS"
        );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        playerNameLabel = new JLabel(
                "Player: " + playerName
        );

        playerNameLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        headerPanel.add(
                titleLabel,
                BorderLayout.WEST
        );

        headerPanel.add(
                playerNameLabel,
                BorderLayout.EAST
        );

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        JPanel statisticsPanel =
                new JPanel(
                        new GridLayout(
                                9,
                                2,
                                12,
                                12
                        )
                );

        statisticsPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Statistics"
                )
        );

        walletLabel =
                createValueLabel();

        gamesLabel =
                createValueLabel();

        winsLabel =
                createValueLabel();

        lossesLabel =
                createValueLabel();

        winPercentageLabel =
                createValueLabel();

        totalBetLabel =
                createValueLabel();

        totalPayoutLabel =
                createValueLabel();

        profitLossLabel =
                createValueLabel();

        addRow(
                statisticsPanel,
                "Current Wallet",
                walletLabel
        );

        addRow(
                statisticsPanel,
                "Total Games",
                gamesLabel
        );

        addRow(
                statisticsPanel,
                "Total Wins",
                winsLabel
        );

        addRow(
                statisticsPanel,
                "Total Losses",
                lossesLabel
        );

        addRow(
                statisticsPanel,
                "Win Percentage",
                winPercentageLabel
        );

        addRow(
                statisticsPanel,
                "Total Bet",
                totalBetLabel
        );

        addRow(
                statisticsPanel,
                "Total Payout",
                totalPayoutLabel
        );

        addRow(
                statisticsPanel,
                "Profit / Loss",
                profitLossLabel
        );

        JLabel statusLabel =
                new JLabel(
                        "Statistics loaded from database"
                );

        statusLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        statisticsPanel.add(
                new JLabel("Status")
        );

        statisticsPanel.add(
                statusLabel
        );

        mainPanel.add(
                statisticsPanel,
                BorderLayout.CENTER
        );

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                5
                        )
                );

        JButton refreshButton =
                new JButton("REFRESH");

        JButton closeButton =
                new JButton("CLOSE");

        refreshButton.addActionListener(
                e -> loadStatistics()
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        buttonPanel.add(
                refreshButton
        );

        buttonPanel.add(
                closeButton
        );

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        setContentPane(mainPanel);
    }

    private JLabel createValueLabel() {

        JLabel label = new JLabel("Loading...");

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        return label;
    }

    private void addRow(
            JPanel panel,
            String title,
            JLabel value
    ) {

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        panel.add(titleLabel);
        panel.add(value);
    }

    private void loadStatistics() {

        try {

            Object[] statistics =
                    statisticsDao.getPlayerStatistics(
                            playerName
                    );

            if (statistics == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Player not found:\n"
                                + playerName,
                        "Player Not Found",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            String name =
                    (String) statistics[0];

            int wallet =
                    (Integer) statistics[1];

            int totalGames =
                    (Integer) statistics[2];

            int totalWins =
                    (Integer) statistics[3];

            int totalLosses =
                    (Integer) statistics[4];

            double winPercentage =
                    (Double) statistics[5];

            int totalBet =
                    (Integer) statistics[6];

            int totalPayout =
                    (Integer) statistics[7];

            int profitLoss =
                    (Integer) statistics[8];

            playerNameLabel.setText(
                    "Player: " + name
            );

            walletLabel.setText(
                    "₹ " + wallet
            );

            gamesLabel.setText(
                    String.valueOf(totalGames)
            );

            winsLabel.setText(
                    String.valueOf(totalWins)
            );

            lossesLabel.setText(
                    String.valueOf(totalLosses)
            );

            winPercentageLabel.setText(
                    String.format(
                            "%.2f%%",
                            winPercentage
                    )
            );

            totalBetLabel.setText(
                    "₹ " + totalBet
            );

            totalPayoutLabel.setText(
                    "₹ " + totalPayout
            );

            profitLossLabel.setText(
                    "₹ " + profitLoss
            );

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load player statistics.\n\n"
                            + "Database Error:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> {

                    PlayerStatisticsUi ui =
                            new PlayerStatisticsUi(
                                    "Sairam"
                            );

                    ui.setVisible(true);
                }
        );
    }
}
