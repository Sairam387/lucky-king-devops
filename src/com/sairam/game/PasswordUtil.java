package com.sairam.game;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtil {

    private static final String ALGORITHM =
            "PBKDF2WithHmacSHA256";

    private static final int ITERATIONS =
            600000;

    private static final int SALT_LENGTH =
            16;

    private static final int KEY_LENGTH =
            256;

    private static final String FORMAT =
            "PBKDF2";

    private PasswordUtil() {
    }

    public static String hashPassword(
            String password
    ) {

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException(
                    "Password cannot be empty."
            );
        }

        try {

            SecureRandom secureRandom =
                    new SecureRandom();

            byte[] salt =
                    new byte[SALT_LENGTH];

            secureRandom.nextBytes(salt);

            PBEKeySpec spec =
                    new PBEKeySpec(
                            password.toCharArray(),
                            salt,
                            ITERATIONS,
                            KEY_LENGTH
                    );

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(
                            ALGORITHM
                    );

            byte[] hash =
                    factory.generateSecret(
                            spec
                    ).getEncoded();

            spec.clearPassword();

            String encodedSalt =
                    Base64.getEncoder()
                            .encodeToString(salt);

            String encodedHash =
                    Base64.getEncoder()
                            .encodeToString(hash);

            return FORMAT
                    + "$"
                    + ITERATIONS
                    + "$"
                    + encodedSalt
                    + "$"
                    + encodedHash;

        } catch (
                NoSuchAlgorithmException
                | InvalidKeySpecException e
        ) {

            throw new IllegalStateException(
                    "Unable to hash password.",
                    e
            );
        }
    }

    public static boolean verifyPassword(
            String password,
            String storedPassword
    ) {

        if (password == null
                || storedPassword == null
                || storedPassword.isEmpty()) {

            return false;
        }

        try {

            String[] parts =
                    storedPassword.split(
                            "\\$"
                    );

            if (parts.length != 4) {

                return false;
            }

            if (!FORMAT.equals(parts[0])) {

                return false;
            }

            int iterations =
                    Integer.parseInt(
                            parts[1]
                    );

            byte[] salt =
                    Base64.getDecoder()
                            .decode(parts[2]);

            byte[] expectedHash =
                    Base64.getDecoder()
                            .decode(parts[3]);

            PBEKeySpec spec =
                    new PBEKeySpec(
                            password.toCharArray(),
                            salt,
                            iterations,
                            expectedHash.length * 8
                    );

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(
                            ALGORITHM
                    );

            byte[] actualHash =
                    factory.generateSecret(
                            spec
                    ).getEncoded();

            spec.clearPassword();

            return constantTimeEquals(
                    actualHash,
                    expectedHash
            );

        } catch (
                NoSuchAlgorithmException
                | InvalidKeySpecException
                | IllegalArgumentException e
        ) {

            return false;
        }
    }

    private static boolean constantTimeEquals(
            byte[] first,
            byte[] second
    ) {

        if (first == null || second == null) {
            return false;
        }

        if (first.length != second.length) {
            return false;
        }

        int result = 0;

        for (int i = 0; i < first.length; i++) {

            result |=
                    first[i] ^ second[i];
        }

        return result == 0;
    }
}