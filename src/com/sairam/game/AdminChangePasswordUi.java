package com.sairam.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * ============================================================
 * LUCKY KING - ADMIN CHANGE PASSWORD UI
 * ============================================================
 *
 * Admin self-service password change screen.
 *
 * Features:
 * - Current password verification
 * - New password validation
 * - Confirm password validation
 * - Live password strength
 * - Live password requirement indicators
 * - PBKDF2 password hashing
 * - Existing admins table preserved
 *
 * ============================================================
 */
public class AdminChangePasswordUi extends JFrame {

    private static final long serialVersionUID = 1L;

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color PURPLE =
            new Color(91, 63, 181);

    private static final Color PURPLE_DARK =
            new Color(66, 42, 145);

    private static final Color PURPLE_LIGHT =
            new Color(241, 237, 255);

    private static final Color GOLD =
            new Color(225, 174, 45);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color BACKGROUND =
            new Color(247, 247, 252);

    private static final Color TEXT =
            new Color(45, 45, 60);

    private static final Color GREY_TEXT =
            new Color(120, 120, 135);

    private static final Color BORDER =
            new Color(220, 220, 232);

    private static final Color RED =
            new Color(210, 55, 55);

    private static final Color GREEN =
            new Color(35, 155, 80);

    // =========================================================
    // FIELDS
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

    private JButton changeButton;

    private JButton clearButton;

    private JButton closeButton;

    private final String adminUsername;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public AdminChangePasswordUi(
            String adminUsername) {

        if (adminUsername == null
                || adminUsername.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Admin username cannot be empty."
            );
        }

        this.adminUsername =
                adminUsername.trim();

        initializeWindow();

