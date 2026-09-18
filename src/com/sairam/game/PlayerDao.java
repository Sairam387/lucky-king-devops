package com.sairam.game;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PlayerDao {

    // =========================================================
    // GAME SETTINGS
    // =========================================================

    private static final int STARTING_CREDITS = 1000;

    // =========================================================
    // PASSWORD HASHING SETTINGS
    // =========================================================

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;

    // =========================================================
    // REGISTER NEW PLAYER
    // =========================================================

    public Player registerPlayer(
            String playerName,
            String password,
            String confirmPassword) throws SQLException {

        validateUsername(playerName);
        validatePassword(password);

        if (confirmPassword == null ||
                !password.equals(confirmPassword)) {

            throw new SQLException(
                    "Password and Confirm Password do not match."
            );
        }

        playerName = playerName.trim();

        // -----------------------------------------------------
        // CHECK USERNAME EXISTS
        // -----------------------------------------------------

        String checkSql =
                "SELECT player_id " +
                "FROM players " +
                "WHERE LOWER(player_name) = LOWER(?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(checkSql)) {

            statement.setString(1, playerName);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    throw new SQLException(
                            "Username already exists."
                    );
                }
            }
        }

        // -----------------------------------------------------
        // HASH PASSWORD
        // -----------------------------------------------------

        String passwordHash =
                hashPassword(password);

        // -----------------------------------------------------
        // INSERT PLAYER
        // -----------------------------------------------------

        String insertSql =
                "INSERT INTO players " +
                "(player_name, credits, password_hash) " +
                "VALUES (?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(insertSql)) {

            statement.setString(1, playerName);
            statement.setInt(2, STARTING_CREDITS);
            statement.setString(3, passwordHash);

            int rowsInserted =
                    statement.executeUpdate();

            if (rowsInserted != 1) {

                throw new SQLException(
                        "Player registration failed."
                );
            }
        }

        return new Player(
                playerName,
                STARTING_CREDITS
        );
    }

    // =========================================================
    // LOGIN EXISTING PLAYER
    // =========================================================

    public Player loginPlayer(
            String playerName,
            String password) throws SQLException {

        validateUsername(playerName);
        validatePassword(password);

        playerName = playerName.trim();

        String sql =
                "SELECT player_name, credits, password_hash " +
                "FROM players " +
                "WHERE LOWER(player_name) = LOWER(?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, playerName);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (!resultSet.next()) {

                    throw new SQLException(
                            "Username does not exist."
                    );
                }

                String databaseName =
                        resultSet.getString("player_name");

                int credits =
                        resultSet.getInt("credits");

                String storedHash =
                        resultSet.getString("password_hash");

                // -------------------------------------------------
                // PASSWORD NOT CONFIGURED
                // -------------------------------------------------

                if (storedHash == null ||
                        storedHash.trim().isEmpty()) {

                    throw new SQLException(
                            "This account does not have a password. "
                            + "Please contact the administrator."
                    );
                }

                // -------------------------------------------------
                // VERIFY PASSWORD
                // -------------------------------------------------

                if (!verifyPassword(
                        password,
                        storedHash)) {

                    throw new SQLException(
                            "Incorrect password."
                    );
                }

                return new Player(
                        databaseName,
                        credits
                );
            }
        }
    }

    // =========================================================
    // AUTHENTICATE PLAYER
    // =========================================================
    //
    // Compatibility method for LoginUi.
    //
    // LoginUi calls:
    //
    // authenticatePlayer(username, password)
    //
    // Your existing DAO already had:
    //
    // loginPlayer(username, password)
    //
    // Therefore this method simply uses the existing secure
    // login implementation.
    // =========================================================

    public Player authenticatePlayer(
            String playerName,
            String password) throws SQLException {

        return loginPlayer(
                playerName,
                password
        );
    }

    // =========================================================
    // GET OR CREATE PLAYER
    // =========================================================

    public Player getOrCreatePlayer(
            String playerName) throws SQLException {

        validateUsername(playerName);

        playerName = playerName.trim();

        String selectSql =
                "SELECT player_name, credits " +
                "FROM players " +
                "WHERE LOWER(player_name) = LOWER(?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(selectSql)) {

            statement.setString(1, playerName);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return new Player(
                            resultSet.getString("player_name"),
                            resultSet.getInt("credits")
                    );
                }
            }
        }

        String insertSql =
                "INSERT INTO players " +
                "(player_name, credits) " +
                "VALUES (?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(insertSql)) {

            statement.setString(1, playerName);
            statement.setInt(2, STARTING_CREDITS);

            statement.executeUpdate();
        }

        return new Player(
                playerName,
                STARTING_CREDITS
        );
    }

    // =========================================================
    // GET PLAYER
    // =========================================================

    public Player getPlayer(
            String playerName) throws SQLException {

        if (playerName == null ||
                playerName.trim().isEmpty()) {

            return null;
        }

        String sql =
                "SELECT player_name, credits " +
                "FROM players " +
                "WHERE LOWER(player_name) = LOWER(?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    playerName.trim()
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return new Player(
                            resultSet.getString("player_name"),
                            resultSet.getInt("credits")
                    );
                }
            }
        }

        return null;
    }

    // =========================================================
    // GET CREDITS
    // =========================================================

    public int getCredits(
            String playerName) throws SQLException {

        if (playerName == null ||
                playerName.trim().isEmpty()) {

            throw new SQLException(
                    "Player name is required."
            );
        }

        String sql =
                "SELECT credits " +
                "FROM players " +
                "WHERE LOWER(player_name) = LOWER(?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    playerName.trim()
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (!resultSet.next()) {

                    throw new SQLException(
                            "Player account not found: "
                            + playerName
                    );
                }

                return resultSet.getInt("credits");
            }
        }
    }

    // =========================================================
    // GET PLAYER CREDITS
    // =========================================================

    public int getPlayerCredits(
            String playerName) throws SQLException {

        return getCredits(playerName);
    }

    // =========================================================
    // UPDATE CREDITS
    // =========================================================

    public void updateCredits(
            String playerName,
            int credits) throws SQLException {

        if (playerName == null ||
                playerName.trim().isEmpty()) {

            throw new SQLException(
                    "Player name is required."
            );
        }

        if (credits < 0) {

            throw new SQLException(
                    "Credits cannot be negative."
            );
        }

        String sql =
                "UPDATE players " +
                "SET credits = ? " +
                "WHERE LOWER(player_name) = LOWER(?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, credits);

            statement.setString(
                    2,
                    playerName.trim()
            );

            int rowsUpdated =
                    statement.executeUpdate();

            if (rowsUpdated != 1) {

                throw new SQLException(
                        "Wallet update failed."
                );
            }
        }
    }

    // =========================================================
    // UPDATE PASSWORD
    // =========================================================

    public void updatePassword(
            String playerName,
            String newPassword) throws SQLException {

        if (playerName == null ||
                playerName.trim().isEmpty()) {

            throw new SQLException(
                    "Player name is required."
            );
        }

        validatePassword(newPassword);

        String passwordHash =
                hashPassword(newPassword);

        String sql =
                "UPDATE players " +
                "SET password_hash = ? " +
                "WHERE LOWER(player_name) = LOWER(?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    passwordHash
            );

            statement.setString(
                    2,
                    playerName.trim()
            );

            int rowsUpdated =
                    statement.executeUpdate();

            if (rowsUpdated != 1) {

                throw new SQLException(
                        "Password update failed."
                );
            }
        }
    }

    // =========================================================
    // RESET PASSWORD
    // =========================================================

    public void resetPlayerPassword(
            String playerName,
            String newPassword) throws SQLException {

        if (playerName == null ||
                playerName.trim().isEmpty()) {

            throw new SQLException(
                    "Player name is required."
            );
        }

        validatePassword(newPassword);

        playerName = playerName.trim();

        // -----------------------------------------------------
        // CHECK PLAYER EXISTS
        // -----------------------------------------------------

        String checkSql =
                "SELECT player_id " +
                "FROM players " +
                "WHERE LOWER(player_name) = LOWER(?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(checkSql)) {

            statement.setString(
                    1,
                    playerName
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (!resultSet.next()) {

                    throw new SQLException(
                            "Player account not found: "
                            + playerName
                    );
                }
            }
        }

        // -----------------------------------------------------
        // HASH NEW PASSWORD
        // -----------------------------------------------------

        String passwordHash =
                hashPassword(newPassword);

        // -----------------------------------------------------
        // UPDATE PASSWORD
        // -----------------------------------------------------

        String updateSql =
                "UPDATE players " +
                "SET password_hash = ? " +
                "WHERE LOWER(player_name) = LOWER(?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(updateSql)) {

            statement.setString(
                    1,
                    passwordHash
            );

            statement.setString(
                    2,
                    playerName
            );

            int rowsUpdated =
                    statement.executeUpdate();

            if (rowsUpdated != 1) {

                throw new SQLException(
                        "Password reset failed."
                );
            }
        }
    }

    // =========================================================
    // VALIDATE PASSWORD
    // =========================================================

    public boolean isValidPassword(
            String password) {

        if (password == null) {
            return false;
        }

        return CredentialRules.isValidPassword(
                password
        );
    }

    // =========================================================
    // CHANGE PLAYER PASSWORD
    // =========================================================

    public void changePlayerPassword(
            String playerName,
            String oldPassword,
            String newPassword) throws SQLException {

        if (playerName == null ||
                playerName.trim().isEmpty()) {

            throw new SQLException(
                    "Player name is required."
            );
        }

        if (oldPassword == null ||
                oldPassword.isEmpty()) {

            throw new SQLException(
                    "Current password cannot be empty."
            );
        }

        validatePassword(newPassword);

        String sql =
                "SELECT password_hash " +
                "FROM players " +
                "WHERE LOWER(player_name) = LOWER(?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    playerName.trim()
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (!resultSet.next()) {

                    throw new SQLException(
                            "Player account not found."
                    );
                }

                String storedHash =
                        resultSet.getString(
                                "password_hash"
                        );

                if (storedHash == null ||
                        storedHash.trim().isEmpty()) {

                    throw new SQLException(
                            "Current password is not configured."
                    );
                }

                if (!verifyPassword(
                        oldPassword,
                        storedHash)) {

                    throw new SQLException(
                            "Current password is incorrect."
                    );
                }
            }
        }

        updatePassword(
                playerName,
                newPassword
        );
    }

    // =========================================================
    // USERNAME VALIDATION
    // =========================================================

    private void validateUsername(
            String username) throws SQLException {

        if (username == null ||
                username.trim().isEmpty()) {

            throw new SQLException(
                    "Username cannot be empty."
            );
        }

        if (!CredentialRules.isValidUsername(
                username)) {

            throw new SQLException(
                    CredentialRules.USERNAME_REQUIREMENTS
            );
        }

        if (!username.equals(
                username.trim())) {

            throw new SQLException(
                    "Username cannot start or end with spaces."
            );
        }
    }

    // =========================================================
    // PASSWORD VALIDATION
    // =========================================================

    private void validatePassword(
            String password) throws SQLException {

        if (password == null ||
                password.isEmpty()) {

            throw new SQLException(
                    "Password cannot be empty."
            );
        }

        if (!CredentialRules.isValidPassword(
                password)) {

            throw new SQLException(
                    CredentialRules.PASSWORD_REQUIREMENTS
            );
        }
    }

    // =========================================================
    // PASSWORD HASHING
    // =========================================================

    private String hashPassword(
            String password) throws SQLException {

        try {

            SecureRandom random =
                    new SecureRandom();

            byte[] salt =
                    new byte[SALT_LENGTH];

            random.nextBytes(salt);

            PBEKeySpec spec =
                    new PBEKeySpec(
                            password.toCharArray(),
                            salt,
                            ITERATIONS,
                            KEY_LENGTH
                    );

            try {

                SecretKeyFactory factory =
                        SecretKeyFactory.getInstance(
                                "PBKDF2WithHmacSHA256"
                        );

                byte[] hash =
                        factory.generateSecret(
                                spec
                        ).getEncoded();

                return ITERATIONS
                        + ":"
                        + Base64.getEncoder()
                                .encodeToString(salt)
                        + ":"
                        + Base64.getEncoder()
                                .encodeToString(hash);

            } finally {

                spec.clearPassword();
            }

        } catch (Exception e) {

            throw new SQLException(
                    "Password hashing failed.",
                    e
            );
        }
    }

    // =========================================================
    // VERIFY PASSWORD
    // =========================================================

    private boolean verifyPassword(
            String password,
            String storedPassword) throws SQLException {

        try {

            if (password == null ||
                    storedPassword == null ||
                    storedPassword.trim().isEmpty()) {

                return false;
            }

            String[] parts =
                    storedPassword.split(":");

            if (parts.length != 3) {
                return false;
            }

            int iterations =
                    Integer.parseInt(parts[0]);

            if (iterations <= 0) {
                return false;
            }

            byte[] salt =
                    Base64.getDecoder()
                            .decode(parts[1]);

            byte[] expectedHash =
                    Base64.getDecoder()
                            .decode(parts[2]);

            if (salt.length == 0 ||
                    expectedHash.length == 0) {

                return false;
            }

            PBEKeySpec spec =
                    new PBEKeySpec(
                            password.toCharArray(),
                            salt,
                            iterations,
                            expectedHash.length * 8
                    );

            try {

                SecretKeyFactory factory =
                        SecretKeyFactory.getInstance(
                                "PBKDF2WithHmacSHA256"
                        );

                byte[] actualHash =
                        factory.generateSecret(
                                spec
                        ).getEncoded();

                return MessageDigest.isEqual(
                        expectedHash,
                        actualHash
                );

            } finally {

                spec.clearPassword();
            }

        } catch (Exception e) {

            throw new SQLException(
                    "Password verification failed.",
                    e
            );
        }
    }
}