package com.sairam.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class AdminResetPlayerPasswordUi extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField playerNameField;

    private JPasswordField newPasswordField;

    private JPasswordField confirmPasswordField;

    private JButton resetButton;

    private JButton clearButton;

    private JButton closeButton;

    private AdminDao adminDao;

    private String adminUsername;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public AdminResetPlayerPasswordUi(
            String adminUsername
    ) {

        this.adminUsername =
                adminUsername;

        adminDao =
                new AdminDao();

        initializeUi();
    }

    // =========================================================
    // UI INITIALIZATION
    // =========================================================

    private void initializeUi() {

        setTitle(
                "Admin - Reset Player Password"
        );

        setSize(
                650,
                500
        );

        setMinimumSize(
                new Dimension(
                        600,
                        450
                )
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLayout(
                new BorderLayout()
        );

        createHeader();

        createForm();

        createBottomButtons();
    }

    // =========================================================
    // HEADER
    // =========================================================

    private void createHeader() {

        JPanel headerPanel =
                new JPanel(
                        new BorderLayout()
                );

        headerPanel.setBorder(
                new EmptyBorder(
                        20,
                        25,
                        20,
                        25
                )
        );

        JLabel titleLabel =
                new JLabel(
                        "RESET PLAYER PASSWORD"
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        titleLabel.setForeground(
                Color.WHITE
        );

        titleLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        headerPanel.setBackground(
                new Color(
                        35,
                        45,
                        65
                )
        );

        headerPanel.add(
                titleLabel,
                BorderLayout.CENTER
        );

        add(
                headerPanel,
                BorderLayout.NORTH
        );
    }

    // =========================================================
    // FORM
    // =========================================================

    private void createForm() {

        JPanel formPanel =
                new JPanel(
                        new GridBagLayout()
                );

        formPanel.setBorder(
                new EmptyBorder(
                        30,
                        50,
                        20,
                        50
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        10,
                        10,
                        10,
                        10
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1.0;

        // -----------------------------------------------------
        // PLAYER NAME
        // -----------------------------------------------------

        JLabel playerLabel =
                new JLabel(
                        "Player Name:"
                );

        playerLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;

        formPanel.add(
                playerLabel,
                gbc
        );

        playerNameField =
                new JTextField();

        playerNameField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        playerNameField.setPreferredSize(
                new Dimension(
                        300,
                        40
                )
        );

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        formPanel.add(
                playerNameField,
                gbc
        );

        // -----------------------------------------------------
        // NEW PASSWORD
        // -----------------------------------------------------

        JLabel newPasswordLabel =
                new JLabel(
                        "New Password:"
                );

        newPasswordLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;

        formPanel.add(
                newPasswordLabel,
                gbc
        );

        newPasswordField =
                new JPasswordField();

        newPasswordField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        newPasswordField.setPreferredSize(
                new Dimension(
                        300,
                        40
                )
        );

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;

        formPanel.add(
                newPasswordField,
                gbc
        );

        // -----------------------------------------------------
        // CONFIRM PASSWORD
        // -----------------------------------------------------

        JLabel confirmLabel =
                new JLabel(
                        "Confirm Password:"
                );

        confirmLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;

        formPanel.add(
                confirmLabel,
                gbc
        );

        confirmPasswordField =
                new JPasswordField();

        confirmPasswordField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        confirmPasswordField.setPreferredSize(
                new Dimension(
                        300,
                        40
                )
        );

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;

        formPanel.add(
                confirmPasswordField,
                gbc
        );

        // -----------------------------------------------------
        // SECURITY INFORMATION
        // -----------------------------------------------------

        JLabel securityLabel =
                new JLabel(
                        "<html>"
                        + "<b>Security:</b> "
                        + "The new password will be securely "
                        + "stored using PBKDF2 hashing."
                        + "<br>"
                        + "Minimum password length: 6 characters."
                        + "</html>"
                );

        securityLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        securityLabel.setForeground(
                new Color(
                        60,
                        70,
                        80
                )
        );

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;

        gbc.insets =
                new Insets(
                        20,
                        10,
                        10,
                        10
                );

        formPanel.add(
                securityLabel,
                gbc
        );

        // -----------------------------------------------------
        // ADMIN INFORMATION
        // -----------------------------------------------------

        JLabel adminLabel =
                new JLabel(
                        "Administrator: "
                                + (
                                adminUsername == null
                                || adminUsername.trim().isEmpty()
                                ? "admin"
                                : adminUsername
                        )
                );

        adminLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        adminLabel.setForeground(
                new Color(
                        80,
                        80,
                        80
                )
        );

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;

        gbc.insets =
                new Insets(
                        10,
                        10,
                        10,
                        10
                );

        formPanel.add(
                adminLabel,
                gbc
        );

        add(
                formPanel,
                BorderLayout.CENTER
        );
    }

    // =========================================================
    // BOTTOM BUTTONS
    // =========================================================

    private void createBottomButtons() {

        JPanel buttonPanel =
                new JPanel();

        buttonPanel.setBorder(
                new EmptyBorder(
                        15,
                        25,
                        20,
                        25
                )
        );

        resetButton =
                new JButton(
                        "RESET PASSWORD"
                );

        clearButton =
                new JButton(
                        "CLEAR"
                );

        closeButton =
                new JButton(
                        "CLOSE"
                );

        styleButton(
                resetButton,
                new Color(
                        35,
                        120,
                        70
                )
        );

        styleButton(
                clearButton,
                new Color(
                        80,
                        90,
                        105
                )
        );

        styleButton(
                closeButton,
                new Color(
                        150,
                        55,
                        55
                )
        );

        buttonPanel.add(
                resetButton
        );

        buttonPanel.add(
                clearButton
        );

        buttonPanel.add(
                closeButton
        );

        add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        // -----------------------------------------------------
        // ACTION LISTENERS
        // -----------------------------------------------------

        resetButton.addActionListener(
                e -> resetPassword()
        );

        clearButton.addActionListener(
                e -> clearFields()
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        getRootPane().setDefaultButton(
                resetButton
        );
    }

    // =========================================================
    // BUTTON STYLE
    // =========================================================

    private void styleButton(
            JButton button,
            Color background
    ) {

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(
                Color.WHITE
        );

        button.setBackground(
                background
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        18,
                        10,
                        18
                )
        );
    }

    // =========================================================
    // RESET PASSWORD
    // =========================================================

    private void resetPassword() {

        String playerName =
                playerNameField
                        .getText()
                        .trim();

        String newPassword =
                new String(
                        newPasswordField
                                .getPassword()
                );

        String confirmPassword =
                new String(
                        confirmPasswordField
                                .getPassword()
                );

        // -----------------------------------------------------
        // PLAYER NAME VALIDATION
        // -----------------------------------------------------

        if (playerName.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter the player name.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            playerNameField.requestFocus();

            return;
        }

        // -----------------------------------------------------
        // PASSWORD VALIDATION
        // -----------------------------------------------------

        if (newPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a new password.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            newPasswordField.requestFocus();

            return;
        }

        if (newPassword.length() < 6) {

            JOptionPane.showMessageDialog(
                    this,
                    "Password must contain at least 6 characters.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            newPasswordField.requestFocus();

            return;
        }

        // -----------------------------------------------------
        // CONFIRM PASSWORD
        // -----------------------------------------------------

        if (confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please confirm the new password.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            confirmPasswordField.requestFocus();

            return;
        }

        if (!newPassword.equals(
                confirmPassword
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    "New password and confirm password do not match.",
                    "Password Error",
                    JOptionPane.ERROR_MESSAGE
            );

            confirmPasswordField.requestFocus();

            return;
        }

        // -----------------------------------------------------
        // CHECK PLAYER
        // -----------------------------------------------------

        try {

            if (!adminDao.playerExists(
                    playerName
            )) {

                JOptionPane.showMessageDialog(
                        this,
                        "Player not found: "
                                + playerName,
                        "Player Not Found",
                        JOptionPane.ERROR_MESSAGE
                );

                playerNameField.requestFocus();

                return;
            }

            // -------------------------------------------------
            // CONFIRM RESET
            // -------------------------------------------------

            int confirmation =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Reset password for player '"
                                    + playerName
                                    + "'?",
                            "Confirm Password Reset",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

            if (confirmation
                    != JOptionPane.YES_OPTION) {

                return;
            }

            // -------------------------------------------------
            // RESET PASSWORD
            // -------------------------------------------------

            adminDao.resetPlayerPassword(
                    playerName,
                    newPassword
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Player password reset successfully.\n\n"
                            + "Player: "
                            + playerName
                            + "\n\n"
                            + "The new password is securely "
                            + "stored using PBKDF2.",
                    "Password Reset Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearFields();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to reset player password.\n\n"
                            + e.getMessage(),
                    "Password Reset Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // CLEAR FIELDS
    // =========================================================

    private void clearFields() {

        playerNameField.setText(
                ""
        );

        newPasswordField.setText(
                ""
        );

        confirmPasswordField.setText(
                ""
        );

        playerNameField.requestFocus();
    }

    // =========================================================
    // MAIN - TESTING
    // =========================================================

    public static void main(
            String[] args
    ) {

        javax.swing.SwingUtilities.invokeLater(
                () -> {

                    AdminResetPlayerPasswordUi ui =
                            new AdminResetPlayerPasswordUi(
                                    "admin"
                            );

                    ui.setVisible(
                            true
                    );
                }
        );
    }
}