        createUI();
    }

    // =========================================================
    // WINDOW
    // =========================================================

    private void initializeWindow() {

        setTitle(
                "Lucky King - Change Admin Password"
        );

        setSize(
                540,
                720
        );

        setMinimumSize(
                new Dimension(
                        520,
                        680
                )
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setResizable(false);
    }

    // =========================================================
    // CREATE UI
    // =========================================================

    private void createUI() {

        JPanel root =
                new JPanel(
                        new BorderLayout()
                );

        root.setBackground(
                BACKGROUND
        );

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

        root.add(
                createHeader(),
                BorderLayout.NORTH
        );

        // -----------------------------------------------------
        // CENTER
        // -----------------------------------------------------

        root.add(
                createCenterPanel(),
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // FOOTER
        // -----------------------------------------------------

        root.add(
                createFooter(),
                BorderLayout.SOUTH
        );

        setContentPane(root);
    }

    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeader() {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                PURPLE_DARK
        );

        header.setBorder(
                new EmptyBorder(
                        15,
                        18,
                        15,
                        18
                )
        );

        // -----------------------------------------------------
        // LEFT
        // -----------------------------------------------------

        JPanel titlePanel =
                new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "LUCKY KING"
                );

        title.setForeground(
                WHITE
        );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        23
                )
        );

        title.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel subtitle =
                new JLabel(
                        "Admin Password Security"
                );

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
                        12
                )
        );

        subtitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        titlePanel.add(title);

        titlePanel.add(
                Box.createVerticalStrut(3)
        );

        titlePanel.add(subtitle);

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        // -----------------------------------------------------
        // ADMIN
        // -----------------------------------------------------

        JLabel admin =
                new JLabel(
                        "Admin: " + adminUsername
                );

        admin.setForeground(
                WHITE
        );

        admin.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        header.add(
                admin,
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

        outer.setBackground(
                BACKGROUND
        );

        outer.setBorder(
                new EmptyBorder(
                        15,
                        0,
                        15,
                        0
                )
        );

        JPanel card =
                new JPanel();

        card.setBackground(
                WHITE
        );

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
        // PAGE TITLE
        // -----------------------------------------------------

        JLabel pageTitle =
                new JLabel(
                        "CHANGE ADMIN PASSWORD"
                );

        pageTitle.setForeground(
                PURPLE_DARK
        );

        pageTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        20
                )
        );

        pageTitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(pageTitle);

        card.add(
                Box.createVerticalStrut(4)
        );

        JLabel description =
                new JLabel(
                        "Create a new secure password for your admin account."
                );

        description.setForeground(
                GREY_TEXT
        );

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
                Box.createVerticalStrut(16)
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
                        "Enter current admin password"
                );

        card.add(
                currentPasswordField
        );

        card.add(
                Box.createVerticalStrut(12)
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
                        "Enter new admin password"
                );

        card.add(
                newPasswordField
        );

        card.add(
                Box.createVerticalStrut(7)
        );

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
                strengthLabel
        );

        card.add(
                Box.createVerticalStrut(12)
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
                        "Re-enter new admin password"
                );

        card.add(
                confirmPasswordField
        );

        card.add(
                Box.createVerticalStrut(14)
        );

        // -----------------------------------------------------
        // REQUIREMENTS
        // -----------------------------------------------------

        card.add(
                createRequirementsPanel()
        );

        outer.add(
                card,
                BorderLayout.CENTER
        );

        // =====================================================
        // IMPORTANT
        // Listener is attached AFTER newPasswordField exists.
        // =====================================================

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

        // Enter key
        confirmPasswordField.addActionListener(
                e -> changePassword()
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

        label.setForeground(
                TEXT
        );

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
                        450,
                        40
                )
        );

        field.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        field.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        field.setForeground(
                TEXT
        );

        field.setBackground(
                new Color(
                        252,
                        252,
                        255
                )
        );

        field.setCaretColor(
                PURPLE
        );

        field.setToolTipText(
                tooltip
        );

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

        // -----------------------------------------------------
        // FOCUS EFFECT
        // -----------------------------------------------------

        field.addFocusListener(
                new FocusAdapter() {

                    @Override
                    public void focusGained(
                            FocusEvent e) {

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
                            FocusEvent e) {

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

        panel.setBackground(
                new Color(
                        249,
                        249,
                        255
                )
        );

        panel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        190
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
                                12,
                                15,
                                12,
                                15
                        )
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
        // ROWS
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

        rows.setOpaque(
                false
        );

        requirementLengthLabel =
                createRequirementLabel(
                        "At least 8 characters"
                );

        requirementUppercaseLabel =
                createRequirementLabel(
                        "At least one uppercase letter (A-Z)"
                );

        requirementLowercaseLabel =
                createRequirementLabel(
                        "At least one lowercase letter (a-z)"
                );

        requirementNumberLabel =
                createRequirementLabel(
                        "At least one number (0-9)"
                );

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

        return label;
    }

    // =========================================================
    // PASSWORD STRENGTH
    // =========================================================

    private void updatePasswordStrength() {

        if (newPasswordField == null) {
            return;
        }

        String password =
                new String(
                        newPasswordField.getPassword()
                );

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
        // UPDATE REQUIREMENTS
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
        // DISPLAY STRENGTH
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
    // REQUIREMENT UPDATE
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
        // CLOSE
        // -----------------------------------------------------

        closeButton =
                new JButton(
                        "Close"
                );

        styleSecondaryButton(
                closeButton
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        // -----------------------------------------------------
        // CLEAR
        // -----------------------------------------------------

        clearButton =
                new JButton(
                        "Clear"
                );

        styleSecondaryButton(
                clearButton
        );

        clearButton.addActionListener(
                e -> clearFields()
        );

        // -----------------------------------------------------
        // CHANGE
        // -----------------------------------------------------

        changeButton =
                new JButton(
                        "Change Password"
                );

        stylePrimaryButton(
                changeButton
        );

        changeButton.addActionListener(
                e -> changePassword()
        );

        footer.add(
                closeButton
        );

        footer.add(
                clearButton
        );

        footer.add(
                changeButton
        );

        return footer;
    }

    // =========================================================
    // PRIMARY BUTTON
    // =========================================================

    private void stylePrimaryButton(
            JButton button) {

        button.setForeground(
                WHITE
        );

        button.setBackground(
                PURPLE
        );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        button.setFocusPainted(
                false
        );

        button.setBorderPainted(
                false
        );

        button.setOpaque(
                true
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setPreferredSize(
                new Dimension(
                        145,
                        40
                )
        );

        addButtonHover(
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

        button.setFocusPainted(
                false
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setPreferredSize(
                new Dimension(
                        80,
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

        addButtonHover(
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

    private void addButtonHover(
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

    private void changePassword() {

        char[] currentChars =
                currentPasswordField.getPassword();

        char[] newChars =
                newPasswordField.getPassword();

        char[] confirmChars =
                confirmPasswordField.getPassword();

        String currentPassword =
                new String(
                        currentChars
                );

        String newPassword =
                new String(
                        newChars
                );

        String confirmPassword =
                new String(
                        confirmChars
                );

        try {

            // -------------------------------------------------
            // CURRENT PASSWORD
            // -------------------------------------------------

            if (currentPassword.isEmpty()) {

                showWarning(
                        "Please enter your current password."
                );

                currentPasswordField.requestFocusInWindow();

                return;
            }

            // -------------------------------------------------
            // NEW PASSWORD
            // -------------------------------------------------

            if (newPassword.isEmpty()) {

                showWarning(
                        "Please enter a new password."
                );

                newPasswordField.requestFocusInWindow();

                return;
            }

            // -------------------------------------------------
            // PASSWORD LENGTH
            // -------------------------------------------------

            if (newPassword.length() < 8) {

                showWarning(
                        "New password must contain at least 8 characters."
                );

                newPasswordField.requestFocusInWindow();

                return;
            }

            // -------------------------------------------------
            // UPPERCASE
            // -------------------------------------------------

            if (!newPassword.matches(
                    ".*[A-Z].*"
            )) {

                showWarning(
                        "New password must contain at least one uppercase letter."
                );

                newPasswordField.requestFocusInWindow();

                return;
            }

            // -------------------------------------------------
            // LOWERCASE
            // -------------------------------------------------

            if (!newPassword.matches(
                    ".*[a-z].*"
            )) {

                showWarning(
                        "New password must contain at least one lowercase letter."
                );

                newPasswordField.requestFocusInWindow();

                return;
            }

            // -------------------------------------------------
            // NUMBER
            // -------------------------------------------------

            if (!newPassword.matches(
                    ".*[0-9].*"
            )) {

                showWarning(
                        "New password must contain at least one number."
                );

                newPasswordField.requestFocusInWindow();

                return;
            }

            // -------------------------------------------------
            // SPECIAL CHARACTER
            // -------------------------------------------------

            if (!newPassword.matches(
                    ".*[^a-zA-Z0-9].*"
            )) {

                showWarning(
                        "New password must contain at least one special character."
                );

                newPasswordField.requestFocusInWindow();

                return;
            }

            // -------------------------------------------------
            // CONFIRM
            // -------------------------------------------------

            if (confirmPassword.isEmpty()) {

                showWarning(
                        "Please confirm your new password."
                );

                confirmPasswordField.requestFocusInWindow();

                return;
            }

            // -------------------------------------------------
            // MATCH
            // -------------------------------------------------

            if (!newPassword.equals(
                    confirmPassword
            )) {

                showWarning(
                        "New password and confirm password do not match."
                );

                confirmPasswordField.requestFocusInWindow();

                return;
            }

            // -------------------------------------------------
            // SAME PASSWORD
            // -------------------------------------------------

            if (currentPassword.equals(
                    newPassword
            )) {

                showWarning(
                        "New password must be different from the current password."
                );

                newPasswordField.requestFocusInWindow();

                return;
            }

            // -------------------------------------------------
            // DISABLE BUTTON
            // -------------------------------------------------

            changeButton.setEnabled(
                    false
            );

            changeButton.setText(
                    "Updating..."
            );

            // -------------------------------------------------
            // GET STORED PASSWORD
            // -------------------------------------------------

            String storedPassword =
                    getStoredPassword();

            if (storedPassword == null
                    || storedPassword.trim().isEmpty()) {

                showError(
                        "Admin password could not be found."
                );

                return;
            }

            // -------------------------------------------------
            // VERIFY CURRENT PASSWORD
            // -------------------------------------------------

            boolean currentPasswordValid;

            if (storedPassword.startsWith(
                    "PBKDF2$"
            )) {

                currentPasswordValid =
                        PasswordUtil.verifyPassword(
                                currentPassword,
                                storedPassword
                        );

            } else {

                /*
                 * Backward compatibility:
                 * Existing plaintext password records
                 * can still be verified.
                 */
                currentPasswordValid =
                        storedPassword.equals(
                                currentPassword
                        );
            }

            // -------------------------------------------------
            // INVALID CURRENT PASSWORD
            // -------------------------------------------------

            if (!currentPasswordValid) {

                showWarning(
                        "Current password is incorrect."
                );

                currentPasswordField.selectAll();

                currentPasswordField.requestFocusInWindow();

                return;
            }

            // -------------------------------------------------
            // HASH NEW PASSWORD
            // -------------------------------------------------

            String hashedPassword =
                    PasswordUtil.hashPassword(
                            newPassword
                    );

            // -------------------------------------------------
            // UPDATE DATABASE
            // -------------------------------------------------

            updatePassword(
                    hashedPassword
            );

            // -------------------------------------------------
            // SUCCESS
            // -----------------------------------------------------

            JOptionPane.showMessageDialog(
                    this,
                    "Admin password changed successfully!\n\n"
                            + "Your new password is now protected using PBKDF2.",
                    "Password Changed",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearFields();

            dispose();

        } catch (SQLException e) {

            e.printStackTrace();

            showError(
                    "Unable to change admin password.\n\n"
                            + "Database error:\n"
                            + e.getMessage()
            );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Unexpected password change error.\n\n"
                            + e.getMessage()
            );

        } finally {

            // -------------------------------------------------
            // CLEAR PASSWORD ARRAYS
            // -------------------------------------------------

            Arrays.fill(
                    currentChars,
                    '\0'
            );

            Arrays.fill(
                    newChars,
                    '\0'
            );

            Arrays.fill(
                    confirmChars,
                    '\0'
            );

            // -------------------------------------------------
            // RESTORE BUTTON
            // -------------------------------------------------

            if (changeButton != null) {

                changeButton.setEnabled(
                        true
                );

                changeButton.setText(
                        "Change Password"
                );
            }
        }
    }

    // =========================================================
    // GET STORED PASSWORD
    // =========================================================

    private String getStoredPassword()
            throws SQLException {

        String sql =
                "SELECT password "
                        + "FROM admins "
                        + "WHERE LOWER(username) = LOWER(?) "
                        + "LIMIT 1";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    adminUsername
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return resultSet.getString(
                            "password"
                    );
                }
            }
        }

        return null;
    }

    // =========================================================
    // UPDATE PASSWORD
    // =========================================================

    private void updatePassword(
            String hashedPassword)
            throws SQLException {

        String sql =
                "UPDATE admins "
                        + "SET password = ? "
                        + "WHERE LOWER(username) = LOWER(?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    hashedPassword
            );

            statement.setString(
                    2,
                    adminUsername
            );

            int rows =
                    statement.executeUpdate();

            if (rows != 1) {

                throw new SQLException(
                        "Admin account was not found."
                );
            }
        }
    }

    // =========================================================
    // CLEAR
    // =========================================================

    private void clearFields() {

        currentPasswordField.setText("");

        newPasswordField.setText("");

        confirmPasswordField.setText("");

        strengthLabel.setText(
                "Password strength: —"
        );

        strengthLabel.setForeground(
                GREY_TEXT
        );

        updateRequirementLabel(
                requirementLengthLabel,
                "At least 8 characters",
                false
        );

        updateRequirementLabel(
                requirementUppercaseLabel,
                "At least one uppercase letter (A-Z)",
                false
        );

        updateRequirementLabel(
                requirementLowercaseLabel,
                "At least one lowercase letter (a-z)",
                false
        );

        updateRequirementLabel(
                requirementNumberLabel,
                "At least one number (0-9)",
                false
        );

        updateRequirementLabel(
                requirementSpecialLabel,
                "At least one special character",
                false
        );

        currentPasswordField.requestFocusInWindow();
    }

    // =========================================================
    // WARNING
    // =========================================================

    private void showWarning(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Validation Error",
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
                "Password Change Error",
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

                        javax.swing.UIManager
                                .setLookAndFeel(
                                        javax.swing.UIManager
                                                .getSystemLookAndFeelClassName()
                                );

                    } catch (Exception ignored) {
                    }

                    new AdminChangePasswordUi(
                            "admin"
                    ).setVisible(
                            true
                    );
                }
        );
    }
}