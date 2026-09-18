package com.sairam.game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class AdminDao {

    // =========================================================
    // ADMIN LOGIN
    // =========================================================

    private static final String PBKDF2_ALGORITHM =
            "PBKDF2WithHmacSHA256";

    private static final int DEFAULT_ITERATIONS = 600000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;

    // =========================================================
    // ADMIN LOGIN
    // =========================================================

    public boolean validateAdmin(
            String username,
            String password
    ) throws SQLException {

        if (username == null || username.trim().isEmpty()
                || password == null || password.isEmpty()) {
            return false;
        }

        String sql =
                "SELECT admin_id, password "
                        + "FROM admins "
                        + "WHERE LOWER(username) = LOWER(?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, username.trim());

            try (ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.next()) {
                    return false;
                }

                String storedPassword =
                        resultSet.getString("password");

                return verifyPassword(password, storedPassword);
            }
        }
    }

    // =========================================================
    // ADMIN PASSWORD HASH
    // =========================================================

    private String hashPassword(String password) {

        if (password == null) {
            throw new IllegalArgumentException(
                    "Password cannot be null."
            );
        }

        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);

        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                DEFAULT_ITERATIONS,
                KEY_LENGTH
        );

        try {

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(
                            PBKDF2_ALGORITHM
                    );

            byte[] hash = factory.generateSecret(spec)
                    .getEncoded();

            return "PBKDF2$"
                    + DEFAULT_ITERATIONS
                    + "$"
                    + Base64.getEncoder().encodeToString(salt)
                    + "$"
                    + Base64.getEncoder().encodeToString(hash);

        } catch (NoSuchAlgorithmException
                | InvalidKeySpecException e) {

            throw new IllegalStateException(
                    "Unable to hash password.",
                    e
            );

        } finally {
            spec.clearPassword();
        }
    }

    // =========================================================
    // VERIFY ADMIN PASSWORD
    // =========================================================

    private boolean verifyPassword(
            String password,
            String storedPassword
    ) {

        if (password == null || storedPassword == null) {
            return false;
        }

        // New secure format:
        // PBKDF2$iterations$base64Salt$base64Hash
        if (storedPassword.startsWith("PBKDF2$")) {

            try {

                String[] parts =
                        storedPassword.split("\\$", -1);

                if (parts.length != 4) {
                    return false;
                }

                int iterations =
                        Integer.parseInt(parts[1]);

                if (iterations <= 0) {
                    return false;
                }

                byte[] salt =
                        Base64.getDecoder().decode(parts[2]);

                byte[] expectedHash =
                        Base64.getDecoder().decode(parts[3]);

                if (salt.length == 0 || expectedHash.length == 0) {
                    return false;
                }

                PBEKeySpec spec = new PBEKeySpec(
                        password.toCharArray(),
                        salt,
                        iterations,
                        expectedHash.length * 8
                );

                try {

                    SecretKeyFactory factory =
                            SecretKeyFactory.getInstance(
                                    PBKDF2_ALGORITHM
                            );

                    byte[] actualHash =
                            factory.generateSecret(spec)
                                    .getEncoded();

                    return MessageDigest.isEqual(
                            actualHash,
                            expectedHash
                    );

                } finally {
                    spec.clearPassword();
                }

            } catch (
                    IllegalArgumentException
                    | NoSuchAlgorithmException
                    | InvalidKeySpecException e
            ) {
                return false;
            }
        }

        // Backward compatibility for old plaintext admin records.
        // New password changes always use PBKDF2.
        return MessageDigest.isEqual(
                password.getBytes(StandardCharsets.UTF_8),
                storedPassword.getBytes(StandardCharsets.UTF_8)
        );
    }

    // =========================================================
    // CHANGE ADMIN PASSWORD
    // =========================================================

    public void changeAdminPassword(
            String username,
            String currentPassword,
            String newPassword
    ) throws SQLException {

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Admin username is required."
            );
        }

        if (currentPassword == null || currentPassword.isEmpty()) {
            throw new IllegalArgumentException(
                    "Current password is required."
            );
        }

        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException(
                    "New password is required."
            );
        }

        if (!CredentialRules.isValidPassword(newPassword)) {
            throw new IllegalArgumentException(
                    CredentialRules.PASSWORD_REQUIREMENTS
            );
        }

        if (currentPassword.equals(newPassword)) {
            throw new IllegalArgumentException(
                    "New password must be different from the current password."
            );
        }

        String storedPassword;

        String selectSql =
                "SELECT password FROM admins "
                        + "WHERE LOWER(username) = LOWER(?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(selectSql)
        ) {

            statement.setString(1, username.trim());

            try (ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.next()) {
                    throw new IllegalArgumentException(
                            "Admin account not found."
                    );
                }

                storedPassword =
                        resultSet.getString("password");
            }
        }

        if (!verifyPassword(currentPassword, storedPassword)) {
            throw new IllegalArgumentException(
                    "Current password is incorrect."
            );
        }

        String newPasswordHash =
                hashPassword(newPassword);

        String updateSql =
                "UPDATE admins SET password = ? "
                        + "WHERE LOWER(username) = LOWER(?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(updateSql)
        ) {

            statement.setString(1, newPasswordHash);
            statement.setString(2, username.trim());

            int rowsUpdated =
                    statement.executeUpdate();

            if (rowsUpdated != 1) {
                throw new SQLException(
                        "Administrator password could not be updated."
                );
            }
        }
    }


    // =========================================================
    // ADD NEW PLAYER
    // =========================================================

    public void addPlayer(
            String playerName,
            int startingCredits
    ) throws SQLException {

        if (playerName == null ||
                playerName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Player name cannot be empty."
            );
        }

        playerName = playerName.trim();

        if (!CredentialRules.isValidUsername(playerName)) {
            throw new IllegalArgumentException(
                    CredentialRules.USERNAME_REQUIREMENTS
            );
        }

        if (startingCredits < 0) {

            throw new IllegalArgumentException(
                    "Starting credits cannot be negative."
            );
        }

        Connection connection = null;

        try {

            connection =
                    DatabaseConnection.getConnection();

            connection.setAutoCommit(false);

            // -------------------------------------------------
            // Check duplicate player
            // -------------------------------------------------

            String checkSql =
                    "SELECT player_name "
                            + "FROM players "
                            + "WHERE player_name = ? "
                            + "FOR UPDATE";

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    checkSql
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

                    if (resultSet.next()) {

                        throw new SQLException(
                                "Player already exists: "
                                        + playerName
                        );
                    }
                }
            }

            // -------------------------------------------------
            // Insert player
            // -------------------------------------------------

            String insertPlayerSql =
                    "INSERT INTO players "
                            + "(player_name, credits) "
                            + "VALUES (?, ?)";

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    insertPlayerSql
                            )
            ) {

                statement.setString(
                        1,
                        playerName
                );

                statement.setInt(
                        2,
                        startingCredits
                );

                int rowsInserted =
                        statement.executeUpdate();

                if (rowsInserted != 1) {

                    throw new SQLException(
                            "Player could not be created."
                    );
                }
            }

            // -------------------------------------------------
            // Create wallet transaction
            // -------------------------------------------------

            String transactionSql =
                    "INSERT INTO wallet_transactions "
                            + "(player_name, "
                            + "transaction_type, "
                            + "amount, "
                            + "balance_before, "
                            + "balance_after, "
                            + "description) "
                            + "VALUES (?, ?, ?, ?, ?, ?)";

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
                        "WALLET_CREATE"
                );

                statement.setInt(
                        3,
                        startingCredits
                );

                statement.setInt(
                        4,
                        0
                );

                statement.setInt(
                        5,
                        startingCredits
                );

                statement.setString(
                        6,
                        "Player account created by administrator"
                );

                int rowsInserted =
                        statement.executeUpdate();

                if (rowsInserted != 1) {

                    throw new SQLException(
                            "Wallet transaction could not be created."
                    );
                }
            }

            // -------------------------------------------------
            // Commit
            // -------------------------------------------------

            connection.commit();

            System.out.println(
                    "New player created successfully: "
                            + playerName
            );

            System.out.println(
                    "Starting credits: "
                            + startingCredits
            );

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

                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }


    // =========================================================
    // GET ALL PLAYERS
    // =========================================================

    public List<Object[]> getAllPlayers()
            throws SQLException {

        List<Object[]> players =
                new ArrayList<>();

        String sql =
                "SELECT player_name, credits "
                        + "FROM players "
                        + "ORDER BY player_name";

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

                        resultSet.getString(
                                "player_name"
                        ),

                        resultSet.getInt(
                                "credits"
                        )
                };

                players.add(row);
            }
        }

        return players;
    }


    // =========================================================
    // SEARCH PLAYERS
    // =========================================================

    public List<Object[]> searchPlayers(
            String searchText
    ) throws SQLException {

        List<Object[]> players =
                new ArrayList<>();

        String sql =
                "SELECT player_name, credits "
                        + "FROM players "
                        + "WHERE LOWER(player_name) "
                        + "LIKE LOWER(?) "
                        + "ORDER BY player_name";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    "%" + searchText.trim() + "%"
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    Object[] row = {

                            resultSet.getString(
                                    "player_name"
                            ),

                            resultSet.getInt(
                                    "credits"
                            )
                    };

                    players.add(row);
                }
            }
        }

        return players;
    }


    // =========================================================
    // GET PLAYER CREDITS
    // =========================================================

    public int getPlayerCredits(
            String playerName
    ) throws SQLException {

        if (playerName == null ||
                playerName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Player name cannot be empty."
            );
        }

        String sql =
                "SELECT credits "
                        + "FROM players "
                        + "WHERE player_name = ?";

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

                    return resultSet.getInt(
                            "credits"
                    );
                }
            }
        }

        throw new SQLException(
                "Player not found: "
                        + playerName
        );
    }


    // =========================================================
    // UPDATE CREDITS
    // =========================================================

    public void updateCredits(
            String playerName,
            int newCredits
    ) throws SQLException {

        if (playerName == null ||
                playerName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Player name cannot be empty."
            );
        }

        if (newCredits < 0) {

            throw new IllegalArgumentException(
                    "Credits cannot be negative."
            );
        }

        String sql =
                "UPDATE players "
                        + "SET credits = ? "
                        + "WHERE player_name = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    newCredits
            );

            statement.setString(
                    2,
                    playerName.trim()
            );

            int rowsUpdated =
                    statement.executeUpdate();

            if (rowsUpdated != 1) {

                throw new SQLException(
                        "Player credits were not updated."
                );
            }
        }
    }


    // =========================================================
    // UPDATE CREDITS + TRANSACTION
    // =========================================================

    public void updateCreditsWithTransaction(
            String playerName,
            int newCredits
    ) throws SQLException {

        if (playerName == null ||
                playerName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Player name cannot be empty."
            );
        }

        if (newCredits < 0) {

            throw new IllegalArgumentException(
                    "Credits cannot be negative."
            );
        }

        playerName = playerName.trim();

        Connection connection = null;

        try {

            connection =
                    DatabaseConnection.getConnection();

            connection.setAutoCommit(false);

            // -------------------------------------------------
            // Get latest balance and lock player
            // -------------------------------------------------

            int oldCredits;

            String selectSql =
                    "SELECT credits "
                            + "FROM players "
                            + "WHERE player_name = ? "
                            + "FOR UPDATE";

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    selectSql
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

                    oldCredits =
                            resultSet.getInt(
                                    "credits"
                            );
                }
            }

            // -------------------------------------------------
            // Update balance
            // -------------------------------------------------

            String updateSql =
                    "UPDATE players "
                            + "SET credits = ? "
                            + "WHERE player_name = ?";

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    updateSql
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
                            "Player credits were not updated."
                    );
                }
            }

            // -------------------------------------------------
            // Calculate transaction amount
            // -------------------------------------------------

            int transactionAmount =
                    newCredits - oldCredits;

            String transactionType;

            if (transactionAmount >= 0) {

                transactionType =
                        "ADMIN_CREDIT";

            } else {

                transactionType =
                        "ADMIN_DEBIT";
            }

            // -------------------------------------------------
            // Save wallet transaction
            // -------------------------------------------------

            String transactionSql =
                    "INSERT INTO wallet_transactions "
                            + "(player_name, "
                            + "transaction_type, "
                            + "amount, "
                            + "balance_before, "
                            + "balance_after, "
                            + "description) "
                            + "VALUES (?, ?, ?, ?, ?, ?)";

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
                        oldCredits
                );

                statement.setInt(
                        5,
                        newCredits
                );

                statement.setString(
                        6,
                        "Credits changed by administrator"
                );

                int rowsInserted =
                        statement.executeUpdate();

                if (rowsInserted != 1) {

                    throw new SQLException(
                            "Wallet transaction could not be created."
                    );
                }
            }

            // -------------------------------------------------
            // Commit
            // -------------------------------------------------

            connection.commit();

            System.out.println(
                    "Credits updated successfully."
            );

            System.out.println(
                    "Player: " + playerName
            );

            System.out.println(
                    "Old Credits: " + oldCredits
            );

            System.out.println(
                    "New Credits: " + newCredits
            );

            System.out.println(
                    "Transaction Amount: "
                            + transactionAmount
            );

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

                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }


    // =========================================================
    // RESET CREDITS
    // =========================================================

    public void resetCredits(
            String playerName
    ) throws SQLException {

        updateCredits(
                playerName,
                1000
        );
    }


    // =========================================================
    // RESET CREDITS + TRANSACTION
    // =========================================================

    public void resetCreditsWithTransaction(
            String playerName
    ) throws SQLException {

        if (playerName == null ||
                playerName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Player name cannot be empty."
            );
        }

        playerName = playerName.trim();

        Connection connection = null;

        try {

            connection =
                    DatabaseConnection.getConnection();

            connection.setAutoCommit(false);

            // -------------------------------------------------
            // Get current balance and lock player
            // -------------------------------------------------

            int oldCredits;

            String selectSql =
                    "SELECT credits "
                            + "FROM players "
                            + "WHERE player_name = ? "
                            + "FOR UPDATE";

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    selectSql
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

                    oldCredits =
                            resultSet.getInt(
                                    "credits"
                            );
                }
            }

            // -------------------------------------------------
            // Reset balance
            // -------------------------------------------------

            int newCredits = 1000;

            String updateSql =
                    "UPDATE players "
                            + "SET credits = ? "
                            + "WHERE player_name = ?";

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    updateSql
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
                            "Player wallet could not be reset."
                    );
                }
            }

            // -------------------------------------------------
            // Wallet transaction
            // -------------------------------------------------

            int amount =
                    newCredits - oldCredits;

            String transactionSql =
                    "INSERT INTO wallet_transactions "
                            + "(player_name, "
                            + "transaction_type, "
                            + "amount, "
                            + "balance_before, "
                            + "balance_after, "
                            + "description) "
                            + "VALUES (?, ?, ?, ?, ?, ?)";

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
                        "WALLET_RESET"
                );

                statement.setInt(
                        3,
                        amount
                );

                statement.setInt(
                        4,
                        oldCredits
                );

                statement.setInt(
                        5,
                        newCredits
                );

                statement.setString(
                        6,
                        "Wallet reset by administrator"
                );

                int rowsInserted =
                        statement.executeUpdate();

                if (rowsInserted != 1) {

                    throw new SQLException(
                            "Wallet reset transaction could not be created."
                    );
                }
            }

            connection.commit();

            System.out.println(
                    "Wallet reset successfully."
            );

            System.out.println(
                    "Player: " + playerName
            );

            System.out.println(
                    "Old Credits: " + oldCredits
            );

            System.out.println(
                    "New Credits: " + newCredits
            );

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

                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }



    // =========================================================
    // ADMIN RESET PLAYER PASSWORD
    // =========================================================

    public void resetPlayerPassword(
            String playerName,
            String newPassword
    ) throws SQLException {

        if (playerName == null || playerName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Player name cannot be empty."
            );
        }

        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException(
                    "New password cannot be empty."
            );
        }

        if (!CredentialRules.isValidPassword(newPassword)) {
            throw new IllegalArgumentException(
                    CredentialRules.PASSWORD_REQUIREMENTS
            );
        }

        // Reuse PlayerDao's secure PBKDF2 password reset implementation.
        // The plain-text password is never stored in PostgreSQL.
        new PlayerDao().resetPlayerPassword(
                playerName.trim(),
                newPassword
        );

        System.out.println(
                "Player password reset successfully by administrator: "
                        + playerName.trim()
        );
    }

    // =========================================================
    // DELETE PLAYER
    // =========================================================

    public void deletePlayer(
            String playerName
    ) throws SQLException {

        if (playerName == null ||
                playerName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Player name cannot be empty."
            );
        }

        playerName = playerName.trim();

        Connection connection = null;

        try {

            connection =
                    DatabaseConnection.getConnection();

            connection.setAutoCommit(false);

            // -------------------------------------------------
            // Check player
            // -------------------------------------------------

            String checkSql =
                    "SELECT player_name "
                            + "FROM players "
                            + "WHERE player_name = ? "
                            + "FOR UPDATE";

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    checkSql
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
                }
            }

            // -------------------------------------------------
            // Delete game history
            // -------------------------------------------------

            String deleteGameHistorySql =
                    "DELETE FROM game_history "
                            + "WHERE player_name = ?";

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    deleteGameHistorySql
                            )
            ) {

                statement.setString(
                        1,
                        playerName
                );

                int deletedGames =
                        statement.executeUpdate();

                System.out.println(
                        "Deleted game history records: "
                                + deletedGames
                );
            }

            // -------------------------------------------------
            // Delete wallet transactions
            // -------------------------------------------------

            String deleteWalletSql =
                    "DELETE FROM wallet_transactions "
                            + "WHERE player_name = ?";

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    deleteWalletSql
                            )
            ) {

                statement.setString(
                        1,
                        playerName
                );

                int deletedTransactions =
                        statement.executeUpdate();

                System.out.println(
                        "Deleted wallet transactions: "
                                + deletedTransactions
                );
            }

            // -------------------------------------------------
            // Delete player
            // -------------------------------------------------

            String deletePlayerSql =
                    "DELETE FROM players "
                            + "WHERE player_name = ?";

            int deletedPlayer;

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    deletePlayerSql
                            )
            ) {

                statement.setString(
                        1,
                        playerName
                );

                deletedPlayer =
                        statement.executeUpdate();
            }

            if (deletedPlayer != 1) {

                throw new SQLException(
                        "Player account could not be deleted."
                );
            }

            connection.commit();

            System.out.println(
                    "Player deleted successfully: "
                            + playerName
            );

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

                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }


    // =========================================================
    // TOTAL PLAYERS
    // =========================================================

    public int getTotalPlayers()
            throws SQLException {

        String sql =
                "SELECT COUNT(*) FROM players";

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


    // =========================================================
    // TOTAL GAMES
    // =========================================================

    public int getTotalGames()
            throws SQLException {

        String sql =
                "SELECT COUNT(*) FROM game_history";

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


    // =========================================================
    // TOTAL WINS
    // =========================================================

    public int getTotalWins()
            throws SQLException {

        String sql =
                "SELECT COUNT(*) "
                        + "FROM game_history "
                        + "WHERE status = 'WON'";

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


    // =========================================================
    // TOTAL LOSSES
    // =========================================================

    public int getTotalLosses()
            throws SQLException {

        String sql =
                "SELECT COUNT(*) "
                        + "FROM game_history "
                        + "WHERE status = 'LOST'";

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


    // =========================================================
    // TOTAL BET
    // =========================================================

    public int getTotalBet()
            throws SQLException {

        String sql =
                "SELECT COALESCE("
                        + "SUM(bet_amount), 0"
                        + ") "
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

                return resultSet.getInt(1);
            }
        }

        return 0;
    }


    // =========================================================
    // TOTAL PAYOUT
    // =========================================================

    public int getTotalPayout()
            throws SQLException {

        String sql =
                "SELECT COALESCE("
                        + "SUM(payout), 0"
                        + ") "
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

                return resultSet.getInt(1);
            }
        }

        return 0;
    }


    // =========================================================
    // TOTAL PROFIT / LOSS
    // =========================================================

    public int getTotalProfitLoss()
            throws SQLException {

        String sql =
                "SELECT COALESCE("
                        + "SUM(payout - bet_amount), 0"
                        + ") "
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

                return resultSet.getInt(1);
            }
        }

        return 0;
    }


    // =========================================================
    // GET PLAYER HISTORY
    // =========================================================

    public List<Object[]> getPlayerHistory(
            String playerName
    ) throws SQLException {

        List<Object[]> history =
                new ArrayList<>();

        String sql =
                "SELECT game_id, "
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
                        + "ORDER BY game_id DESC";

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
                            ),

                            resultSet.getString(
                                    "selected_value"
                            ),

                            resultSet.getInt(
                                    "result_number"
                            ),

                            resultSet.getString(
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


    // =========================================================
    // PLAYER EXISTS
    // =========================================================

    public boolean playerExists(
            String playerName
    ) throws SQLException {

        if (playerName == null ||
                playerName.trim().isEmpty()) {

            return false;
        }

        String sql =
                "SELECT 1 "
                        + "FROM players "
                        + "WHERE player_name = ?";

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

                return resultSet.next();
            }
        }
    }
}