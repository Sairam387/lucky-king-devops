package com.sairam.game;

/**
 * ============================================================
 * LUCKY KING - CREDENTIAL RULES
 * ============================================================
 *
 * Centralized validation rules for usernames and passwords.
 *
 * Username:
 *   - 3 to 20 characters
 *   - A-Z / a-z
 *   - 0-9
 *   - _ @ # $ % &
 *   - No spaces
 *
 * Password:
 *   - 6 to 30 characters
 *   - At least one letter
 *   - At least one number
 *   - No spaces
 *
 * ============================================================
 */
public final class CredentialRules {

    private CredentialRules() {
        // Prevent object creation
    }

    // =========================================================
    // USERNAME RULES
    // =========================================================

    public static final int USERNAME_MIN_LENGTH = 3;
    public static final int USERNAME_MAX_LENGTH = 20;

    public static final String USERNAME_REQUIREMENTS =
            "Username must be 3–20 characters.\n"
            + "Allowed characters:\n"
            + "• Letters (A-Z, a-z)\n"
            + "• Numbers (0-9)\n"
            + "• _ @ # $ % &\n"
            + "Spaces are not allowed.";

    // =========================================================
    // PASSWORD RULES
    // =========================================================

    public static final int PASSWORD_MIN_LENGTH = 6;
    public static final int PASSWORD_MAX_LENGTH = 30;

    public static final String PASSWORD_REQUIREMENTS =
            "Password must meet the following requirements:\n"
            + "• Minimum 6 characters\n"
            + "• Maximum 30 characters\n"
            + "• At least one letter\n"
            + "• At least one number\n"
            + "• Spaces are not allowed.";

    // =========================================================
    // USERNAME VALIDATION
    // =========================================================

    public static boolean isValidUsername(String username) {

        if (username == null) {
            return false;
        }

        username = username.trim();

        if (username.length() < USERNAME_MIN_LENGTH
                || username.length() > USERNAME_MAX_LENGTH) {
            return false;
        }

        return username.matches(
                "^[A-Za-z0-9_@#$%&]+$"
        );
    }

    // =========================================================
    // PASSWORD VALIDATION
    // =========================================================

    public static boolean isValidPassword(String password) {

        if (password == null) {
            return false;
        }

        // Length
        if (password.length() < PASSWORD_MIN_LENGTH
                || password.length() > PASSWORD_MAX_LENGTH) {
            return false;
        }

        // Spaces are not allowed
        if (password.contains(" ")) {
            return false;
        }

        // At least one letter
        if (!password.matches(".*[A-Za-z].*")) {
            return false;
        }

        // At least one number
        if (!password.matches(".*[0-9].*")) {
            return false;
        }

        return true;
    }
}