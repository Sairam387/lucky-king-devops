package com.sairam.game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameHistoryDAO {

    /*
     * Save game history only
     */
    public boolean saveGame(
            String playerName,
            String selectedType,
            String selectedValue,
            int resultNumber,
            String resultColor,
            int betAmount,
            int payout,
            String status
    ) throws SQLException {

        String sql =
                "INSERT INTO game_history " +
                "(player_name, selected_type, selected_value, " +
                "result_number, result_color, bet_amount, " +
                "payout, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, playerName);
            statement.setString(2, selectedType);
            statement.setString(3, selectedValue);
            statement.setInt(4, resultNumber);
            statement.setString(5, resultColor);
            statement.setInt(6, betAmount);
            statement.setInt(7, payout);
            statement.setString(8, status);

            int rowsInserted =
                    statement.executeUpdate();

            return rowsInserted == 1;
        }
    }

    /*
     * Save game + update wallet
     * + save wallet transaction
     *
     * Everything happens inside ONE transaction.
     */
    public int saveGameWithWalletUpdate(
            String playerName,
            String selectedType,
            String selectedValue,
            int resultNumber,
            String resultColor,
            int betAmount,
            int payout,
            String status
    ) throws SQLException {

        Connection connection = null;

        try {

            connection =
                    DatabaseConnection.getConnection();

            connection.setAutoCommit(false);

            /*
             * STEP 1
             * Get current wallet and lock player row.
             */
            String selectWalletSql =
                    "SELECT credits " +
                    "FROM players " +
                    "WHERE player_name = ? " +
                    "FOR UPDATE";

            int currentCredits;

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    selectWalletSql
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

                    if (!resultSet.next()) {

                        throw new SQLException(
                                "Player not found: "
                                        + playerName
                        );
                    }

                    currentCredits =
                            resultSet.getInt(
                                    "credits"
                            );
                }
            }

            /*
             * STEP 2
             * Check wallet balance.
             */
            if (betAmount > currentCredits) {

                throw new SQLException(
                        "Insufficient wallet balance. "
                                + "Current balance: "
                                + currentCredits
                                + ", Bet: "
                                + betAmount
                );
            }

            /*
             * STEP 3
             * Calculate final wallet.
             */
            int newCredits =
                    currentCredits
                            - betAmount
                            + payout;

            /*
             * STEP 4
             * Update player wallet.
             */
            String updateWalletSql =
                    "UPDATE players " +
                    "SET credits = ? " +
                    "WHERE player_name = ?";

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    updateWalletSql
                            )
            ) {

                statement.setInt(
                        1,
                        newCredits
                );

                statement.setString(
                        2,
                        playerName
                );

                int rowsUpdated =
                        statement.executeUpdate();

                if (rowsUpdated != 1) {

                    throw new SQLException(
                            "Wallet update failed."
                    );
                }
            }

            /*
             * STEP 5
             * Save game history.
             */
            String historySql =
                    "INSERT INTO game_history " +
                    "(player_name, selected_type, " +
                    "selected_value, result_number, " +
                    "result_color, bet_amount, payout, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    historySql
                            )
            ) {

                statement.setString(
                        1,
                        playerName
                );

                statement.setString(
                        2,
                        selectedType
                );

                statement.setString(
                        3,
                        selectedValue
                );

                statement.setInt(
                        4,
                        resultNumber
                );

                statement.setString(
                        5,
                        resultColor
                );

                statement.setInt(
                        6,
                        betAmount
                );

                statement.setInt(
                        7,
                        payout
                );

                statement.setString(
                        8,
                        status
                );

                int rowsInserted =
                        statement.executeUpdate();

                if (rowsInserted != 1) {

                    throw new SQLException(
                            "Game history insertion failed."
                    );
                }
            }

            /*
             * STEP 6
             * Save wallet transaction.
             */
            int transactionAmount =
                    payout - betAmount;

            String transactionType;

            if ("WON".equalsIgnoreCase(status)) {

                transactionType =
                        "GAME_WIN";

            } else if ("LOST".equalsIgnoreCase(status)) {

                transactionType =
                        "GAME_LOSS";

            } else {

                transactionType =
                        "GAME_DRAW";
            }

            String description =
                    "Game bet: "
                            + betAmount
                            + ", payout: "
                            + payout
                            + ", result: "
                            + resultNumber
                            + " "
                            + resultColor;

            String transactionSql =
                    "INSERT INTO wallet_transactions " +
                    "(player_name, transaction_type, " +
                    "amount, balance_before, balance_after, " +
                    "description) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    transactionSql
                            )
            ) {

                statement.setString(
                        1,
                        playerName
                );

                statement.setString(
                        2,
                        transactionType
                );

                statement.setInt(
                        3,
                        transactionAmount
                );

                statement.setInt(
                        4,
                        currentCredits
                );

                statement.setInt(
                        5,
                        newCredits
                );

                statement.setString(
                        6,
                        description
                );

                int rowsInserted =
                        statement.executeUpdate();

                if (rowsInserted != 1) {

                    throw new SQLException(
                            "Wallet transaction insertion failed."
                    );
                }
            }

            /*
             * STEP 7
             * Commit everything.
             */
            connection.commit();

            System.out.println(
                    "Game transaction completed."
            );

            System.out.println(
                    "Player: "
                            + playerName
            );

            System.out.println(
                    "Balance Before: "
                            + currentCredits
            );

            System.out.println(
                    "Bet: "
                            + betAmount
            );

            System.out.println(
                    "Payout: "
                            + payout
            );

            System.out.println(
                    "Balance After: "
                            + newCredits
            );

            return newCredits;

        } catch (SQLException e) {

            if (connection != null) {

                try {

                    connection.rollback();

                } catch (SQLException rollbackException) {

                    rollbackException.printStackTrace();
                }
            }

            throw e;

        } finally {

            if (connection != null) {

                try {

                    connection.setAutoCommit(true);
                    connection.close();

                } catch (SQLException closeException) {

                    closeException.printStackTrace();
                }
            }
        }
    }

    /*
     * Get complete game history
     */
    public List<Object[]> getHistory()
            throws SQLException {

        List<Object[]> history =
                new ArrayList<>();

        String sql =
                "SELECT game_id, " +
                "player_name, " +
                "selected_type, " +
                "selected_value, " +
                "result_number, " +
                "result_color, " +
                "bet_amount, " +
                "payout, " +
                "status, " +
                "played_at " +
                "FROM game_history " +
                "ORDER BY game_id DESC";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Object[] row = {

                        resultSet.getInt(
                                "game_id"
                        ),

                        resultSet.getString(
                                "player_name"
                        ),

                        resultSet.getString(
                                "selected_type"
                        )
                                + ": "
                                + resultSet.getString(
                                        "selected_value"
                                ),

                        resultSet.getInt(
                                "result_number"
                        )
                                + " - "
                                + resultSet.getString(
                                        "result_color"
                                ),

                        resultSet.getInt(
                                "bet_amount"
                        ),

                        resultSet.getInt(
                                "payout"
                        ),

                        resultSet.getString(
                                "status"
                        ),

                        resultSet.getTimestamp(
                                "played_at"
                        )
                };

                history.add(row);
            }
        }

        return history;
    }

    /*
     * Get game history for a specific player
     */
    public List<Object[]> getPlayerHistory(
            String playerName
    ) throws SQLException {

        List<Object[]> history =
                new ArrayList<>();

        String sql =
                "SELECT game_id, " +
                "player_name, " +
                "selected_type, " +
                "selected_value, " +
                "result_number, " +
                "result_color, " +
                "bet_amount, " +
                "payout, " +
                "status, " +
                "played_at " +
                "FROM game_history " +
                "WHERE player_name = ? " +
                "ORDER BY game_id DESC";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
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

                    Object[] row = {

                            resultSet.getInt(
                                    "game_id"
                            ),

                            resultSet.getString(
                                    "player_name"
                            ),

                            resultSet.getString(
                                    "selected_type"
                            )
                                    + ": "
                                    + resultSet.getString(
                                            "selected_value"
                                    ),

                            resultSet.getInt(
                                    "result_number"
                            )
                                    + " - "
                                    + resultSet.getString(
                                            "result_color"
                                    ),

                            resultSet.getInt(
                                    "bet_amount"
                            ),

                            resultSet.getInt(
                                    "payout"
                            ),

                            resultSet.getString(
                                    "status"
                            ),

                            resultSet.getTimestamp(
                                    "played_at"
                            )
                    };

                    history.add(row);
                }
            }
        }

        return history;
    }

    /*
     * Get complete game statistics
     *
     * Return order:
     *
     * 0 = Total Games
     * 1 = Wins
     * 2 = Losses
     * 3 = Win Percentage
     * 4 = Total Bet
     * 5 = Total Payout
     * 6 = Profit/Loss
     */
    public Object[] getStatistics()
            throws SQLException {

        String sql =
                "SELECT "
                + "COUNT(*) AS total_games, "
                + "COUNT(CASE "
                + "WHEN status = 'WON' "
                + "THEN 1 END) AS total_wins, "
                + "COUNT(CASE "
                + "WHEN status = 'LOST' "
                + "THEN 1 END) AS total_losses, "
                + "COALESCE(SUM(bet_amount), 0) "
                + "AS total_bet, "
                + "COALESCE(SUM(payout), 0) "
                + "AS total_payout, "
                + "COALESCE("
                + "SUM(payout - bet_amount), 0"
                + ") AS profit_loss "
                + "FROM game_history";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {

                int totalGames =
                        resultSet.getInt(
                                "total_games"
                        );

                int totalWins =
                        resultSet.getInt(
                                "total_wins"
                        );

                int totalLosses =
                        resultSet.getInt(
                                "total_losses"
                        );

                double winPercentage =
                        0.0;

                if (totalGames > 0) {

                    winPercentage =
                            (totalWins * 100.0)
                                    / totalGames;
                }

                return new Object[] {

                        totalGames,

                        totalWins,

                        totalLosses,

                        winPercentage,

                        resultSet.getInt(
                                "total_bet"
                        ),

                        resultSet.getInt(
                                "total_payout"
                        ),

                        resultSet.getInt(
                                "profit_loss"
                        )
                };
            }
        }

        return null;
    }

    /*
     * Get statistics for a specific player
     *
     * Return order:
     *
     * 0 = Player Name
     * 1 = Total Games
     * 2 = Wins
     * 3 = Losses
     * 4 = Win Percentage
     * 5 = Total Bet
     * 6 = Total Payout
     * 7 = Profit/Loss
     */
    public Object[] getPlayerStatistics(
            String playerName
    ) throws SQLException {

        String sql =
                "SELECT "
                + "COUNT(*) AS total_games, "
                + "COUNT(CASE "
                + "WHEN status = 'WON' "
                + "THEN 1 END) AS total_wins, "
                + "COUNT(CASE "
                + "WHEN status = 'LOST' "
                + "THEN 1 END) AS total_losses, "
                + "COALESCE(SUM(bet_amount), 0) "
                + "AS total_bet, "
                + "COALESCE(SUM(payout), 0) "
                + "AS total_payout, "
                + "COALESCE("
                + "SUM(payout - bet_amount), 0"
                + ") AS profit_loss "
                + "FROM game_history "
                + "WHERE player_name = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    playerName
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    int totalGames =
                            resultSet.getInt(
                                    "total_games"
                            );

                    int totalWins =
                            resultSet.getInt(
                                    "total_wins"
                            );

                    int totalLosses =
                            resultSet.getInt(
                                    "total_losses"
                            );

                    double winPercentage =
                            0.0;

                    if (totalGames > 0) {

                        winPercentage =
                                (totalWins * 100.0)
                                        / totalGames;
                    }

                    return new Object[] {

                            playerName,

                            totalGames,

                            totalWins,

                            totalLosses,

                            winPercentage,

                            resultSet.getInt(
                                    "total_bet"
                            ),

                            resultSet.getInt(
                                    "total_payout"
                            ),

                            resultSet.getInt(
                                    "profit_loss"
                            )
                    };
                }
            }
        }

        return null;
    }

    /*
     * Get leaderboard
     *
     * Return order:
     *
     * 0 = Player
     * 1 = Games
     * 2 = Wins
     * 3 = Profit/Loss
     */
    public List<Object[]> getLeaderboard()
            throws SQLException {

        List<Object[]> leaderboard =
                new ArrayList<>();

        String sql =
                "SELECT "
                + "player_name, "
                + "COUNT(*) AS total_games, "
                + "COUNT(CASE "
                + "WHEN status = 'WON' "
                + "THEN 1 END) AS total_wins, "
                + "COALESCE("
                + "SUM(payout - bet_amount), 0"
                + ") AS profit_loss "
                + "FROM game_history "
                + "GROUP BY player_name "
                + "ORDER BY profit_loss DESC";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            int rank = 1;

            while (resultSet.next()) {

                Object[] row = {

                        rank,

                        resultSet.getString(
                                "player_name"
                        ),

                        resultSet.getInt(
                                "total_games"
                        ),

                        resultSet.getInt(
                                "total_wins"
                        ),

                        resultSet.getInt(
                                "profit_loss"
                        )
                };

                leaderboard.add(row);

                rank++;
            }
        }

        return leaderboard;
    }

    /*
     * Get game history as JTable-ready list.
     */
    public List<Object[]> getHistoryTable()
            throws SQLException {

        return getHistory();
    }
}