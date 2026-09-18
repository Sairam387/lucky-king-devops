package com.sairam.game;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

/**
 * ============================================================
 * LUCKY KING - PLAYER CHANGE PASSWORD UI
 * ============================================================
 *
 * Player self-service password change screen.
 *
 * Uses:
 *     PlayerDao.changePlayerPassword(
 *          playerName,
 *          oldPassword,
 *          newPassword
 *     );
 *
 * No database changes are required.
 *
 * ============================================================
 */
public class PlayerChangePasswordUi extends JFrame {

    private static final long serialVersionUID = 1L;

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color PURPLE = new Color(91, 63, 181);
    private static final Color PURPLE_DARK = new Color(66, 42, 145);
    private static final Color PURPLE_LIGHT = new Color(241, 237, 255);

    private static final Color GOLD = new Color(225, 174, 45);
    private static final Color GOLD_LIGHT = new Color(255, 248, 220);

    private static final Color WHITE = Color.WHITE;
    private static final Color BACKGROUND = new Color(247, 247, 252);

    private static final Color TEXT = new Color(45, 45, 60);
    private static final Color GREY_TEXT = new Color(120, 120, 135);
    private static final Color BORDER = new Color(220, 220, 232);

    private static final Color RED = new Color(210, 55, 55);
    private static final Color GREEN = new Color(35, 155, 80);

    // =========================================================
    // DAO
    // =========================================================

    private final PlayerDao playerDao;

    // =========================================================
    // PLAYER
    // =========================================================

    private final String playerName;

    // =========================================================
    // COMPONENTS
    // =========================================================

    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    private JLabel strengthLabel;

    private JLabel requirementLengthLabel;
    private JLabel requirementUppercaseLabel;
    private JLabel requirementLowercaseLabel;
    private JLabel requirementNumberLabel;
    private JLabel requirementSpecialLabel;

    private JButton changePasswordButton;
    private JButton cancelButton;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public PlayerChangePasswordUi(String playerName) {

        if (playerName == null || playerName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Player name cannot be empty."
            );
        }

        this.playerName = playerName.trim();
        this.playerDao = new PlayerDao();

