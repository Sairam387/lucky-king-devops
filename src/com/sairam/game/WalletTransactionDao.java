package com.sairam.game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WalletTransactionDao {

    /**
     * Saves a wallet transaction.
     *
     * Amount convention:
     * Positive  = money/credits added
     * Negative  = money/credits deducted
     * Zero      = no balance change
     */
    public boolean saveTransaction(
            String playerName,
            String transactionType,
            int amount,
            int balanceBefore,
            int balanceAfter,
            String description
    ) throws SQLException {

        if (playerName == null
                || playerName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Player name cannot be empty."
            );
        }

        if (transactionType == null
                || transactionType.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Transaction type cannot be empty."
            );
        }

        if (balanceBefore < 0) {

            throw new IllegalArgumentException(
                    "Balance before cannot be negative."
            );
        }

        if (balanceAfter < 0) {

            throw new IllegalArgumentException(
                    "Balance after cannot be negative."
            );
        }

        String sql =
                "INSERT INTO wallet_transactions "
                        + "(player_name, transaction_type, amount, "
                        + "balance_before, balance_after, description) "
                        + "VALUES (?, ?, ?, ?, ?, ?)";

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

            statement.setString(
                    2,
                    transactionType.trim()
            );

            statement.setInt(
                    3,
                    amount
            );

            statement.setInt(
                    4,
                    balanceBefore
            );

            statement.setInt(
                    5,
                    balanceAfter
            );

            statement.setString(
                    6,
                    description
            );

            int rowsInserted =
                    statement.executeUpdate();

            return rowsInserted == 1;
        }
    }


    /**
     * Saves a game-related wallet transaction.
     *
     * GAME_WIN  = payout greater than bet
     * GAME_LOSS = payout less than bet
     * GAME_DRAW = payout equal to bet
     */
    public boolean saveGameTransaction(
            String playerName,
            int betAmount,
            int payout,
            int balanceBefore,
            int balanceAfter
    ) throws SQLException {

        if (betAmount < 0) {

            throw new IllegalArgumentException(
                    "Bet amount cannot be negative."
            );
        }

        if (payout < 0) {

            throw new IllegalArgumentException(
                    "Payout cannot be negative."
            );
        }

        int netAmount =
                payout - betAmount;

        String transactionType;

        if (netAmount > 0) {

            transactionType =
                    "GAME_WIN";

        } else if (netAmount < 0) {

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
                        + payout;

        return saveTransaction(
                playerName,
                transactionType,
                netAmount,
                balanceBefore,
                balanceAfter,
                description
        );
    }


    /**
     * Saves an administrator credit transaction.
     */
    public boolean saveCreditTransaction(
            String playerName,
            int amount,
            int balanceBefore,
            int balanceAfter
    ) throws SQLException {

        if (amount < 0) {

            throw new IllegalArgumentException(
                    "Credit amount cannot be negative."
            );
        }

        String description =
                "Credits added by administrator";

        return saveTransaction(
                playerName,
                "ADMIN_CREDIT",
                amount,
                balanceBefore,
                balanceAfter,
                description
        );
    }


    /**
     * Saves a wallet reset transaction.
     */
    public boolean saveResetTransaction(
            String playerName,
            int balanceBefore,
            int balanceAfter
    ) throws SQLException {

        int amount =
                balanceAfter - balanceBefore;

        String description =
                "Wallet reset by administrator";

        return saveTransaction(
                playerName,
                "WALLET_RESET",
                amount,
                balanceBefore,
                balanceAfter,
                description
        );
    }


    /**
     * Returns all wallet transactions.
     *
     * Row structure:
     *
     * 0 = Transaction ID
     * 1 = Player Name
     * 2 = Transaction Type
     * 3 = Amount
     * 4 = Balance Before
     * 5 = Balance After
     * 6 = Description
     * 7 = Created At
     */
    public List<Object[]> getAllTransactions()
            throws SQLException {

        List<Object[]> transactions =
                new ArrayList<>();

        String sql =
                "SELECT transaction_id, "
                        + "player_name, "
                        + "transaction_type, "
                        + "amount, "
                        + "balance_before, "
                        + "balance_after, "
                        + "description, "
                        + "created_at "
                        + "FROM wallet_transactions "
                        + "ORDER BY transaction_id DESC";

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
                                "transaction_id"
                        ),

                        resultSet.getString(
                                "player_name"
                        ),

                        resultSet.getString(
                                "transaction_type"
                        ),

                        resultSet.getInt(
                                "amount"
                        ),

                        resultSet.getInt(
                                "balance_before"
                        ),

                        resultSet.getInt(
                                "balance_after"
                        ),

                        resultSet.getString(
                                "description"
                        ),

                        resultSet.getTimestamp(
                                "created_at"
                        )
                };

                transactions.add(row);
            }
        }

        return transactions;
    }


    /**
     * Returns transactions for one player.
     *
     * Player matching is case-insensitive.
     *
     * Example:
     * "sairam" matches "Sairam", "SAIRAM", etc.
     *
     * Row structure:
     *
     * 0 = Transaction ID
     * 1 = Player Name
     * 2 = Transaction Type
     * 3 = Amount
     * 4 = Balance Before
     * 5 = Balance After
     * 6 = Description
     * 7 = Created At
     */
    public List<Object[]> getPlayerTransactions(
            String playerName
    ) throws SQLException {

        List<Object[]> transactions =
                new ArrayList<>();

        if (playerName == null
                || playerName.trim().isEmpty()) {

            return transactions;
        }

        String sql =
                "SELECT transaction_id, "
                        + "player_name, "
                        + "transaction_type, "
                        + "amount, "
                        + "balance_before, "
                        + "balance_after, "
                        + "description, "
                        + "created_at "
                        + "FROM wallet_transactions "
                        + "WHERE LOWER(player_name) = LOWER(?) "
                        + "ORDER BY transaction_id DESC";

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

                while (resultSet.next()) {

                    Object[] row = {

                            resultSet.getInt(
                                    "transaction_id"
                            ),

                            resultSet.getString(
                                    "player_name"
                            ),

                            resultSet.getString(
                                    "transaction_type"
                            ),

                            resultSet.getInt(
                                    "amount"
                            ),

                            resultSet.getInt(
                                    "balance_before"
                            ),

                            resultSet.getInt(
                                    "balance_after"
                            ),

                            resultSet.getString(
                                    "description"
                            ),

                            resultSet.getTimestamp(
                                    "created_at"
                            )
                    };

                    transactions.add(row);
                }
            }
        }

        return transactions;
    }


    /**
     * Returns total number of wallet transactions.
     */
    public int getTotalTransactions()
            throws SQLException {

        String sql =
                "SELECT COUNT(*) "
                        + "FROM wallet_transactions";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {

                return resultSet.getInt(1);
            }
        }

        return 0;
    }
}