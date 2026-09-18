package com.sairam.game;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportsDao {

    // ============================================================
    // OVERALL REPORT
    // ============================================================

    public Object[] getOverallReport() throws SQLException {

        String sql =
                "SELECT "
                        + "COUNT(*) AS total_games, "
                        + "SUM(CASE WHEN UPPER(status) = 'WON' THEN 1 ELSE 0 END) AS wins, "
                        + "SUM(CASE WHEN UPPER(status) = 'LOST' THEN 1 ELSE 0 END) AS losses, "
                        + "COALESCE(SUM(bet_amount), 0) AS total_bet, "
                        + "COALESCE(SUM(payout), 0) AS total_payout "
                        + "FROM game_history";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {

                int totalGames =
                        resultSet.getInt("total_games");

                int wins =
                        resultSet.getInt("wins");

                int losses =
                        resultSet.getInt("losses");

                int totalBet =
                        resultSet.getInt("total_bet");

                int totalPayout =
                        resultSet.getInt("total_payout");

                int profitLoss =
                        totalPayout - totalBet;

                double winPercentage =
                        calculateWinPercentage(
                                wins,
                                totalGames
                        );

                return new Object[] {
                        totalGames,
                        wins,
                        losses,
                        totalBet,
                        totalPayout,
                        profitLoss,
                        winPercentage
                };
            }
        }

        return emptyReport();
    }

    // ============================================================
    // TODAY REPORT
    // ============================================================

    public Object[] getTodayReport() throws SQLException {

        String sql =
                "SELECT "
                        + "COUNT(*) AS total_games, "
                        + "SUM(CASE WHEN UPPER(status) = 'WON' THEN 1 ELSE 0 END) AS wins, "
                        + "SUM(CASE WHEN UPPER(status) = 'LOST' THEN 1 ELSE 0 END) AS losses, "
                        + "COALESCE(SUM(bet_amount), 0) AS total_bet, "
                        + "COALESCE(SUM(payout), 0) AS total_payout "
                        + "FROM game_history "
                        + "WHERE DATE(played_at) = CURRENT_DATE";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {

                int totalGames =
                        resultSet.getInt("total_games");

                int wins =
                        resultSet.getInt("wins");

                int losses =
                        resultSet.getInt("losses");

                int totalBet =
                        resultSet.getInt("total_bet");

                int totalPayout =
                        resultSet.getInt("total_payout");

                int profitLoss =
                        totalPayout - totalBet;

                double winPercentage =
                        calculateWinPercentage(
                                wins,
                                totalGames
                        );

                return new Object[] {
                        totalGames,
                        wins,
                        losses,
                        totalBet,
                        totalPayout,
                        profitLoss,
                        winPercentage
                };
            }
        }

        return emptyReport();
    }

    // ============================================================
    // PLAYER REPORT
    // ============================================================

    public Object[] getPlayerReport(
            String playerName) throws SQLException {

        String sql =
                "SELECT "
                        + "COUNT(*) AS total_games, "
                        + "SUM(CASE WHEN UPPER(status) = 'WON' THEN 1 ELSE 0 END) AS wins, "
                        + "SUM(CASE WHEN UPPER(status) = 'LOST' THEN 1 ELSE 0 END) AS losses, "
                        + "COALESCE(SUM(bet_amount), 0) AS total_bet, "
                        + "COALESCE(SUM(payout), 0) AS total_payout "
                        + "FROM game_history "
                        + "WHERE LOWER(player_name) = LOWER(?)";

        try (
                Connection connection = DatabaseConnection.getConnection();
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

                    int totalGames =
                            resultSet.getInt("total_games");

                    int wins =
                            resultSet.getInt("wins");

                    int losses =
                            resultSet.getInt("losses");

                    int totalBet =
                            resultSet.getInt("total_bet");

                    int totalPayout =
                            resultSet.getInt("total_payout");

                    int profitLoss =
                            totalPayout - totalBet;

                    double winPercentage =
                            calculateWinPercentage(
                                    wins,
                                    totalGames
                            );

                    return new Object[] {
                            totalGames,
                            wins,
                            losses,
                            totalBet,
                            totalPayout,
                            profitLoss,
                            winPercentage
                    };
                }
            }
        }

        return emptyReport();
    }

    // ============================================================
    // DATE RANGE REPORT
    // ============================================================

    public Object[] getDateRangeReport(
            String startDate,
            String endDate) throws SQLException {

        LocalDate start =
                LocalDate.parse(
                        startDate
                );

        LocalDate end =
                LocalDate.parse(
                        endDate
                );

        LocalDate nextDay =
                end.plusDays(1);

        System.out.println();
        System.out.println(
                "=========================================="
        );
        System.out.println(
                "DATE RANGE REPORT"
        );
        System.out.println(
                "Start Date : " + start
        );
        System.out.println(
                "End Date   : " + end
        );
        System.out.println(
                "Next Day   : " + nextDay
        );
        System.out.println(
                "Executing DATE RANGE query..."
        );

        String sql =
                "SELECT "
                        + "COUNT(*) AS total_games, "
                        + "SUM(CASE WHEN UPPER(status) = 'WON' THEN 1 ELSE 0 END) AS wins, "
                        + "SUM(CASE WHEN UPPER(status) = 'LOST' THEN 1 ELSE 0 END) AS losses, "
                        + "COALESCE(SUM(bet_amount), 0) AS total_bet, "
                        + "COALESCE(SUM(payout), 0) AS total_payout "
                        + "FROM game_history "
                        + "WHERE played_at >= CAST(? AS DATE) "
                        + "AND played_at < CAST(? AS DATE)";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setDate(
                    1,
                    Date.valueOf(start)
            );

            statement.setDate(
                    2,
                    Date.valueOf(nextDay)
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

                    int wins =
                            resultSet.getInt(
                                    "wins"
                            );

                    int losses =
                            resultSet.getInt(
                                    "losses"
                            );

                    int totalBet =
                            resultSet.getInt(
                                    "total_bet"
                            );

                    int totalPayout =
                            resultSet.getInt(
                                    "total_payout"
                            );

                    int profitLoss =
                            totalPayout - totalBet;

                    double winPercentage =
                            calculateWinPercentage(
                                    wins,
                                    totalGames
                            );

                    System.out.println(
                            "Total Games : " + totalGames
                    );

                    System.out.println(
                            "Total Wins  : " + wins
                    );

                    System.out.println(
                            "Total Losses: " + losses
                    );

                    System.out.println(
                            "Total Bet   : " + totalBet
                    );

                    System.out.println(
                            "Total Payout: " + totalPayout
                    );

                    System.out.println(
                            "Profit/Loss : " + profitLoss
                    );

                    System.out.println(
                            "Win %       : "
                                    + winPercentage
                    );

                    System.out.println(
                            "DATE RANGE REPORT SUCCESS"
                    );

                    System.out.println(
                            "=========================================="
                    );

                    return new Object[] {
                            totalGames,
                            wins,
                            losses,
                            totalBet,
                            totalPayout,
                            profitLoss,
                            winPercentage
                    };
                }
            }
        }

        return emptyReport();
    }

    // ============================================================
    // DAILY REPORT
    // ============================================================

    public List<Object[]> getDailyReport()
            throws SQLException {

        List<Object[]> report =
                new ArrayList<>();

        String sql =
                "SELECT "
                        + "DATE(played_at) AS report_date, "
                        + "COUNT(*) AS total_games, "
                        + "SUM(CASE WHEN UPPER(status) = 'WON' THEN 1 ELSE 0 END) AS wins, "
                        + "SUM(CASE WHEN UPPER(status) = 'LOST' THEN 1 ELSE 0 END) AS losses, "
                        + "COALESCE(SUM(bet_amount), 0) AS total_bet, "
                        + "COALESCE(SUM(payout), 0) AS total_payout "
                        + "FROM game_history "
                        + "GROUP BY DATE(played_at) "
                        + "ORDER BY report_date DESC";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                int games =
                        resultSet.getInt(
                                "total_games"
                        );

                int wins =
                        resultSet.getInt(
                                "wins"
                        );

                int losses =
                        resultSet.getInt(
                                "losses"
                        );

                int bet =
                        resultSet.getInt(
                                "total_bet"
                        );

                int payout =
                        resultSet.getInt(
                                "total_payout"
                        );

                int profitLoss =
                        payout - bet;

                Object[] row = {
                        resultSet.getDate(
                                "report_date"
                        ),
                        games,
                        wins,
                        losses,
                        bet,
                        payout,
                        profitLoss
                };

                report.add(row);
            }
        }

        return report;
    }

    // ============================================================
    // MONTHLY REPORT
    // ============================================================

    public List<Object[]> getMonthlyReport()
            throws SQLException {

        List<Object[]> report =
                new ArrayList<>();

        String sql =
                "SELECT "
                        + "DATE_TRUNC('month', played_at) AS report_month, "
                        + "COUNT(*) AS total_games, "
                        + "SUM(CASE WHEN UPPER(status) = 'WON' THEN 1 ELSE 0 END) AS wins, "
                        + "SUM(CASE WHEN UPPER(status) = 'LOST' THEN 1 ELSE 0 END) AS losses, "
                        + "COALESCE(SUM(bet_amount), 0) AS total_bet, "
                        + "COALESCE(SUM(payout), 0) AS total_payout "
                        + "FROM game_history "
                        + "GROUP BY DATE_TRUNC('month', played_at) "
                        + "ORDER BY report_month DESC";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                int games =
                        resultSet.getInt(
                                "total_games"
                        );

                int wins =
                        resultSet.getInt(
                                "wins"
                        );

                int losses =
                        resultSet.getInt(
                                "losses"
                        );

                int bet =
                        resultSet.getInt(
                                "total_bet"
                        );

                int payout =
                        resultSet.getInt(
                                "total_payout"
                        );

                int profitLoss =
                        payout - bet;

                Object[] row = {
                        resultSet.getTimestamp(
                                "report_month"
                        ),
                        games,
                        wins,
                        losses,
                        bet,
                        payout,
                        profitLoss
                };

                report.add(row);
            }
        }

        return report;
    }

    // ============================================================
    // PLAYER-WISE REPORT
    // ============================================================

    public List<Object[]> getPlayerWiseReport()
            throws SQLException {

        List<Object[]> report =
                new ArrayList<>();

        String sql =
                "SELECT "
                        + "LOWER(player_name) AS player_name, "
                        + "COUNT(*) AS total_games, "
                        + "SUM(CASE WHEN UPPER(status) = 'WON' THEN 1 ELSE 0 END) AS wins, "
                        + "SUM(CASE WHEN UPPER(status) = 'LOST' THEN 1 ELSE 0 END) AS losses, "
                        + "COALESCE(SUM(bet_amount), 0) AS total_bet, "
                        + "COALESCE(SUM(payout), 0) AS total_payout "
                        + "FROM game_history "
                        + "GROUP BY LOWER(player_name) "
                        + "ORDER BY total_games DESC";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                String player =
                        resultSet.getString(
                                "player_name"
                        );

                int games =
                        resultSet.getInt(
                                "total_games"
                        );

                int wins =
                        resultSet.getInt(
                                "wins"
                        );

                int losses =
                        resultSet.getInt(
                                "losses"
                        );

                int bet =
                        resultSet.getInt(
                                "total_bet"
                        );

                int payout =
                        resultSet.getInt(
                                "total_payout"
                        );

                int profitLoss =
                        payout - bet;

                double winPercentage =
                        calculateWinPercentage(
                                wins,
                                games
                        );

                Object[] row = {
                        player,
                        games,
                        wins,
                        losses,
                        bet,
                        payout,
                        profitLoss,
                        winPercentage
                };

                report.add(row);
            }
        }

        return report;
    }

    // ============================================================
    // GAME TYPE REPORT
    // ============================================================

    public List<Object[]> getGameTypeReport()
            throws SQLException {

        List<Object[]> report =
                new ArrayList<>();

        String sql =
                "SELECT "
                        + "selected_type, "
                        + "COUNT(*) AS total_games, "
                        + "SUM(CASE WHEN UPPER(status) = 'WON' THEN 1 ELSE 0 END) AS wins, "
                        + "SUM(CASE WHEN UPPER(status) = 'LOST' THEN 1 ELSE 0 END) AS losses, "
                        + "COALESCE(SUM(bet_amount), 0) AS total_bet, "
                        + "COALESCE(SUM(payout), 0) AS total_payout "
                        + "FROM game_history "
                        + "GROUP BY selected_type "
                        + "ORDER BY total_games DESC";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                int games =
                        resultSet.getInt(
                                "total_games"
                        );

                int wins =
                        resultSet.getInt(
                                "wins"
                        );

                int losses =
                        resultSet.getInt(
                                "losses"
                        );

                int bet =
                        resultSet.getInt(
                                "total_bet"
                        );

                int payout =
                        resultSet.getInt(
                                "total_payout"
                        );

                int profitLoss =
                        payout - bet;

                Object[] row = {
                        resultSet.getString(
                                "selected_type"
                        ),
                        games,
                        wins,
                        losses,
                        bet,
                        payout,
                        profitLoss
                };

                report.add(row);
            }
        }

        return report;
    }

    // ============================================================
    // COLOR REPORT
    // ============================================================

    public List<Object[]> getColorReport()
            throws SQLException {

        List<Object[]> report =
                new ArrayList<>();

        String sql =
                "SELECT "
                        + "result_color, "
                        + "COUNT(*) AS total_games, "
                        + "SUM(CASE WHEN UPPER(status) = 'WON' THEN 1 ELSE 0 END) AS wins, "
                        + "SUM(CASE WHEN UPPER(status) = 'LOST' THEN 1 ELSE 0 END) AS losses, "
                        + "COALESCE(SUM(bet_amount), 0) AS total_bet, "
                        + "COALESCE(SUM(payout), 0) AS total_payout "
                        + "FROM game_history "
                        + "GROUP BY result_color "
                        + "ORDER BY total_games DESC";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                int games =
                        resultSet.getInt(
                                "total_games"
                        );

                int wins =
                        resultSet.getInt(
                                "wins"
                        );

                int losses =
                        resultSet.getInt(
                                "losses"
                        );

                int bet =
                        resultSet.getInt(
                                "total_bet"
                        );

                int payout =
                        resultSet.getInt(
                                "total_payout"
                        );

                int profitLoss =
                        payout - bet;

                Object[] row = {
                        resultSet.getString(
                                "result_color"
                        ),
                        games,
                        wins,
                        losses,
                        bet,
                        payout,
                        profitLoss
                };

                report.add(row);
            }
        }

        return report;
    }

    // ============================================================
    // HELPER
    // ============================================================

    private double calculateWinPercentage(
            int wins,
            int totalGames) {

        if (totalGames <= 0) {
            return 0.0;
        }

        return ((double) wins
                / totalGames) * 100.0;
    }

    private Object[] emptyReport() {

        return new Object[] {
                0,
                0,
                0,
                0,
                0,
                0,
                0.0
        };
    }
}