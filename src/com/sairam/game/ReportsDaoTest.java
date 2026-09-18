package com.sairam.game;

import java.util.List;

public class ReportsDaoTest {

    public static void main(String[] args) {

        ReportsDao reportsDao = new ReportsDao();

        System.out.println(
                "================================================="
        );

        System.out.println(
                "          REPORTS DAO TEST"
        );

        System.out.println(
                "================================================="
        );

        try {

            // =================================================
            // 1. OVERALL REPORT
            // =================================================

            System.out.println(
                    "\n========== 1. OVERALL REPORT =========="
            );

            Object[] overallReport =
                    reportsDao.getOverallReport();

            printReport(overallReport);


            // =================================================
            // 2. TODAY REPORT
            // =================================================

            System.out.println(
                    "\n========== 2. TODAY REPORT =========="
            );

            Object[] todayReport =
                    reportsDao.getTodayReport();

            printReport(todayReport);


            // =================================================
            // 3. PLAYER REPORT
            // =================================================

            System.out.println(
                    "\n========== 3. PLAYER REPORT =========="
            );

            String playerName = "Sairam";

            Object[] playerReport =
                    reportsDao.getPlayerReport(
                            playerName
                    );

            System.out.println(
                    "Player: " + playerName
            );

            printReport(playerReport);


            // =================================================
            // 4. DATE RANGE REPORT
            // =================================================

            System.out.println(
                    "\n========== 4. DATE RANGE REPORT =========="
            );

            String startDate =
                    "2026-09-01";

            String endDate =
                    "2026-09-06";

            Object[] dateRangeReport =
                    reportsDao.getDateRangeReport(
                            startDate,
                            endDate
                    );

            System.out.println(
                    "Start Date: " + startDate
            );

            System.out.println(
                    "End Date: " + endDate
            );

            printReport(dateRangeReport);


            // =================================================
            // 5. DAILY REPORT
            // =================================================

            System.out.println(
                    "\n========== 5. DAILY REPORT =========="
            );

            List<Object[]> dailyReports =
                    reportsDao.getDailyReport();

            if (dailyReports.isEmpty()) {

                System.out.println(
                        "No daily report data found."
                );

            } else {

                for (Object[] row : dailyReports) {

                    System.out.println(
                            "Date: " + row[0]
                    );

                    System.out.println(
                            "Total Games: " + row[1]
                    );

                    System.out.println(
                            "Total Wins: " + row[2]
                    );

                    System.out.println(
                            "Total Losses: " + row[3]
                    );

                    System.out.println(
                            "Total Bet: " + row[4]
                    );

                    System.out.println(
                            "Total Payout: " + row[5]
                    );

                    System.out.println(
                            "Profit/Loss: " + row[6]
                    );

                    System.out.println(
                            "-----------------------------------------"
                    );
                }
            }


            // =================================================
            // 6. MONTHLY REPORT
            // =================================================

            System.out.println(
                    "\n========== 6. MONTHLY REPORT =========="
            );

            List<Object[]> monthlyReports =
                    reportsDao.getMonthlyReport();

            if (monthlyReports.isEmpty()) {

                System.out.println(
                        "No monthly report data found."
                );

            } else {

                for (Object[] row : monthlyReports) {

                    System.out.println(
                            "Month: " + row[0]
                    );

                    System.out.println(
                            "Total Games: " + row[1]
                    );

                    System.out.println(
                            "Total Wins: " + row[2]
                    );

                    System.out.println(
                            "Total Losses: " + row[3]
                    );

                    System.out.println(
                            "Total Bet: " + row[4]
                    );

                    System.out.println(
                            "Total Payout: " + row[5]
                    );

                    System.out.println(
                            "Profit/Loss: " + row[6]
                    );

                    System.out.println(
                            "-----------------------------------------"
                    );
                }
            }


            // =================================================
            // 7. PLAYER-WISE REPORT
            // =================================================

            System.out.println(
                    "\n========== 7. PLAYER-WISE REPORT =========="
            );

            List<Object[]> playerWiseReports =
                    reportsDao.getPlayerWiseReport();

            if (playerWiseReports.isEmpty()) {

                System.out.println(
                        "No player-wise report data found."
                );

            } else {

                for (Object[] row :
                        playerWiseReports) {

                    System.out.println(
                            "Player: " + row[0]
                    );

                    System.out.println(
                            "Total Games: " + row[1]
                    );

                    System.out.println(
                            "Total Wins: " + row[2]
                    );

                    System.out.println(
                            "Total Losses: " + row[3]
                    );

                    System.out.println(
                            "Total Bet: " + row[4]
                    );

                    System.out.println(
                            "Total Payout: " + row[5]
                    );

                    System.out.println(
                            "Profit/Loss: " + row[6]
                    );

                    System.out.println(
                            "Win Percentage: "
                                    + String.format(
                                            "%.2f",
                                            (Double) row[7]
                                    )
                                    + "%"
                    );

                    System.out.println(
                            "-----------------------------------------"
                    );
                }
            }


            // =================================================
            // 8. GAME TYPE REPORT
            // =================================================

            System.out.println(
                    "\n========== 8. GAME TYPE REPORT =========="
            );

            List<Object[]> gameTypeReports =
                    reportsDao.getGameTypeReport();

            if (gameTypeReports.isEmpty()) {

                System.out.println(
                        "No game type report data found."
                );

            } else {

                for (Object[] row :
                        gameTypeReports) {

                    System.out.println(
                            "Game Type: " + row[0]
                    );

                    System.out.println(
                            "Total Games: " + row[1]
                    );

                    System.out.println(
                            "Total Wins: " + row[2]
                    );

                    System.out.println(
                            "Total Losses: " + row[3]
                    );

                    System.out.println(
                            "Total Bet: " + row[4]
                    );

                    System.out.println(
                            "Total Payout: " + row[5]
                    );

                    System.out.println(
                            "Profit/Loss: " + row[6]
                    );

                    System.out.println(
                            "-----------------------------------------"
                    );
                }
            }


            // =================================================
            // 9. COLOR REPORT
            // =================================================

            System.out.println(
                    "\n========== 9. COLOR REPORT =========="
            );

            List<Object[]> colorReports =
                    reportsDao.getColorReport();

            if (colorReports.isEmpty()) {

                System.out.println(
                        "No color report data found."
                );

            } else {

                for (Object[] row :
                        colorReports) {

                    System.out.println(
                            "Result Color: " + row[0]
                    );

                    System.out.println(
                            "Total Games: " + row[1]
                    );

                    System.out.println(
                            "Total Wins: " + row[2]
                    );

                    System.out.println(
                            "Total Losses: " + row[3]
                    );

                    System.out.println(
                            "Total Bet: " + row[4]
                    );

                    System.out.println(
                            "Total Payout: " + row[5]
                    );

                    System.out.println(
                            "Profit/Loss: " + row[6]
                    );

                    System.out.println(
                            "-----------------------------------------"
                    );
                }
            }


            // =================================================
            // TEST COMPLETED
            // =================================================

            System.out.println(
                    "\n================================================="
            );

            System.out.println(
                    "       REPORTS DAO TEST COMPLETED"
            );

            System.out.println(
                    "================================================="
            );

        } catch (Exception e) {

            System.out.println(
                    "\n================================================="
            );

            System.out.println(
                    "       REPORTS DAO TEST FAILED"
            );

            System.out.println(
                    "================================================="
            );

            System.out.println(
                    "Error: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }


    // =========================================================
    // PRINT COMMON REPORT
    // =========================================================

    private static void printReport(
            Object[] report
    ) {

        if (report == null) {

            System.out.println(
                    "No report data found."
            );

            return;
        }

        System.out.println(
                "Total Games: " + report[0]
        );

        System.out.println(
                "Total Wins: " + report[1]
        );

        System.out.println(
                "Total Losses: " + report[2]
        );

        System.out.println(
                "Total Bet: " + report[3]
        );

        System.out.println(
                "Total Payout: " + report[4]
        );

        System.out.println(
                "Profit/Loss: " + report[5]
        );

        System.out.println(
                "Win Percentage: "
                        + String.format(
                                "%.2f",
                                (Double) report[6]
                        )
                        + "%"
        );
    }
}