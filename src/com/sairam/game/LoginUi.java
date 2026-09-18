package com.sairam.game;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class LoginUi extends JFrame {

    private static final long serialVersionUID = 1L;

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color COLOR_DEEP_NAVY =
            new Color(8, 43, 89);

    private static final Color COLOR_PRIMARY_BLUE =
            new Color(23, 105, 255);

    private static final Color COLOR_LOGIN_ORANGE =
            new Color(255, 140, 0);

    private static final Color COLOR_LOGIN_ORANGE_HOVER =
            new Color(255, 167, 38);

    private static final Color COLOR_LOGIN_ORANGE_PRESSED =
            new Color(230, 118, 0);

    private static final Color COLOR_TEXT_DARK =
            new Color(16, 35, 63);

    private static final Color COLOR_TEXT_SECONDARY =
            new Color(88, 112, 143);

    private static final Color COLOR_TEXT_DESCRIPTION =
            new Color(220, 235, 255);

    private static final Color COLOR_TEXT_SECURITY =
            new Color(180, 210, 245);

    private static final Color COLOR_BUTTON_TEXT =
            new Color(16, 35, 63);

    private static final Color COLOR_FOOTER_TEXT =
            new Color(100, 125, 155);

    private static final Color COLOR_WHITE =
            new Color(255, 255, 255);

    // =========================================================
    // COMPONENTS
    // =========================================================

    private JTextField playerNameField;
    private JPasswordField passwordField;

    private JButton loginButton;
    private JButton registrationButton;
    private JButton adminLoginButton;
    private JButton showPasswordButton;

    private PlayerDao playerDao;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public LoginUi() {

        playerDao = new PlayerDao();

        setTitle("Lucky King - Player Login");

        setSize(1080, 680);

        setMinimumSize(
                new Dimension(1080, 680)
        );

        setResizable(false);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        createUI();

        setupKeyboardActions();
    }

    // =========================================================
    // CREATE UI
    // =========================================================

    private void createUI() {

        JPanel rootPanel =
                new JPanel(
                        new GridLayout(1, 1)
                );

        rootPanel.setBackground(
                COLOR_DEEP_NAVY
        );

        rootPanel.setBorder(
                new EmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        JPanel mainCard =
                new JPanel(
                        new GridLayout(1, 2)
                );

        mainCard.setBackground(
                COLOR_WHITE
        );

        mainCard.setBorder(
                new LineBorder(
                        new Color(210, 220, 235),
                        1
                )
        );

        JPanel brandPanel =
                createBrandPanel();

        JPanel loginPanel =
                createLoginPanel();

        mainCard.add(brandPanel);
        mainCard.add(loginPanel);

        rootPanel.add(mainCard);

        setContentPane(rootPanel);
    }

    // =========================================================
    // BRAND PANEL
    // =========================================================

    private JPanel createBrandPanel() {

        JPanel panel =
                new JPanel();

        panel.setBackground(
                COLOR_DEEP_NAVY
        );

        panel.setBorder(
                new EmptyBorder(
                        55,
                        55,
                        55,
                        55
                )
        );

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        // =====================================================
        // LOGO
        // =====================================================

        JLabel logoLabel =
                new JLabel("LK");

        logoLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        54
                )
        );

        logoLabel.setForeground(
                COLOR_LOGIN_ORANGE
        );

        logoLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(logoLabel);

        panel.add(
                Box.createVerticalStrut(10)
        );

        // =====================================================
        // BRAND NAME
        // =====================================================

        JLabel brandLabel =
                new JLabel("LUCKY KINGDOM");

        brandLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        brandLabel.setForeground(
                COLOR_WHITE
        );

        brandLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(brandLabel);

        panel.add(
                Box.createVerticalStrut(8)
        );

        // =====================================================
        // BLUE LINE
        // =====================================================

        JPanel blueLine =
                new JPanel();

        blueLine.setBackground(
                COLOR_PRIMARY_BLUE
        );

        blueLine.setPreferredSize(
                new Dimension(300, 4)
        );

        blueLine.setMaximumSize(
                new Dimension(300, 4)
        );

        blueLine.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(blueLine);

        panel.add(
                Box.createVerticalStrut(35)
        );

        // =====================================================
        // WELCOME
        // =====================================================

        JLabel portalLabel =
                new JLabel(
                        "Welcome to the Lucky King"
                );

        portalLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        portalLabel.setForeground(
                COLOR_LOGIN_ORANGE
        );

        portalLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(portalLabel);

        panel.add(
                Box.createVerticalStrut(20)
        );

        // =====================================================
        // DESCRIPTION
        // =====================================================

        JLabel descriptionLabel =
                new JLabel(
                        "<html><div style='width:330px;'>"
                        + "Sign in to access your world, "
                        + "Play Bold win Bold."
                        + "</div></html>"
                );

        descriptionLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        descriptionLabel.setForeground(
                COLOR_TEXT_DESCRIPTION
        );

        descriptionLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(descriptionLabel);

        panel.add(
                Box.createVerticalGlue()
        );

        // =====================================================
        // SECURITY
        // =====================================================

        JLabel securityLabel =
                new JLabel(
                        "SECURE PLAYER ACCESS"
                );

        securityLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        securityLabel.setForeground(
                COLOR_TEXT_SECURITY
        );

        securityLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(securityLabel);

        panel.add(
                Box.createVerticalStrut(10)
        );

        JLabel secureInfoLabel =
                new JLabel(
                        "Your account information is protected."
                );

        secureInfoLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        10
                )
        );

        secureInfoLabel.setForeground(
                COLOR_TEXT_SECURITY
        );

        secureInfoLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(secureInfoLabel);

        return panel;
    }

    // =========================================================
    // LOGIN PANEL
    // =========================================================

    private JPanel createLoginPanel() {

        JPanel panel =
                new JPanel();

        panel.setBackground(
                COLOR_WHITE
        );

        panel.setBorder(
                new EmptyBorder(
                        55,
                        65,
                        45,
                        65
                )
        );

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        // =====================================================
        // TITLE
        // =====================================================

        JLabel titleLabel =
                new JLabel("Sign In");

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        titleLabel.setForeground(
                COLOR_TEXT_DARK
        );

        titleLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(titleLabel);

        panel.add(
                Box.createVerticalStrut(9)
        );

        // =====================================================
        // SUBTITLE
        // =====================================================

        JLabel subtitleLabel =
                new JLabel(
                        "Enter your player credentials to continue."
                );

        subtitleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        subtitleLabel.setForeground(
                COLOR_TEXT_SECONDARY
        );

        subtitleLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(subtitleLabel);

        panel.add(
                Box.createVerticalStrut(32)
        );

        // =====================================================
        // PLAYER NAME LABEL
        // =====================================================

        JLabel playerNameLabel =
                createFieldLabel(
                        " USERNAME"
                );

        panel.add(playerNameLabel);

        panel.add(
                Box.createVerticalStrut(8)
        );

        // =====================================================
        // PLAYER NAME FIELD
        // WHITE INSIDE + ORANGE BORDER
        // =====================================================

        playerNameField =
                new JTextField();

        styleTextField(
                playerNameField
        );

        panel.add(playerNameField);

        panel.add(
                Box.createVerticalStrut(20)
        );

        // =====================================================
        // PASSWORD LABEL
        // =====================================================

        JLabel passwordLabel =
                createFieldLabel(
                        "PASSWORD"
                );

        panel.add(passwordLabel);

        panel.add(
                Box.createVerticalStrut(8)
        );

        // =====================================================
        // PASSWORD CONTAINER
        // WHITE INSIDE + ORANGE BORDER
        // =====================================================

        JPanel passwordPanel =
                new JPanel(
                        new BorderLayout()
                );

        passwordPanel.setBackground(
                COLOR_WHITE
        );

        passwordPanel.setBorder(
                new LineBorder(
                        COLOR_LOGIN_ORANGE,
                        1
                )
        );

        passwordPanel.setPreferredSize(
                new Dimension(390, 48)
        );

        passwordPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        48
                )
        );

        // =====================================================
        // PASSWORD FIELD
        // =====================================================

        passwordField =
                new JPasswordField();

        passwordField.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        passwordField.setForeground(
                COLOR_TEXT_DARK
        );

        passwordField.setBackground(
                COLOR_WHITE
        );

        passwordField.setBorder(
                new EmptyBorder(
                        0,
                        14,
                        0,
                        8
                )
        );

        passwordField.setOpaque(true);

        passwordPanel.add(
                passwordField,
                BorderLayout.CENTER
        );

        // =====================================================
        // SHOW BUTTON
        // =====================================================

        showPasswordButton =
                new JButton("SHOW");

        showPasswordButton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        showPasswordButton.setForeground(
                COLOR_BUTTON_TEXT
        );

        showPasswordButton.setBackground(
                COLOR_LOGIN_ORANGE
        );

        showPasswordButton.setOpaque(true);

        showPasswordButton.setContentAreaFilled(true);

        showPasswordButton.setFocusPainted(
                false
        );

        showPasswordButton.setBorder(
                new LineBorder(
                        COLOR_LOGIN_ORANGE,
                        1
                )
        );

        showPasswordButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        showPasswordButton.setPreferredSize(
                new Dimension(70, 48)
        );

        showPasswordButton.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        showPasswordButton.setBackground(
                                COLOR_LOGIN_ORANGE_HOVER
                        );

                        showPasswordButton.setBorder(
                                new LineBorder(
                                        COLOR_LOGIN_ORANGE_HOVER,
                                        1
                                )
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        showPasswordButton.setBackground(
                                COLOR_LOGIN_ORANGE
                        );

                        showPasswordButton.setBorder(
                                new LineBorder(
                                        COLOR_LOGIN_ORANGE,
                                        1
                                )
                        );
                    }

                    @Override
                    public void mousePressed(
                            MouseEvent e
                    ) {

                        showPasswordButton.setBackground(
                                COLOR_LOGIN_ORANGE_PRESSED
                        );
                    }

                    @Override
                    public void mouseReleased(
                            MouseEvent e
                    ) {

                        if (showPasswordButton.contains(
                                e.getPoint()
                        )) {

                            showPasswordButton.setBackground(
                                    COLOR_LOGIN_ORANGE_HOVER
                            );

                        } else {

                            showPasswordButton.setBackground(
                                    COLOR_LOGIN_ORANGE
                            );
                        }
                    }
                }
        );

        showPasswordButton.addActionListener(
                e -> togglePassword()
        );

        passwordPanel.add(
                showPasswordButton,
                BorderLayout.EAST
        );

        passwordPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        // =====================================================
        // PASSWORD FOCUS EFFECT
        // =====================================================

        passwordField.addFocusListener(
                new FocusAdapter() {

                    @Override
                    public void focusGained(
                            FocusEvent e
                    ) {

                        passwordPanel.setBorder(
                                new LineBorder(
                                        COLOR_LOGIN_ORANGE_HOVER,
                                        2
                                )
                        );
                    }

                    @Override
                    public void focusLost(
                            FocusEvent e
                    ) {

                        passwordPanel.setBorder(
                                new LineBorder(
                                        COLOR_LOGIN_ORANGE,
                                        1
                                )
                        );
                    }
                }
        );

        panel.add(passwordPanel);

        panel.add(
                Box.createVerticalStrut(28)
        );

        // =====================================================
        // BUTTON PANEL
        // =====================================================

        JPanel buttonsPanel =
                new JPanel(
                        new GridLayout(
                                3,
                                1,
                                0,
                                10
                        )
                );

        buttonsPanel.setOpaque(false);

        buttonsPanel.setPreferredSize(
                new Dimension(390, 164)
        );

        buttonsPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        164
                )
        );

        buttonsPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        // =====================================================
        // LOGIN
        // =====================================================

        loginButton =
                createActionButton(
                        "LOGIN"
                );

        // =====================================================
        // CREATE ACCOUNT
        // =====================================================

        registrationButton =
                createActionButton(
                        "CREATE NEW ACCOUNT"
                );

        // =====================================================
        // ADMIN LOGIN
        // =====================================================

        adminLoginButton =
                createActionButton(
                        "ADMIN LOGIN"
                );

        buttonsPanel.add(
                loginButton
        );

        buttonsPanel.add(
                registrationButton
        );

        buttonsPanel.add(
                adminLoginButton
        );

        panel.add(buttonsPanel);

        panel.add(
                Box.createVerticalGlue()
        );

        // =====================================================
        // FOOTER
        // =====================================================

        JLabel footerLabel =
                new JLabel(
                        "Welcome to the Lucky Kingdom"
                );

        footerLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        10
                )
        );

        footerLabel.setForeground(
                COLOR_FOOTER_TEXT
        );

        footerLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(footerLabel);

        // =====================================================
        // BUTTON ACTIONS
        // =====================================================

        loginButton.addActionListener(
                e -> loginPlayer()
        );

        registrationButton.addActionListener(
                e -> openPlayerRegistration()
        );

        adminLoginButton.addActionListener(
                e -> openAdminLogin()
        );

        return panel;
    }

    // =========================================================
    // FIELD LABEL
    // =========================================================

    private JLabel createFieldLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        label.setForeground(
                COLOR_TEXT_DARK
        );

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        return label;
    }

    // =========================================================
    // PLAYER NAME FIELD
    // WHITE INSIDE + ORANGE BORDER
    // =========================================================

    private void styleTextField(
            JTextField field
    ) {

        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        field.setForeground(
                COLOR_TEXT_DARK
        );

        // WHITE INSIDE
        field.setBackground(
                COLOR_WHITE
        );

        // SAME ORANGE BORDER AS LOGIN BUTTON
        field.setBorder(
                new LineBorder(
                        COLOR_LOGIN_ORANGE,
                        1
                )
        );

        field.setPreferredSize(
                new Dimension(390, 48)
        );

        field.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        48
                )
        );

        field.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        field.setOpaque(true);

        // =====================================================
        // FOCUS BORDER
        // =====================================================

        field.addFocusListener(
                new FocusAdapter() {

                    @Override
                    public void focusGained(
                            FocusEvent e
                    ) {

                        field.setBorder(
                                new LineBorder(
                                        COLOR_LOGIN_ORANGE_HOVER,
                                        2
                                )
                        );
                    }

                    @Override
                    public void focusLost(
                            FocusEvent e
                    ) {

                        field.setBorder(
                                new LineBorder(
                                        COLOR_LOGIN_ORANGE,
                                        1
                                )
                        );
                    }
                }
        );
    }

    // =========================================================
    // ACTION BUTTON
    // =========================================================

    private JButton createActionButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        button.setForeground(
                COLOR_BUTTON_TEXT
        );

        button.setBackground(
                COLOR_LOGIN_ORANGE
        );

        button.setOpaque(true);

        button.setContentAreaFilled(true);

        button.setBorderPainted(true);

        button.setBorder(
                new LineBorder(
                        COLOR_LOGIN_ORANGE,
                        1
                )
        );

        button.setFocusPainted(false);

        button.setRolloverEnabled(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setPreferredSize(
                new Dimension(390, 48)
        );

        button.setMinimumSize(
                new Dimension(390, 48)
        );

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        48
                )
        );

        // =====================================================
        // MOUSE EFFECT
        // =====================================================

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                COLOR_LOGIN_ORANGE_HOVER
                        );

                        button.setBorder(
                                new LineBorder(
                                        COLOR_LOGIN_ORANGE_HOVER,
                                        1
                                )
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                COLOR_LOGIN_ORANGE
                        );

                        button.setBorder(
                                new LineBorder(
                                        COLOR_LOGIN_ORANGE,
                                        1
                                )
                        );
                    }

                    @Override
                    public void mousePressed(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                COLOR_LOGIN_ORANGE_PRESSED
                        );

                        button.setBorder(
                                new LineBorder(
                                        COLOR_LOGIN_ORANGE_PRESSED,
                                        1
                                )
                        );
                    }

                    @Override
                    public void mouseReleased(
                            MouseEvent e
                    ) {

                        if (button.contains(
                                e.getPoint()
                        )) {

                            button.setBackground(
                                    COLOR_LOGIN_ORANGE_HOVER
                            );

                            button.setBorder(
                                    new LineBorder(
                                            COLOR_LOGIN_ORANGE_HOVER,
                                            1
                                    )
                            );

                        } else {

                            button.setBackground(
                                    COLOR_LOGIN_ORANGE
                            );

                            button.setBorder(
                                    new LineBorder(
                                            COLOR_LOGIN_ORANGE,
                                            1
                                    )
                            );
                        }
                    }
                }
        );

        return button;
    }

    // =========================================================
    // KEYBOARD ACTIONS
    // =========================================================

    private void setupKeyboardActions() {

        passwordField.addActionListener(
                e -> loginPlayer()
        );

        playerNameField.addActionListener(
                e -> passwordField.requestFocusInWindow()
        );

        getRootPane().setDefaultButton(
                loginButton
        );
    }

    // =========================================================
    // SHOW / HIDE PASSWORD
    // =========================================================

    private void togglePassword() {

        if (passwordField.getEchoChar() == 0) {

            passwordField.setEchoChar('•');

            showPasswordButton.setText(
                    "SHOW"
            );

        } else {

            passwordField.setEchoChar(
                    (char) 0
            );

            showPasswordButton.setText(
                    "HIDE"
            );
        }
    }

    // =========================================================
    // LOGIN PLAYER
    // =========================================================

    private void loginPlayer() {

        String playerName =
                playerNameField
                        .getText()
                        .trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        // =====================================================
        // PLAYER NAME EMPTY
        // =====================================================

        if (playerName.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter player name.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            playerNameField.requestFocus();

            return;
        }

        // =====================================================
        // USERNAME VALIDATION
        // =====================================================

        if (!CredentialRules.isValidUsername(
                playerName
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid player name.\n\n"
                    + "Player name requirements:\n"
                    + "• Minimum 6 characters\n"
                    + "• Maximum 12 characters\n"
                    + "• Letters and numbers allowed\n"
                    + "• Allowed symbols: _ @ # $ % &\n"
                    + "• Spaces are not allowed",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            playerNameField.requestFocus();

            return;
        }

        // =====================================================
        // PASSWORD EMPTY
        // =====================================================

        if (password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter password.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            passwordField.requestFocus();

            return;
        }

        // =====================================================
        // PASSWORD VALIDATION
        // =====================================================

        if (!CredentialRules.isValidPassword(
                password
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    CredentialRules.PASSWORD_REQUIREMENTS,
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            passwordField.requestFocus();

            return;
        }

        // =====================================================
        // DATABASE AUTHENTICATION
        // =====================================================

        try {

            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "Starting player authentication..."
            );

            System.out.println(
                    "Player Name: "
                    + playerName
            );

            Player player =
                    playerDao.authenticatePlayer(
                            playerName,
                            password
                    );

            // =================================================
            // LOGIN FAILED
            // =================================================

            if (player == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid player name or password.",
                        "Login Failed",
                        JOptionPane.WARNING_MESSAGE
                );

                passwordField.selectAll();

                passwordField.requestFocus();

                return;
            }

            // =================================================
            // LOGIN SUCCESS
            // =================================================

            System.out.println(
                    "Authentication successful."
            );

            System.out.println(
                    "Player: "
                    + player.getName()
            );

            System.out.println(
                    "Credits: "
                    + player.getCredits()
            );

            // =================================================
            // OPEN GAME UI
            // =================================================

            GameUi gameUi =
                    new GameUi(
                            player.getName(),
                            player.getCredits()
                    );

            gameUi.setLocationRelativeTo(null);

            gameUi.setVisible(true);

            dispose();

            System.out.println(
                    "GameUi opened successfully."
            );

            System.out.println(
                    "===================================="
            );

        } catch (SQLException e) {

            e.printStackTrace();

            String message =
                    e.getMessage();

            if (message == null
                    || message.trim().isEmpty()) {

                message =
                        "Unable to authenticate player.";
            }

            JOptionPane.showMessageDialog(
                    this,
                    message,
                    "Login Failed",
                    JOptionPane.WARNING_MESSAGE
            );

            passwordField.selectAll();

            passwordField.requestFocus();

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to start the game.\n\n"
                    + "Error:\n"
                    + e.getMessage(),
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // OPEN PLAYER REGISTRATION
    // =========================================================

    private void openPlayerRegistration() {

        try {

            PlayerRegistrationUi registrationUi =
                    new PlayerRegistrationUi();

            registrationUi.setLocationRelativeTo(
                    this
            );

            registrationUi.setVisible(true);

            dispose();

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open Player Registration.\n\n"
                    + e.getClass().getName()
                    + "\n\n"
                    + e.getMessage(),
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // OPEN ADMIN LOGIN
    // =========================================================

    private void openAdminLogin() {

        try {

            AdminLoginUi adminLoginUi =
                    new AdminLoginUi();

            adminLoginUi.setLocationRelativeTo(
                    this
            );

            adminLoginUi.setVisible(true);

            dispose();

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open Admin Login.\n\n"
                    + e.getClass().getName()
                    + "\n\n"
                    + e.getMessage(),
                    "Admin Login Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {

        try {

            UIManager.setLookAndFeel(
                    UIManager
                            .getSystemLookAndFeelClassName()
            );

        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(
                () -> {

                    LoginUi loginUi =
                            new LoginUi();

                    loginUi.setLocationRelativeTo(
                            null
                    );

                    loginUi.setVisible(true);
                }
        );
    }
}