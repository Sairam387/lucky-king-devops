package com.sairam.game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerStatisticsDao {

    public Object[] getPlayerStatistics(
            String playerName
    ) throws SQLException {

        if (playerName == null || playerName.trim().isEmpty()) {
            throw new SQLException("Player name cannot be empty.");
        }

        String sql =
                "SELECT "
                + "p.player_name, "
                + "p.credits, "

                + "COUNT(g.game_id) AS total_games, "

                + "COUNT(CASE "
                + "WHEN UPPER(g.status) = 'WON' "
                + "THEN 1 END) AS total_wins, "

                + "COUNT(CASE "
                + "WHEN UPPER(g.status) = 'LOST' "
                + "THEN 1 END) AS total_losses, "

                + "COALESCE(SUM(g.bet_amount), 0) "
                + "AS total_bet, "

                + "COALESCE(SUM(g.payout), 0) "
                + "AS total_payout, "

                + "COALESCE("
                + "SUM(g.payout - g.bet_amount), 0"
                + ") AS profit_loss "

                + "FROM players p "

                + "LEFT JOIN game_history g "
                + "ON LOWER(p.player_name) = LOWER(g.player_name) "

                + "WHERE LOWER(p.player_name) = LOWER(?) "

                + "GROUP BY "
                + "p.player_name, "
                + "p.credits";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    playerName.trim()
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    String actualPlayerName =
                            resultSet.getString(
                                    "player_name"
                            );

                    int currentWallet =
                            resultSet.getInt(
                                    "credits"
                            );

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

                    double winPercentage = 0.0;

                    if (totalGames > 0) {

                        winPercentage =
                                (totalWins * 100.0)
                                / totalGames;
                    }

                    int totalBet =
                            resultSet.getInt(
                                    "total_bet"
                            );

                    int totalPayout =
                            resultSet.getInt(
                                    "total_payout"
                            );

                    int profitLoss =
                            resultSet.getInt(
                                    "profit_loss"
                            );

                    /*
                     * PlayerDetailsUi expects exactly
                     * 9 values in this order:
                     *
                     * 0 - Player Name
                     * 1 - Current Wallet
                     * 2 - Total Games
                     * 3 - Wins
                     * 4 - Losses
                     * 5 - Win Percentage
                     * 6 - Total Bet
                     * 7 - Total Payout
                     * 8 - Profit / Loss
                     */

                    return new Object[] {

                            actualPlayerName,

                            currentWallet,

                            totalGames,

                            totalWins,

                            totalLosses,

                            winPercentage,

                            totalBet,

                            totalPayout,

                            profitLoss
                    };
                }
            }
        }

        return null;
    }
}