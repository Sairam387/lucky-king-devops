package com.sairam.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class PlayerDetailsUi extends JFrame {

    private static final long serialVersionUID = 1L;

    private final String playerName;

    private PlayerStatisticsDao statisticsDao;
    private WalletTransactionDao walletTransactionDao;

    private JLabel playerNameLabel;
    private JLabel walletLabel;
    private JLabel gamesLabel;
    private JLabel winsLabel;
    private JLabel lossesLabel;
    private JLabel winPercentageLabel;
    private JLabel totalBetLabel;
    private JLabel totalPayoutLabel;
    private JLabel profitLossLabel;

    private JTable transactionTable;
    private DefaultTableModel transactionModel;

    private JTable gameHistoryTable;
    private DefaultTableModel gameHistoryModel;

    public PlayerDetailsUi(String playerName) {

        this.playerName = playerName;

        statisticsDao = new PlayerStatisticsDao();
        walletTransactionDao = new WalletTransactionDao();

        setTitle("Player Details - " + playerName);

        setSize(1200, 800);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        buildUi();

        loadPlayerDetails();

        loadTransactions();

        loadGameHistory();
    }

    private void buildUi() {

        getContentPane().setLayout(
                new BorderLayout(10, 10)
        );

        getContentPane().setBackground(
                new Color(245, 247, 250)
        );

        JPanel headerPanel =
                createHeaderPanel();

        getContentPane().add(
                headerPanel,
                BorderLayout.NORTH
        );

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        centerPanel.setBackground(
                new Color(245, 247, 250)
        );

        JPanel statisticsPanel =
                createStatisticsPanel();

        centerPanel.add(
                statisticsPanel,
                BorderLayout.NORTH
        );

        JTabbedPane tabbedPane =
                createTabbedPane();

        centerPanel.add(
                tabbedPane,
                BorderLayout.CENTER
        );

        getContentPane().add(
                centerPanel,
                BorderLayout.CENTER
        );

        JPanel buttonPanel =
                createButtonPanel();

        getContentPane().add(
                buttonPanel,
                BorderLayout.SOUTH
        );
    }

    private JPanel createHeaderPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                new Color(35, 45, 60)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        20,
                        15,
                        20
                )
        );

        playerNameLabel =
                new JLabel(
                        "PLAYER DETAILS: "
                                + playerName
                );

        playerNameLabel.setForeground(
                Color.WHITE
        );

        playerNameLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        panel.add(
                playerNameLabel,
                BorderLayout.WEST
        );

        JLabel statusLabel =
                new JLabel(
                        "ADMIN VIEW"
                );

        statusLabel.setForeground(
                Color.WHITE
        );

        statusLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        statusLabel.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        panel.add(
                statusLabel,
                BorderLayout.EAST
        );

        return panel;
    }

    private JPanel createStatisticsPanel() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                3,
                                3,
                                10,
                                10
                        )
                );

        panel.setBackground(
                new Color(245, 247, 250)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        walletLabel =
                createStatisticCard(
                        panel,
                        "CURRENT WALLET"
                );

        gamesLabel =
                createStatisticCard(
                        panel,
                        "TOTAL GAMES"
                );

        winsLabel =
                createStatisticCard(
                        panel,
                        "WINS"
                );

        lossesLabel =
                createStatisticCard(
                        panel,
                        "LOSSES"
                );

        winPercentageLabel =
                createStatisticCard(
                        panel,
                        "WIN %"
                );

        totalBetLabel =
                createStatisticCard(
                        panel,
                        "TOTAL BET"
                );

        totalPayoutLabel =
                createStatisticCard(
                        panel,
                        "TOTAL PAYOUT"
                );

        profitLossLabel =
                createStatisticCard(
                        panel,
                        "PROFIT / LOSS"
                );

        JLabel playerLabel =
                createStatisticCard(
                        panel,
                        "PLAYER"
                );

        playerLabel.setText(
                playerName
        );

        return panel;
    }

    private JLabel createStatisticCard(
            JPanel parent,
            String title
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(
                Color.WHITE
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        210,
                                        215,
                                        220
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                10,
                                10,
                                10,
                                10
                        )
                )
        );

        JLabel titleLabel =
                new JLabel(
                        title,
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        titleLabel.setForeground(
                new Color(
                        90,
                        95,
                        100
                )
        );

        JLabel valueLabel =
                new JLabel(
                        "Loading...",
                        SwingConstants.CENTER
                );

        valueLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        valueLabel.setForeground(
                new Color(
                        35,
                        45,
                        60
                )
        );

        card.add(
                titleLabel,
                BorderLayout.NORTH
        );

        card.add(
                valueLabel,
                BorderLayout.CENTER
        );

        parent.add(card);

        return valueLabel;
    }

    private JTabbedPane createTabbedPane() {

        JTabbedPane tabs =
                new JTabbedPane();

        tabs.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        JPanel gameHistoryPanel =
                createGameHistoryPanel();

        JPanel transactionPanel =
                createTransactionPanel();

        tabs.addTab(
                "GAME HISTORY",
                gameHistoryPanel
        );

        tabs.addTab(
                "WALLET TRANSACTIONS",
                transactionPanel
        );

        return tabs;
    }

    private JPanel createGameHistoryPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        210,
                                        215,
                                        220
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                5,
                                5,
                                5
                        )
                )
        );

        JLabel titleLabel =
                new JLabel(
                        "COMPLETE GAME HISTORY"
                );

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        titleLabel.setBorder(
                BorderFactory.createEmptyBorder(
                        5,
                        10,
                        8,
                        10
                )
        );

        panel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        gameHistoryModel =
                new DefaultTableModel(
                        new Object[] {
                                "Game ID",
                                "Player",
                                "Type",
                                "Selected",
                                "Result",
                                "Color",
                                "Bet",
                                "Payout",
                                "Status",
                                "Played At"
                        },
                        0
                ) {

                    private static final long serialVersionUID =
                            1L;

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        gameHistoryTable =
                new JTable(
                        gameHistoryModel
                );

        gameHistoryTable.setRowHeight(28);

        gameHistoryTable.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        gameHistoryTable.getTableHeader()
                .setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                13
                        )
                );

        gameHistoryTable.setAutoCreateRowSorter(
                true
        );

        gameHistoryTable.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(70);

        gameHistoryTable.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(100);

        gameHistoryTable.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(90);

        gameHistoryTable.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(100);

        gameHistoryTable.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(70);

        gameHistoryTable.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(80);

        gameHistoryTable.getColumnModel()
                .getColumn(6)
                .setPreferredWidth(70);

        gameHistoryTable.getColumnModel()
                .getColumn(7)
                .setPreferredWidth(80);

        gameHistoryTable.getColumnModel()
                .getColumn(8)
                .setPreferredWidth(80);

        gameHistoryTable.getColumnModel()
                .getColumn(9)
                .setPreferredWidth(150);

        JScrollPane scrollPane =
                new JScrollPane(
                        gameHistoryTable
                );

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JPanel createTransactionPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        210,
                                        215,
                                        220
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                5,
                                5,
                                5
                        )
                )
        );

        JLabel titleLabel =
                new JLabel(
                        "WALLET TRANSACTION HISTORY"
                );

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        titleLabel.setBorder(
                BorderFactory.createEmptyBorder(
                        5,
                        10,
                        8,
                        10
                )
        );

        panel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        transactionModel =
                new DefaultTableModel(
                        new Object[] {
                                "ID",
                                "Player",
                                "Type",
                                "Amount",
                                "Before",
                                "After",
                                "Description",
                                "Date"
                        },
                        0
                ) {

                    private static final long serialVersionUID =
                            1L;

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        transactionTable =
                new JTable(
                        transactionModel
                );

        transactionTable.setRowHeight(28);

        transactionTable.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        transactionTable.getTableHeader()
                .setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                13
                        )
                );

        transactionTable.setAutoCreateRowSorter(
                true
        );

        transactionTable.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(50);

        transactionTable.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(100);

        transactionTable.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(110);

        transactionTable.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(80);

        transactionTable.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(80);

        transactionTable.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(80);

        transactionTable.getColumnModel()
                .getColumn(6)
                .setPreferredWidth(300);

        transactionTable.getColumnModel()
                .getColumn(7)
                .setPreferredWidth(150);

        JScrollPane scrollPane =
                new JScrollPane(
                        transactionTable
                );

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JPanel createButtonPanel() {

        JPanel panel =
                new JPanel();

        panel.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        5,
                        10,
                        10,
                        10
                )
        );

        JButton refreshButton =
                new JButton(
                        "REFRESH"
                );

        refreshButton.setPreferredSize(
                new Dimension(
                        130,
                        40
                )
        );

        refreshButton.addActionListener(
                e -> refreshDetails()
        );

        JButton closeButton =
                new JButton(
                        "CLOSE"
                );

        closeButton.setPreferredSize(
                new Dimension(
                        130,
                        40
                )
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        panel.add(
                refreshButton
        );

        panel.add(
                closeButton
        );

        return panel;
    }

    private void loadPlayerDetails() {

        try {

            Object[] data =
                    statisticsDao.getPlayerStatistics(
                            playerName
                    );

            if (data == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Player not found:\n"
                                + playerName,
                        "Player Details",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            walletLabel.setText(
                    String.valueOf(
                            data[1]
                    )
            );

            gamesLabel.setText(
                    String.valueOf(
                            data[2]
                    )
            );

            winsLabel.setText(
                    String.valueOf(
                            data[3]
                    )
            );

            lossesLabel.setText(
                    String.valueOf(
                            data[4]
                    )
            );

            winPercentageLabel.setText(
                    String.format(
                            "%.2f%%",
                            ((Number) data[5])
                                    .doubleValue()
                    )
            );

            totalBetLabel.setText(
                    String.valueOf(
                            data[6]
                    )
            );

            totalPayoutLabel.setText(
                    String.valueOf(
                            data[7]
                    )
            );

            profitLossLabel.setText(
                    String.valueOf(
                            data[8]
                    )
            );

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load player statistics.\n\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadTransactions() {

        transactionModel.setRowCount(0);

        try {

            List<Object[]> transactions =
                    walletTransactionDao
                            .getPlayerTransactions(
                                    playerName
                            );

            if (transactions == null
                    || transactions.isEmpty()) {

                return;
            }

            for (Object[] row : transactions) {

                transactionModel.addRow(
                        row
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load wallet transactions.\n\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadGameHistory() {

        gameHistoryModel.setRowCount(0);

        String sql =
                "SELECT "
                + "game_id, "
                + "player_name, "
                + "selected_type, "
                + "selected_value, "
                + "result_number, "
                + "result_color, "
                + "bet_amount, "
                + "payout, "
                + "status, "
                + "played_at "
                + "FROM game_history "
                + "WHERE player_name = ? "
                + "ORDER BY played_at DESC";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setString(
                    1,
                    playerName
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    int gameId =
                            resultSet.getInt(
                                    "game_id"
                            );

                    String player =
                            resultSet.getString(
                                    "player_name"
                            );

                    String selectedType =
                            resultSet.getString(
                                    "selected_type"
                            );

                    String selectedValue =
                            resultSet.getString(
                                    "selected_value"
                            );

                    int resultNumber =
                            resultSet.getInt(
                                    "result_number"
                            );

                    String resultColor =
                            resultSet.getString(
                                    "result_color"
                            );

                    int betAmount =
                            resultSet.getInt(
                                    "bet_amount"
                            );

                    int payout =
                            resultSet.getInt(
                                    "payout"
                            );

                    String status =
                            resultSet.getString(
                                    "status"
                            );

                    Timestamp playedAt =
                            resultSet.getTimestamp(
                                    "played_at"
                            );

                    gameHistoryModel.addRow(
                            new Object[] {
                                    gameId,
                                    player,
                                    selectedType,
                                    selectedValue,
                                    resultNumber,
                                    resultColor,
                                    betAmount,
                                    payout,
                                    status,
                                    playedAt
                            }
                    );
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load game history.\n\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void refreshDetails() {

        loadPlayerDetails();

        loadTransactions();

        loadGameHistory();
    }

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(
                () -> {

                    PlayerDetailsUi ui =
                            new PlayerDetailsUi(
                                    "Sairam"
                            );

                    ui.setVisible(true);
                }
        );
    }
}