        initializeWindow();
        buildUi();
    }

    // =========================================================
    // WINDOW
    // =========================================================

    private void initializeWindow() {

        setTitle("Lucky King - Change Password");

        setSize(520, 720);

        setMinimumSize(new Dimension(500, 680));

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setResizable(false);

        getContentPane().setBackground(BACKGROUND);
    }

    // =========================================================
    // BUILD UI
    // =========================================================

    private void buildUi() {

        JPanel root = new JPanel(new BorderLayout());

        root.setBackground(BACKGROUND);

        root.setBorder(
                new EmptyBorder(
                        18,
                        18,
                        18,
                        18
                )
        );

        // -----------------------------------------------------
        // HEADER
        // -----------------------------------------------------

        root.add(createHeader(), BorderLayout.NORTH);

        // -----------------------------------------------------
        // CENTER
        // -----------------------------------------------------

        root.add(createCenterPanel(), BorderLayout.CENTER);

        // -----------------------------------------------------
        // FOOTER
        // -----------------------------------------------------

        root.add(createFooter(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(PURPLE_DARK);

        header.setBorder(
                new EmptyBorder(
                        16,
                        18,
                        16,
                        18
                )
        );

        // -----------------------------------------------------
        // LEFT
        // -----------------------------------------------------

        JPanel left = new JPanel();

        left.setOpaque(false);

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title = new JLabel("LUCKY KING");

        title.setForeground(WHITE);

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        23
                )
        );

        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle =
                new JLabel("Change Your Password");

        subtitle.setForeground(
                new Color(
                        225,
                        220,
                        250
                )
        );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        subtitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        left.add(title);

        left.add(
                Box.createVerticalStrut(4)
        );

        left.add(subtitle);

        header.add(
                left,
                BorderLayout.WEST
        );

        // -----------------------------------------------------
        // USER
        // -----------------------------------------------------

        JPanel userPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                0,
                                0
                        )
                );

        userPanel.setOpaque(false);

        JLabel userLabel =
                new JLabel(
                        "Player: " + playerName
                );

        userLabel.setForeground(WHITE);

        userLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        userPanel.add(userLabel);

        header.add(
                userPanel,
                BorderLayout.EAST
        );

        return header;
    }

    // =========================================================
    // CENTER
    // =========================================================

    private JPanel createCenterPanel() {

        JPanel outer =
                new JPanel(
                        new BorderLayout()
                );

        outer.setBackground(BACKGROUND);

        outer.setBorder(
                new EmptyBorder(
                        15,
                        0,
                        15,
                        0
                )
        );

        JPanel card = new JPanel();

        card.setBackground(WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                BORDER,
                                1,
                                true
                        ),
                        new EmptyBorder(
                                18,
                                20,
                                18,
                                20
                        )
                )
        );

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        // -----------------------------------------------------
        // DESCRIPTION
        // -----------------------------------------------------

        JLabel description =
                new JLabel(
                        "Update your Lucky King account password."
                );

        description.setForeground(GREY_TEXT);

        description.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        description.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(description);

        card.add(
                Box.createVerticalStrut(15)
        );

        // -----------------------------------------------------
        // CURRENT PASSWORD
        // -----------------------------------------------------

        card.add(
                createFieldTitle(
                        "Current Password"
                )
        );

        card.add(
                Box.createVerticalStrut(6)
        );

        currentPasswordField =
                createPasswordField(
                        "Enter current password"
                );

        card.add(currentPasswordField);

        card.add(
                Box.createVerticalStrut(13)
        );

        // -----------------------------------------------------
        // NEW PASSWORD
        // -----------------------------------------------------

        card.add(
                createFieldTitle(
                        "New Password"
                )
        );

        card.add(
                Box.createVerticalStrut(6)
        );

        newPasswordField =
                createPasswordField(
                        "Enter new password"
                );

        card.add(newPasswordField);

        // -----------------------------------------------------
        // STRENGTH
        // -----------------------------------------------------

        strengthLabel =
                new JLabel(
                        "Password strength: —"
                );

        strengthLabel.setForeground(
                GREY_TEXT
        );

        strengthLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        strengthLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(
                Box.createVerticalStrut(7)
        );

        card.add(strengthLabel);

        card.add(
                Box.createVerticalStrut(13)
        );

        // -----------------------------------------------------
        // CONFIRM PASSWORD
        // -----------------------------------------------------

        card.add(
                createFieldTitle(
                        "Confirm New Password"
                )
        );

        card.add(
                Box.createVerticalStrut(6)
        );

        confirmPasswordField =
                createPasswordField(
                        "Re-enter new password"
                );

        card.add(confirmPasswordField);

        card.add(
                Box.createVerticalStrut(15)
        );

        // -----------------------------------------------------
        // REQUIREMENTS
        // -----------------------------------------------------

        card.add(
                createRequirementsPanel()
        );

        card.add(
                Box.createVerticalGlue()
        );

        outer.add(
                card,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // IMPORTANT:
        // Attach listener AFTER newPasswordField is assigned.
        // -----------------------------------------------------

        newPasswordField
                .getDocument()
                .addDocumentListener(
                        new DocumentListener() {

                            @Override
                            public void insertUpdate(
                                    DocumentEvent e) {

                                updatePasswordStrength();
                            }

                            @Override
                            public void removeUpdate(
                                    DocumentEvent e) {

                                updatePasswordStrength();
                            }

                            @Override
                            public void changedUpdate(
                                    DocumentEvent e) {

                                updatePasswordStrength();
                            }
                        }
                );

        return outer;
    }

    // =========================================================
    // FIELD TITLE
    // =========================================================

    private JLabel createFieldTitle(
            String text) {

        JLabel label =
                new JLabel(text);

        label.setForeground(TEXT);

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        return label;
    }

    // =========================================================
    // PASSWORD FIELD
    // =========================================================

    private JPasswordField createPasswordField(
            String tooltip) {

        JPasswordField field =
                new JPasswordField();

        field.setPreferredSize(
                new Dimension(
                        430,
                        42
                )
        );

        field.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        42
                )
        );

        field.setMinimumSize(
                new Dimension(
                        100,
                        42
                )
        );

        field.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        field.setForeground(TEXT);

        field.setBackground(
                new Color(
                        252,
                        252,
                        255
                )
        );

        field.setCaretColor(PURPLE);

        field.setToolTipText(tooltip);

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                BORDER,
                                1,
                                true
                        ),
                        new EmptyBorder(
                                7,
                                10,
                                7,
                                10
                        )
                )
        );

        // Focus effect
        field.addFocusListener(
                new java.awt.event.FocusAdapter() {

                    @Override
                    public void focusGained(
                            java.awt.event.FocusEvent e) {

                        field.setBorder(
                                BorderFactory.createCompoundBorder(
                                        new LineBorder(
                                                PURPLE,
                                                2,
                                                true
                                        ),
                                        new EmptyBorder(
                                                6,
                                                9,
                                                6,
                                                9
                                        )
                                )
                        );
                    }

                    @Override
                    public void focusLost(
                            java.awt.event.FocusEvent e) {

                        field.setBorder(
                                BorderFactory.createCompoundBorder(
                                        new LineBorder(
                                                BORDER,
                                                1,
                                                true
                                        ),
                                        new EmptyBorder(
                                                7,
                                                10,
                                                7,
                                                10
                                        )
                                )
                        );
                    }
                }
        );

        return field;
    }

    // =========================================================
    // REQUIREMENTS PANEL
    // =========================================================

    private JPanel createRequirementsPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.setBackground(
                new Color(
                        249,
                        249,
                        255
                )
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                new Color(
                                        225,
                                        225,
                                        240
                                ),
                                1,
                                true
                        ),
                        new EmptyBorder(
                                13,
                                15,
                                13,
                                15
                        )
                )
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        190
                )
        );

        // -----------------------------------------------------
        // TITLE
        // -----------------------------------------------------

        JLabel title =
                new JLabel(
                        "Password Requirements"
                );

        title.setForeground(
                PURPLE_DARK
        );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        title.setBorder(
                new EmptyBorder(
                        0,
                        0,
                        8,
                        0
                )
        );

        panel.add(
                title,
                BorderLayout.NORTH
        );

        // -----------------------------------------------------
        // REQUIREMENT ROWS
        // -----------------------------------------------------

        JPanel rows =
                new JPanel(
                        new GridLayout(
                                5,
                                1,
                                0,
                                4
                        )
                );

        rows.setOpaque(false);

        // -----------------------------------------------------
        // ROW 1
        // -----------------------------------------------------

        requirementLengthLabel =
                createRequirementLabel(
                        "At least 8 characters"
                );

        // -----------------------------------------------------
        // ROW 2
        // -----------------------------------------------------

        requirementUppercaseLabel =
                createRequirementLabel(
                        "At least one uppercase letter (A-Z)"
                );

        // -----------------------------------------------------
        // ROW 3
        // -----------------------------------------------------

        requirementLowercaseLabel =
                createRequirementLabel(
                        "At least one lowercase letter (a-z)"
                );

        // -----------------------------------------------------
        // ROW 4
        // -----------------------------------------------------

        requirementNumberLabel =
                createRequirementLabel(
                        "At least one number (0-9)"
                );

        // -----------------------------------------------------
        // ROW 5
        // -----------------------------------------------------

        requirementSpecialLabel =
                createRequirementLabel(
                        "At least one special character"
                );

        rows.add(
                requirementLengthLabel
        );

        rows.add(
                requirementUppercaseLabel
        );

        rows.add(
                requirementLowercaseLabel
        );

        rows.add(
                requirementNumberLabel
        );

        rows.add(
                requirementSpecialLabel
        );

        panel.add(
                rows,
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================================================
    // REQUIREMENT LABEL
    // =========================================================

    private JLabel createRequirementLabel(
            String text) {

        JLabel label =
                new JLabel(
                        "○  " + text
                );

        label.setForeground(
                GREY_TEXT
        );

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        label.setBorder(
                new EmptyBorder(
                        1,
                        2,
                        1,
                        2
                )
        );

        return label;
    }

    // =========================================================
    // UPDATE PASSWORD STRENGTH
    // =========================================================

    private void updatePasswordStrength() {

        if (newPasswordField == null) {
            return;
        }

        String password =
                new String(
                        newPasswordField.getPassword()
                );

        // -----------------------------------------------------
        // REQUIREMENTS
        // -----------------------------------------------------

        boolean lengthOK =
                password.length() >= 8;

        boolean uppercaseOK =
                password.matches(
                        ".*[A-Z].*"
                );

        boolean lowercaseOK =
                password.matches(
                        ".*[a-z].*"
                );

        boolean numberOK =
                password.matches(
                        ".*[0-9].*"
                );

        boolean specialOK =
                password.matches(
                        ".*[^a-zA-Z0-9].*"
                );

        // -----------------------------------------------------
        // UPDATE REQUIREMENT ROWS
        // -----------------------------------------------------

        updateRequirementLabel(
                requirementLengthLabel,
                "At least 8 characters",
                lengthOK
        );

        updateRequirementLabel(
                requirementUppercaseLabel,
                "At least one uppercase letter (A-Z)",
                uppercaseOK
        );

        updateRequirementLabel(
                requirementLowercaseLabel,
                "At least one lowercase letter (a-z)",
                lowercaseOK
        );

        updateRequirementLabel(
                requirementNumberLabel,
                "At least one number (0-9)",
                numberOK
        );

        updateRequirementLabel(
                requirementSpecialLabel,
                "At least one special character",
                specialOK
        );

        // -----------------------------------------------------
        // EMPTY
        // -----------------------------------------------------

        if (password.isEmpty()) {

            strengthLabel.setText(
                    "Password strength: —"
            );

            strengthLabel.setForeground(
                    GREY_TEXT
            );

            return;
        }

        // -----------------------------------------------------
        // SCORE
        // -----------------------------------------------------

        int score = 0;

        if (lengthOK) {
            score++;
        }

        if (uppercaseOK) {
            score++;
        }

        if (lowercaseOK) {
            score++;
        }

        if (numberOK) {
            score++;
        }

        if (specialOK) {
            score++;
        }

        // -----------------------------------------------------
        // STRENGTH
        // -----------------------------------------------------

        if (score <= 2) {

            strengthLabel.setText(
                    "Password strength: Weak"
            );

            strengthLabel.setForeground(
                    RED
            );

        } else if (score <= 4) {

            strengthLabel.setText(
                    "Password strength: Medium"
            );

            strengthLabel.setForeground(
                    GOLD
            );

        } else {

            strengthLabel.setText(
                    "Password strength: Strong"
            );

            strengthLabel.setForeground(
                    GREEN
            );
        }
    }

    // =========================================================
    // UPDATE REQUIREMENT LABEL
    // =========================================================

    private void updateRequirementLabel(
            JLabel label,
            String text,
            boolean satisfied) {

        if (label == null) {
            return;
        }

        if (satisfied) {

            label.setText(
                    "✓  " + text
            );

            label.setForeground(
                    GREEN
            );

        } else {

            label.setText(
                    "○  " + text
            );

            label.setForeground(
                    GREY_TEXT
            );
        }
    }

    // =========================================================
    // FOOTER
    // =========================================================

    private JPanel createFooter() {

        JPanel footer =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        footer.setBackground(
                BACKGROUND
        );

        // -----------------------------------------------------
        // CANCEL
        // -----------------------------------------------------

        cancelButton =
                new JButton(
                        "Cancel"
                );

        styleSecondaryButton(
                cancelButton
        );

        cancelButton.addActionListener(
                e -> dispose()
        );

        // -----------------------------------------------------
        // CHANGE PASSWORD
        // -----------------------------------------------------

        changePasswordButton =
                new JButton(
                        "Change Password"
                );

        stylePrimaryButton(
                changePasswordButton
        );

        changePasswordButton.addActionListener(
                this::handleChangePassword
        );

        footer.add(
                cancelButton
        );

        footer.add(
                changePasswordButton
        );

        return footer;
    }

    // =========================================================
    // PRIMARY BUTTON
    // =========================================================

    private void stylePrimaryButton(
            JButton button) {

        button.setForeground(WHITE);

        button.setBackground(PURPLE);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        button.setFocusPainted(false);

        button.setBorderPainted(false);

        button.setOpaque(true);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setPreferredSize(
                new Dimension(
                        150,
                        40
                )
        );

        button.setBorder(
                new EmptyBorder(
                        8,
                        14,
                        8,
                        14
                )
        );

        addButtonHoverEffect(
                button,
                PURPLE,
                PURPLE_DARK
        );
    }

    // =========================================================
    // SECONDARY BUTTON
    // =========================================================

    private void styleSecondaryButton(
            JButton button) {

        button.setForeground(
                TEXT
        );

        button.setBackground(
                WHITE
        );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        button.setFocusPainted(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setPreferredSize(
                new Dimension(
                        95,
                        40
                )
        );

        button.setBorder(
                new LineBorder(
                        BORDER,
                        1,
                        true
                )
        );

        addButtonHoverEffect(
                button,
                WHITE,
                new Color(
                        240,
                        240,
                        248
                )
        );
    }

    // =========================================================
    // BUTTON HOVER
    // =========================================================

    private void addButtonHoverEffect(
            JButton button,
            Color normal,
            Color hover) {

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e) {

                        button.setBackground(
                                hover
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e) {

                        button.setBackground(
                                normal
                        );
                    }
                }
        );
    }

    // =========================================================
    // CHANGE PASSWORD
    // =========================================================

    private void handleChangePassword(
            ActionEvent event) {

        char[] currentPassword =
                currentPasswordField.getPassword();

        char[] newPassword =
                newPasswordField.getPassword();

        char[] confirmPassword =
                confirmPasswordField.getPassword();

        try {

            // -------------------------------------------------
            // CURRENT PASSWORD
            // -------------------------------------------------

            if (currentPassword.length == 0) {

                showWarning(
                        "Please enter your current password."
                );

                currentPasswordField.requestFocusInWindow();

                return;
            }

            // -------------------------------------------------
            // NEW PASSWORD
            // -------------------------------------------------

            if (newPassword.length == 0) {

                showWarning(
                        "Please enter a new password."
                );

                newPasswordField.requestFocusInWindow();

                return;
            }

            // -------------------------------------------------
            // CONFIRM PASSWORD
            // -------------------------------------------------

            if (confirmPassword.length == 0) {

                showWarning(
                        "Please confirm your new password."
                );

                confirmPasswordField.requestFocusInWindow();

                return;
            }

            String current =
                    new String(
                            currentPassword
                    );

            String newPass =
                    new String(
                            newPassword
                    );

            String confirm =
                    new String(
                            confirmPassword
                    );

            // -------------------------------------------------
            // VALIDATE NEW PASSWORD
            // -------------------------------------------------

            if (!playerDao.isValidPassword(newPass)) {

                showWarning(
                        getPasswordRequirementMessage()
                );

                newPasswordField.requestFocusInWindow();

                return;
            }

            // -------------------------------------------------
            // CONFIRM MATCH
            // -------------------------------------------------

            if (!newPass.equals(confirm)) {

                showWarning(
                        "New password and confirmation password do not match."
                );

                confirmPasswordField.requestFocusInWindow();

                return;
            }

            // -------------------------------------------------
            // SAME PASSWORD
            // -------------------------------------------------

            if (current.equals(newPass)) {

                showWarning(
                        "New password must be different from your current password."
                );

                newPasswordField.requestFocusInWindow();

                return;
            }

            // -------------------------------------------------
            // BUTTON STATE
            // -------------------------------------------------

            changePasswordButton.setEnabled(false);

            changePasswordButton.setText(
                    "Updating..."
            );

            // -------------------------------------------------
            // DAO
            // -------------------------------------------------

            playerDao.changePlayerPassword(
                    playerName,
                    current,
                    newPass
            );

            // -------------------------------------------------
            // SUCCESS
            // -------------------------------------------------

            JOptionPane.showMessageDialog(
                    this,
                    "Your password has been changed successfully.",
                    "Password Changed",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (SQLException ex) {

            showError(
                    getFriendlySqlMessage(
                            ex
                    )
            );

        } catch (Exception ex) {

            showError(
                    ex.getMessage() != null
                            ? ex.getMessage()
                            : "Unable to change password."
            );

        } finally {

            // -------------------------------------------------
            // CLEAR PASSWORD DATA
            // -------------------------------------------------

            java.util.Arrays.fill(
                    currentPassword,
                    '\0'
            );

            java.util.Arrays.fill(
                    newPassword,
                    '\0'
            );

            java.util.Arrays.fill(
                    confirmPassword,
                    '\0'
            );

            // -------------------------------------------------
            // RESTORE BUTTON
            // -------------------------------------------------

            if (changePasswordButton != null) {

                changePasswordButton.setEnabled(
                        true
                );

                changePasswordButton.setText(
                        "Change Password"
                );
            }
        }
    }

    // =========================================================
    // PASSWORD REQUIREMENT MESSAGE
    // =========================================================

    private String getPasswordRequirementMessage() {

        return "Password does not meet the required rules.\n\n"
                + "• At least 8 characters\n"
                + "• At least one uppercase letter (A-Z)\n"
                + "• At least one lowercase letter (a-z)\n"
                + "• At least one number (0-9)\n"
                + "• At least one special character";
    }

    // =========================================================
    // SQL ERROR MESSAGE
    // =========================================================

    private String getFriendlySqlMessage(
            SQLException ex) {

        String message =
                ex.getMessage();

        if (message == null ||
                message.trim().isEmpty()) {

            return "Unable to change password.";
        }

        String lower =
                message.toLowerCase();

        if (lower.contains(
                "incorrect current password")) {

            return "Current password is incorrect.";
        }

        if (lower.contains(
                "player not found")) {

            return "Player account was not found.";
        }

        if (lower.contains(
                "password")) {

            return message;
        }

        return "Unable to change password.\n\n"
                + message;
    }

    // =========================================================
    // WARNING
    // =========================================================

    private void showWarning(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Password",
                JOptionPane.WARNING_MESSAGE
        );
    }

    // =========================================================
    // ERROR
    // =========================================================

    private void showError(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // =========================================================
    // MAIN - TEST ONLY
    // =========================================================

    public static void main(
            String[] args) {

        SwingUtilities.invokeLater(
                () -> {

                    try {

                        UIManager.setLookAndFeel(
                                UIManager
                                        .getSystemLookAndFeelClassName()
                        );

                    } catch (Exception ignored) {
                    }

                    new PlayerChangePasswordUi(
                            "Sairam"
                    ).setVisible(true);
                }
        );
    }
}