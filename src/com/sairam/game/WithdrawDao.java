
package com.sairam.game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class WithdrawDao {

    // =========================================================
    // CREATE WITHDRAWAL REQUEST
    // =========================================================

    public int createWithdrawRequest(String playerName, int amount)
            throws SQLException {

        if (playerName == null || playerName.trim().isEmpty()) {
            throw new SQLException("Player name is required.");
        }

        if (amount <= 0) {
            throw new SQLException("Withdrawal amount must be greater than zero.");
        }

        String sql =
                "INSERT INTO withdraw_requests " +
                "(player_name, amount, status) " +
                "VALUES (?, ?, 'PENDING') " +
                "RETURNING request_id";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, playerName.trim());
            ps.setInt(2, amount);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("request_id");
                }

                throw new SQLException(
                        "Unable to create withdrawal request."
                );
            }
        }
    }


    // =========================================================
    // GET PLAYER CREDITS
    // =========================================================

    public int getPlayerCredits(String playerName)
            throws SQLException {

        if (playerName == null || playerName.trim().isEmpty()) {
            throw new SQLException("Player name is required.");
        }

        String sql =
                "SELECT credits " +
                "FROM players " +
                "WHERE player_name = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, playerName.trim());

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    throw new SQLException(
                            "Player account not found: "
                                    + playerName
                    );
                }

                return rs.getInt("credits");
            }
        }
    }


    // =========================================================
    // GET PLAYER WITHDRAWAL HISTORY
    //
    // Object[] structure:
    // [0] request_id
    // [1] amount
    // [2] status
    // [3] created_at
    // [4] approved_at
    // =========================================================

    public List<Object[]> getPlayerWithdrawRequests(String playerName)
            throws SQLException {

        if (playerName == null || playerName.trim().isEmpty()) {
            throw new SQLException("Player name is required.");
        }

        List<Object[]> list = new ArrayList<>();

        String sql =
                "SELECT request_id, amount, status, " +
                "       created_at, approved_at " +
                "FROM withdraw_requests " +
                "WHERE player_name = ? " +
                "ORDER BY request_id DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, playerName.trim());

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    int requestId = rs.getInt("request_id");
                    int amount = rs.getInt("amount");
                    String status = rs.getString("status");

                    Timestamp createdAt =
                            rs.getTimestamp("created_at");

                    Timestamp approvedAt =
                            rs.getTimestamp("approved_at");

                    list.add(new Object[] {
                            requestId,
                            amount,
                            status,
                            createdAt,
                            approvedAt
                    });
                }
            }
        }

        return list;
    }


    // =========================================================
    // GET ALL PENDING WITHDRAWAL REQUESTS
    //
    // Object[] structure:
    // [0] request_id
    // [1] player_name
    // [2] amount
    // [3] status
    // [4] created_at
    // =========================================================

    public List<Object[]> getPendingWithdrawRequests()
            throws SQLException {

        List<Object[]> list = new ArrayList<>();

        String sql =
                "SELECT request_id, player_name, amount, " +
                "       status, created_at " +
                "FROM withdraw_requests " +
                "WHERE UPPER(status) = 'PENDING' " +
                "ORDER BY created_at ASC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int requestId =
                        rs.getInt("request_id");

                String playerName =
                        rs.getString("player_name");

                int amount =
                        rs.getInt("amount");

                String status =
                        rs.getString("status");

                Timestamp createdAt =
                        rs.getTimestamp("created_at");

                list.add(new Object[] {
                        requestId,
                        playerName,
                        amount,
                        status,
                        createdAt
                });
            }
        }

        return list;
    }


    // =========================================================
    // GET SINGLE WITHDRAWAL REQUEST
    //
    // Object[] structure:
    // [0] request_id
    // [1] player_name
    // [2] amount
    // [3] status
    // [4] created_at
    // [5] approved_at
    // =========================================================

    public Object[] getWithdrawRequest(int requestId)
            throws SQLException {

        if (requestId <= 0) {
            throw new SQLException("Invalid withdrawal request ID.");
        }

        String sql =
                "SELECT request_id, player_name, amount, status, " +
                "       created_at, approved_at " +
                "FROM withdraw_requests " +
                "WHERE request_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, requestId);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    throw new SQLException(
                            "Withdrawal request not found: "
                                    + requestId
                    );
                }

                return new Object[] {
                        rs.getInt("request_id"),
                        rs.getString("player_name"),
                        rs.getInt("amount"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("approved_at")
                };
            }
        }
    }


    // =========================================================
    // APPROVE WITHDRAWAL
    //
    // Steps:
    // 1. Lock withdrawal request
    // 2. Check request is PENDING
    // 3. Lock player
    // 4. Check player balance
    // 5. Deduct credits
    // 6. Mark withdrawal APPROVED
    // 7. Insert wallet transaction
    // 8. Commit
    // =========================================================

    public void approveWithdraw(int requestId)
            throws SQLException {

        if (requestId <= 0) {
            throw new SQLException("Invalid withdrawal request ID.");
        }

        Connection con = null;

        try {
            con = DatabaseConnection.getConnection();

            con.setAutoCommit(false);

            String playerName;
            int amount;
            String status;

            // -------------------------------------------------
            // LOCK WITHDRAWAL REQUEST
            // -------------------------------------------------

            String requestSql =
                    "SELECT player_name, amount, status " +
                    "FROM withdraw_requests " +
                    "WHERE request_id = ? " +
                    "FOR UPDATE";

            try (PreparedStatement ps =
                         con.prepareStatement(requestSql)) {

                ps.setInt(1, requestId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        throw new SQLException(
                                "Withdrawal request not found: "
                                        + requestId
                        );
                    }

                    playerName =
                            rs.getString("player_name");

                    amount =
                            rs.getInt("amount");

                    status =
                            rs.getString("status");
                }
            }

            // -------------------------------------------------
            // CHECK STATUS
            // -------------------------------------------------

            if (!"PENDING".equalsIgnoreCase(status)) {
                throw new SQLException(
                        "Withdrawal request is already "
                                + status + "."
                );
            }

            // -------------------------------------------------
            // LOCK PLAYER
            // -------------------------------------------------

            int currentCredits;

            String playerSql =
                    "SELECT credits " +
                    "FROM players " +
                    "WHERE player_name = ? " +
                    "FOR UPDATE";

            try (PreparedStatement ps =
                         con.prepareStatement(playerSql)) {

                ps.setString(1, playerName);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        throw new SQLException(
                                "Player account not found: "
                                        + playerName
                        );
                    }

                    currentCredits =
                            rs.getInt("credits");
                }
            }

            // -------------------------------------------------
            // CHECK BALANCE
            // -------------------------------------------------

            if (currentCredits < amount) {

                throw new SQLException(
                        "Insufficient player balance. " +
                        "Available: " + currentCredits +
                        ", Withdrawal: " + amount
                );
            }

            // -------------------------------------------------
            // DEDUCT PLAYER CREDITS
            // -------------------------------------------------

            int newBalance =
                    currentCredits - amount;

            String updatePlayerSql =
                    "UPDATE players " +
                    "SET credits = ? " +
                    "WHERE player_name = ?";

            try (PreparedStatement ps =
                         con.prepareStatement(updatePlayerSql)) {

                ps.setInt(1, newBalance);
                ps.setString(2, playerName);

                int updated =
                        ps.executeUpdate();

                if (updated != 1) {
                    throw new SQLException(
                            "Unable to update player balance."
                    );
                }
            }

            // -------------------------------------------------
            // MARK REQUEST AS APPROVED
            // -------------------------------------------------

            String updateRequestSql =
                    "UPDATE withdraw_requests " +
                    "SET status = 'APPROVED', " +
                    "    approved_at = CURRENT_TIMESTAMP " +
                    "WHERE request_id = ?";

            try (PreparedStatement ps =
                         con.prepareStatement(updateRequestSql)) {

                ps.setInt(1, requestId);

                int updated =
                        ps.executeUpdate();

                if (updated != 1) {
                    throw new SQLException(
                            "Unable to approve withdrawal request."
                    );
                }
            }

            // -------------------------------------------------
            // INSERT WALLET TRANSACTION
            // -------------------------------------------------

            insertWalletTransaction(
                    con,
                    playerName,
                    amount,
                    "WITHDRAW",
                    "Withdrawal approved. Request ID: "
                            + requestId
            );

            // -------------------------------------------------
            // COMMIT
            // -------------------------------------------------

            con.commit();

        } catch (SQLException e) {

            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
            }

            throw e;

        } finally {

            if (con != null) {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException ignored) {
                }

                try {
                    con.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }


    // =========================================================
    // REJECT WITHDRAWAL
    // =========================================================

    public void rejectWithdraw(int requestId)
            throws SQLException {

        if (requestId <= 0) {
            throw new SQLException("Invalid withdrawal request ID.");
        }

        String sql =
                "UPDATE withdraw_requests " +
                "SET status = 'REJECTED' " +
                "WHERE request_id = ? " +
                "AND UPPER(status) = 'PENDING'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, requestId);

            int updated =
                    ps.executeUpdate();

            if (updated == 0) {
                throw new SQLException(
                        "Withdrawal request not found " +
                        "or it is no longer pending."
                );
            }
        }
    }


    // =========================================================
    // INSERT WALLET TRANSACTION
    // =========================================================

    private void insertWalletTransaction(
            Connection con,
            String playerName,
            int amount,
            String transactionType,
            String description)
            throws SQLException {

        String sql =
                "INSERT INTO wallet_transactions " +
                "(player_name, amount, transaction_type, description) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, playerName);
            ps.setInt(2, amount);
            ps.setString(3, transactionType);
            ps.setString(4, description);

            ps.executeUpdate();
        }
    }


    // =========================================================
    // PUBLIC WALLET TRANSACTION METHOD
    // =========================================================

    public void insertWalletTransaction(
            String playerName,
            int amount,
            String transactionType,
            String description)
            throws SQLException {

        if (playerName == null || playerName.trim().isEmpty()) {
            throw new SQLException("Player name is required.");
        }

        if (amount <= 0) {
            throw new SQLException("Transaction amount must be greater than zero.");
        }

        if (transactionType == null
                || transactionType.trim().isEmpty()) {

            throw new SQLException(
                    "Transaction type is required."
            );
        }

        String sql =
                "INSERT INTO wallet_transactions " +
                "(player_name, amount, transaction_type, description) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, playerName.trim());
            ps.setInt(2, amount);
            ps.setString(3, transactionType.trim());
            ps.setString(4, description);

            ps.executeUpdate();
        }
    }


    // =========================================================
    // GET PENDING WITHDRAWAL COUNT
    // =========================================================

    public int getPendingWithdrawCount()
            throws SQLException {

        String sql =
                "SELECT COUNT(*) " +
                "FROM withdraw_requests " +
                "WHERE UPPER(status) = 'PENDING'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

            return 0;
        }
    }


    // =========================================================
    // GET TOTAL APPROVED WITHDRAWALS
    //
    // Returns total withdrawal amount.
    // =========================================================

    public int getTotalApprovedWithdrawals()
            throws SQLException {

        String sql =
                "SELECT COALESCE(SUM(amount), 0) " +
                "FROM withdraw_requests " +
                "WHERE UPPER(status) = 'APPROVED'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

            return 0;
        }
    }


    // =========================================================
    // GET PLAYER APPROVED WITHDRAWAL TOTAL
    // =========================================================

    public int getPlayerApprovedWithdrawTotal(
            String playerName)
            throws SQLException {

        if (playerName == null || playerName.trim().isEmpty()) {
            throw new SQLException("Player name is required.");
        }

        String sql =
                "SELECT COALESCE(SUM(amount), 0) " +
                "FROM withdraw_requests " +
                "WHERE player_name = ? " +
                "AND UPPER(status) = 'APPROVED'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, playerName.trim());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        }
    }


    // =========================================================
    // GET PLAYER PENDING WITHDRAWAL TOTAL
    // =========================================================

    public int getPlayerPendingWithdrawTotal(
            String playerName)
            throws SQLException {

        if (playerName == null || playerName.trim().isEmpty()) {
            throw new SQLException("Player name is required.");
        }

        String sql =
                "SELECT COALESCE(SUM(amount), 0) " +
                "FROM withdraw_requests " +
                "WHERE player_name = ? " +
                "AND UPPER(status) = 'PENDING'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, playerName.trim());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        }
    }


    // =========================================================
    // GET PLAYER REJECTED WITHDRAWAL TOTAL
    // =========================================================

    public int getPlayerRejectedWithdrawTotal(
            String playerName)
            throws SQLException {

        if (playerName == null || playerName.trim().isEmpty()) {
            throw new SQLException("Player name is required.");
        }

        String sql =
                "SELECT COALESCE(SUM(amount), 0) " +
                "FROM withdraw_requests " +
                "WHERE player_name = ? " +
                "AND UPPER(status) = 'REJECTED'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, playerName.trim());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        }
    }
}
