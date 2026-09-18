
package com.sairam.game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DepositDao {

    // ============================================================
    // CREATE DEPOSIT REQUEST
    // ============================================================

    public int createDepositRequest(String playerName, int amount)
            throws SQLException {

        if (playerName == null || playerName.trim().isEmpty()) {
            throw new SQLException("Player name is required.");
        }

        if (amount <= 0) {
            throw new SQLException("Deposit amount must be greater than zero.");
        }

        playerName = playerName.trim();

        try (Connection con = DatabaseConnection.getConnection()) {

            // ----------------------------------------------------
            // Check player exists
            // ----------------------------------------------------

            String playerSql =
                    "SELECT player_name " +
                    "FROM players " +
                    "WHERE player_name = ?";

            try (PreparedStatement ps =
                         con.prepareStatement(playerSql)) {

                ps.setString(1, playerName);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        throw new SQLException(
                                "Player account not found: " + playerName
                        );
                    }
                }
            }

            // ----------------------------------------------------
            // Prevent duplicate pending request
            // ----------------------------------------------------

            String duplicateSql =
                    "SELECT request_id " +
                    "FROM deposit_requests " +
                    "WHERE player_name = ? " +
                    "AND amount = ? " +
                    "AND status = 'PENDING' " +
                    "LIMIT 1";

            try (PreparedStatement ps =
                         con.prepareStatement(duplicateSql)) {

                ps.setString(1, playerName);
                ps.setInt(2, amount);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        throw new SQLException(
                                "A pending deposit request already exists. "
                                + "Request ID: " + rs.getInt("request_id")
                        );
                    }
                }
            }

            // ----------------------------------------------------
            // Insert request
            // ----------------------------------------------------

            String insertSql =
                    "INSERT INTO deposit_requests " +
                    "(player_name, amount, status) " +
                    "VALUES (?, ?, 'PENDING') " +
                    "RETURNING request_id";

            try (PreparedStatement ps =
                         con.prepareStatement(insertSql)) {

                ps.setString(1, playerName);
                ps.setInt(2, amount);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        return rs.getInt("request_id");
                    }

                    throw new SQLException(
                            "Unable to create deposit request."
                    );
                }
            }
        }
    }


    // ============================================================
    // GET PLAYER DEPOSIT REQUESTS
    // ============================================================

    public List<Object[]> getPlayerDepositRequests(String playerName)
            throws SQLException {

        List<Object[]> requests = new ArrayList<>();

        if (playerName == null || playerName.trim().isEmpty()) {
            return requests;
        }

        String sql =
                "SELECT request_id, amount, status, created_at " +
                "FROM deposit_requests " +
                "WHERE player_name = ? " +
                "ORDER BY created_at DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, playerName.trim());

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Object[] row = new Object[4];

                    row[0] = rs.getInt("request_id");
                    row[1] = rs.getInt("amount");
                    row[2] = rs.getString("status");
                    row[3] = rs.getTimestamp("created_at");

                    requests.add(row);
                }
            }
        }

        return requests;
    }


    // ============================================================
    // GET ALL PENDING DEPOSIT REQUESTS
    // ============================================================

    public List<Object[]> getPendingDepositRequests()
            throws SQLException {

        List<Object[]> requests = new ArrayList<>();

        String sql =
                "SELECT request_id, player_name, amount, status, created_at " +
                "FROM deposit_requests " +
                "WHERE status = 'PENDING' " +
                "ORDER BY created_at ASC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Object[] row = new Object[5];

                // IMPORTANT:
                // AdminUi.java expects this exact order.

                row[0] = rs.getInt("request_id");
                row[1] = rs.getString("player_name");
                row[2] = rs.getInt("amount");
                row[3] = rs.getString("status");
                row[4] = rs.getTimestamp("created_at");

                requests.add(row);
            }
        }

        return requests;
    }


    // ============================================================
    // GET SINGLE DEPOSIT REQUEST
    // ============================================================

    public Object[] getDepositRequest(int requestId)
            throws SQLException {

        String sql =
                "SELECT request_id, player_name, amount, status, " +
                "created_at, approved_at " +
                "FROM deposit_requests " +
                "WHERE request_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, requestId);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                Object[] row = new Object[6];

                row[0] = rs.getInt("request_id");
                row[1] = rs.getString("player_name");
                row[2] = rs.getInt("amount");
                row[3] = rs.getString("status");
                row[4] = rs.getTimestamp("created_at");
                row[5] = rs.getTimestamp("approved_at");

                return row;
            }
        }
    }


    // ============================================================
    // APPROVE DEPOSIT
    // ============================================================

    public void approveDeposit(int requestId)
            throws SQLException {

        if (requestId <= 0) {
            throw new SQLException("Invalid deposit request ID.");
        }

        Connection con = null;

        try {

            con = DatabaseConnection.getConnection();

            con.setAutoCommit(false);

            // ----------------------------------------------------
            // Lock deposit request
            // ----------------------------------------------------

            String requestSql =
                    "SELECT request_id, player_name, amount, status " +
                    "FROM deposit_requests " +
                    "WHERE request_id = ? " +
                    "FOR UPDATE";

            String playerName;
            int amount;
            String status;

            try (PreparedStatement ps =
                         con.prepareStatement(requestSql)) {

                ps.setInt(1, requestId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {

                        throw new SQLException(
                                "Deposit request not found. "
                                + "Request ID: " + requestId
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

            // ----------------------------------------------------
            // Check status
            // ----------------------------------------------------

            if (!"PENDING".equalsIgnoreCase(status)) {

                throw new SQLException(
                        "Deposit request is already "
                        + status.toLowerCase()
                        + ". Request ID: "
                        + requestId
                );
            }

            if (amount <= 0) {

                throw new SQLException(
                        "Invalid deposit amount."
                );
            }

            // ----------------------------------------------------
            // Lock player row
            // ----------------------------------------------------

            String playerSql =
                    "SELECT credits " +
                    "FROM players " +
                    "WHERE player_name = ? " +
                    "FOR UPDATE";

            int balanceBefore;

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

                    balanceBefore =
                            rs.getInt("credits");
                }
            }

            // ----------------------------------------------------
            // Calculate new balance
            // ----------------------------------------------------

            long newBalanceLong =
                    (long) balanceBefore + amount;

            if (newBalanceLong > Integer.MAX_VALUE) {

                throw new SQLException(
                        "Credit balance exceeds the maximum allowed value."
                );
            }

            int balanceAfter =
                    (int) newBalanceLong;

            // ----------------------------------------------------
            // Update player credits
            // ----------------------------------------------------

            String updatePlayerSql =
                    "UPDATE players " +
                    "SET credits = ? " +
                    "WHERE player_name = ?";

            try (PreparedStatement ps =
                         con.prepareStatement(updatePlayerSql)) {

                ps.setInt(1, balanceAfter);
                ps.setString(2, playerName);

                int updated =
                        ps.executeUpdate();

                if (updated != 1) {

                    throw new SQLException(
                            "Unable to update player credits."
                    );
                }
            }

            // ----------------------------------------------------
            // Mark deposit APPROVED
            // ----------------------------------------------------

            String updateRequestSql =
                    "UPDATE deposit_requests " +
                    "SET status = 'APPROVED', " +
                    "approved_at = CURRENT_TIMESTAMP " +
                    "WHERE request_id = ?";

            try (PreparedStatement ps =
                         con.prepareStatement(updateRequestSql)) {

                ps.setInt(1, requestId);

                int updated =
                        ps.executeUpdate();

                if (updated != 1) {

                    throw new SQLException(
                            "Unable to approve deposit request."
                    );
                }
            }

            // ----------------------------------------------------
            // Add wallet transaction
            // ----------------------------------------------------

            insertWalletTransaction(
                    con,
                    playerName,
                    amount,
                    balanceBefore,
                    balanceAfter,
                    "Deposit approved - Request ID: "
                            + requestId
            );

            // ----------------------------------------------------
            // Commit
            // ----------------------------------------------------

            con.commit();

        } catch (SQLException e) {

            if (con != null) {

                try {
                    con.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
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


    // ============================================================
    // REJECT DEPOSIT
    // ============================================================

    public void rejectDeposit(int requestId)
            throws SQLException {

        if (requestId <= 0) {
            throw new SQLException(
                    "Invalid deposit request ID."
            );
        }

        String sql =
                "UPDATE deposit_requests " +
                "SET status = 'REJECTED' " +
                "WHERE request_id = ? " +
                "AND status = 'PENDING'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, requestId);

            int updated =
                    ps.executeUpdate();

            if (updated == 0) {

                throw new SQLException(
                        "Deposit request was not found "
                        + "or is no longer pending."
                );
            }
        }
    }


    // ============================================================
    // INSERT WALLET TRANSACTION
    // ============================================================

    private void insertWalletTransaction(
            Connection con,
            String playerName,
            int amount,
            int balanceBefore,
            int balanceAfter,
            String description)
            throws SQLException {

        String sql =
                "INSERT INTO wallet_transactions " +
                "(player_name, transaction_type, amount, " +
                "balance_before, balance_after, description) " +
                "VALUES (?, 'DEPOSIT', ?, ?, ?, ?)";

        try (PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, playerName);
            ps.setInt(2, amount);
            ps.setInt(3, balanceBefore);
            ps.setInt(4, balanceAfter);
            ps.setString(5, description);

            ps.executeUpdate();
        }
    }


    // ============================================================
    // GET DEPOSIT COUNT
    // ============================================================

    public int getPendingDepositCount()
            throws SQLException {

        String sql =
                "SELECT COUNT(*) " +
                "FROM deposit_requests " +
                "WHERE status = 'PENDING'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }


    // ============================================================
    // GET TOTAL APPROVED DEPOSIT
    // ============================================================

    public long getTotalApprovedDeposits()
            throws SQLException {

        String sql =
                "SELECT COALESCE(SUM(amount), 0) " +
                "FROM deposit_requests " +
                "WHERE status = 'APPROVED'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        }

        return 0;
    }


    // ============================================================
    // GET PLAYER APPROVED DEPOSIT TOTAL
    // ============================================================

    public long getPlayerApprovedDepositTotal(
            String playerName)
            throws SQLException {

        String sql =
                "SELECT COALESCE(SUM(amount), 0) " +
                "FROM deposit_requests " +
                "WHERE player_name = ? " +
                "AND status = 'APPROVED'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, playerName);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }

        return 0;
    }
}
