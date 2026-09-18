package com.sairam.game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {

    // =========================================================
    // CREATE TRANSACTION
    // =========================================================

    public void createTransaction(
            String playerName,
            String transactionType,
            int amount
    ) throws SQLException {

        if (playerName == null || playerName.trim().isEmpty()) {
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

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Transaction amount must be greater than 0."
            );
        }

        String sql =
                "INSERT INTO transactions " +
                "(player_name, transaction_type, amount) " +
                "VALUES (?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, playerName);
            statement.setString(2, transactionType);
            statement.setInt(3, amount);

            statement.executeUpdate();
        }
    }

    // =========================================================
    // DEPOSIT TRANSACTION
    // =========================================================

    public void recordDeposit(
            String playerName,
            int amount
    ) throws SQLException {

        createTransaction(
                playerName,
                "DEPOSIT",
                amount
        );
    }

    // =========================================================
    // WITHDRAW TRANSACTION
    // =========================================================

    public void recordWithdraw(
            String playerName,
            int amount
    ) throws SQLException {

        createTransaction(
                playerName,
                "WITHDRAW",
                amount
        );
    }

    // =========================================================
    // GAME ENTRY TRANSACTION
    // =========================================================

    public void recordGameEntry(
            String playerName,
            int amount
    ) throws SQLException {

        createTransaction(
                playerName,
                "GAME_ENTRY",
                amount
        );
    }

    // =========================================================
    // GAME WIN TRANSACTION
    // =========================================================

    public void recordGameWin(
            String playerName,
            int amount
    ) throws SQLException {

        createTransaction(
                playerName,
                "GAME_WIN",
                amount
        );
    }

    // =========================================================
    // GET ALL TRANSACTIONS
    // =========================================================

    public List<Transaction> getAllTransactions()
            throws SQLException {

        List<Transaction> transactions =
                new ArrayList<>();

        String sql =
                "SELECT transaction_id, " +
                "player_name, " +
                "transaction_type, " +
                "amount, " +
                "transaction_date " +
                "FROM transactions " +
                "ORDER BY transaction_date DESC";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Transaction transaction =
                        createTransactionFromResultSet(
                                resultSet
                        );

                transactions.add(transaction);
            }
        }

        return transactions;
    }

    // =========================================================
    // GET PLAYER TRANSACTIONS
    // =========================================================

    public List<Transaction> getPlayerTransactions(
            String playerName
    ) throws SQLException {

        List<Transaction> transactions =
                new ArrayList<>();

        String sql =
                "SELECT transaction_id, " +
                "player_name, " +
                "transaction_type, " +
                "amount, " +
                "transaction_date " +
                "FROM transactions " +
                "WHERE player_name = ? " +
                "ORDER BY transaction_date DESC";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, playerName);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    Transaction transaction =
                            createTransactionFromResultSet(
                                    resultSet
                            );

                    transactions.add(transaction);
                }
            }
        }

        return transactions;
    }

    // =========================================================
    // GET RECENT PLAYER TRANSACTIONS
    // =========================================================

    public List<Transaction> getRecentTransactions(
            String playerName,
            int limit
    ) throws SQLException {

        List<Transaction> transactions =
                new ArrayList<>();

        if (limit <= 0) {
            return transactions;
        }

        String sql =
                "SELECT transaction_id, " +
                "player_name, " +
                "transaction_type, " +
                "amount, " +
                "transaction_date " +
                "FROM transactions " +
                "WHERE player_name = ? " +
                "ORDER BY transaction_date DESC " +
                "LIMIT ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, playerName);
            statement.setInt(2, limit);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    Transaction transaction =
                            createTransactionFromResultSet(
                                    resultSet
                            );

                    transactions.add(transaction);
                }
            }
        }

        return transactions;
    }

    // =========================================================
    // GET TRANSACTION BY ID
    // =========================================================

    public Transaction getTransactionById(
            int transactionId
    ) throws SQLException {

        String sql =
                "SELECT transaction_id, " +
                "player_name, " +
                "transaction_type, " +
                "amount, " +
                "transaction_date " +
                "FROM transactions " +
                "WHERE transaction_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, transactionId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return createTransactionFromResultSet(
                            resultSet
                    );
                }
            }
        }

        return null;
    }

    // =========================================================
    // GET TRANSACTION COUNT
    // =========================================================

    public int getTransactionCount(
            String playerName
    ) throws SQLException {

        String sql =
                "SELECT COUNT(*) " +
                "FROM transactions " +
                "WHERE player_name = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, playerName);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }

        return 0;
    }

    // =========================================================
    // GET TOTAL DEPOSITED
    // =========================================================

    public int getTotalDeposited(
            String playerName
    ) throws SQLException {

        String sql =
                "SELECT COALESCE(SUM(amount), 0) " +
                "FROM transactions " +
                "WHERE player_name = ? " +
                "AND transaction_type = 'DEPOSIT'";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, playerName);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }

        return 0;
    }

    // =========================================================
    // GET TOTAL WITHDRAWN
    // =========================================================

    public int getTotalWithdrawn(
            String playerName
    ) throws SQLException {

        String sql =
                "SELECT COALESCE(SUM(amount), 0) " +
                "FROM transactions " +
                "WHERE player_name = ? " +
                "AND transaction_type = 'WITHDRAW'";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, playerName);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }

        return 0;
    }

    // =========================================================
    // GET TOTAL GAME ENTRY
    // =========================================================

    public int getTotalGameEntry(
            String playerName
    ) throws SQLException {

        String sql =
                "SELECT COALESCE(SUM(amount), 0) " +
                "FROM transactions " +
                "WHERE player_name = ? " +
                "AND transaction_type = 'GAME_ENTRY'";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, playerName);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }

        return 0;
    }

    // =========================================================
    // GET TOTAL GAME WIN
    // =========================================================

    public int getTotalGameWin(
            String playerName
    ) throws SQLException {

        String sql =
                "SELECT COALESCE(SUM(amount), 0) " +
                "FROM transactions " +
                "WHERE player_name = ? " +
                "AND transaction_type = 'GAME_WIN'";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, playerName);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }

        return 0;
    }

    // =========================================================
    // DELETE PLAYER TRANSACTIONS
    // =========================================================

    public void deletePlayerTransactions(
            String playerName
    ) throws SQLException {

        String sql =
                "DELETE FROM transactions " +
                "WHERE player_name = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, playerName);

            statement.executeUpdate();
        }
    }

    // =========================================================
    // CREATE TRANSACTION OBJECT
    // =========================================================

    private Transaction createTransactionFromResultSet(
            ResultSet resultSet
    ) throws SQLException {

        int transactionId =
                resultSet.getInt("transaction_id");

        String playerName =
                resultSet.getString("player_name");

        String transactionType =
                resultSet.getString("transaction_type");

        int amount =
                resultSet.getInt("amount");

        java.sql.Timestamp transactionDate =
                resultSet.getTimestamp(
                        "transaction_date"
                );

        return new Transaction(
                transactionId,
                playerName,
                transactionType,
                amount,
                transactionDate
        );
    }
}