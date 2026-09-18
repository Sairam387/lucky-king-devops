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

public class AdminLoginUi extends JFrame {

    private static final long serialVersionUID = 1L;

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color DEEP_NAVY =
            new Color(8, 43, 89);

    private static final Color ROYAL_BLUE =
            new Color(23, 105, 255);

    private static final Color ORANGE =
            new Color(255, 140, 0);

    private static final Color ORANGE_HOVER =
            new Color(255, 167, 38);

    private static final Color ORANGE_PRESSED =
            new Color(230, 118, 0);

    private static final Color FIELD_BACKGROUND =
            new Color(242, 247, 255);

    private static final Color BORDER =
            new Color(184, 203, 229);

    private static final Color DARK_TEXT =
            new Color(16, 35, 63);

    private static final Color SECONDARY_TEXT =
            new Color(88, 112, 143);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color LIGHT_BLUE =
            new Color(230, 240, 255);

    // =========================================================
    // COMPONENTS
    // =========================================================

    private JTextField adminUsernameField;
    private JPasswordField adminPasswordField;

    private JButton loginButton;
    private JButton backButton;
    private JButton showPasswordButton;

    private AdminDao adminDao;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public AdminLoginUi() {

        adminDao = new AdminDao();

        setTitle("LUCKY KING - ADMIN LOGIN");

        setSize(1080, 680);

        setMinimumSize(
                new Dimension(1080, 680)
        );

        setResizable(false);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);

        createUI();
    }

    // =========================================================
    // CREATE UI
    // =========================================================

    private void createUI() {

        JPanel rootPanel =
                new JPanel(new BorderLayout());

        rootPanel.setBackground(DEEP_NAVY);

        rootPanel.setBorder(
                new EmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        JPanel mainPanel =
                new JPanel(
                        new GridLayout(1, 2)
                );

        mainPanel.setBackground(WHITE);

        mainPanel.setBorder(
                new LineBorder(
                        new Color(210, 220, 235),
                        1
                )
        );

        mainPanel.add(createLeftPanel());
        mainPanel.add(createRightPanel());

        rootPanel.add(
                mainPanel,
                BorderLayout.CENTER
        );

        setContentPane(rootPanel);
    }

    // =========================================================
    // LEFT PANEL
    // =========================================================

    private JPanel createLeftPanel() {

        JPanel panel = new JPanel();

        panel.setBackground(DEEP_NAVY);

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

        // -----------------------------------------------------
        // LOGO
        // -----------------------------------------------------

        JLabel logo =
                new JLabel("LK");

        logo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        58
                )
        );

        logo.setForeground(ORANGE);

        logo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(logo);

        panel.add(
                Box.createVerticalStrut(5)
        );

        // -----------------------------------------------------
        // BRAND
        // -----------------------------------------------------

        JLabel brand =
                new JLabel("LUCKY KING");

        brand.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        brand.setForeground(WHITE);

        brand.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(brand);

        panel.add(
                Box.createVerticalStrut(10)
        );

        // -----------------------------------------------------
        // BLUE LINE
        // -----------------------------------------------------

        JPanel line = new JPanel();

        line.setBackground(ROYAL_BLUE);

        line.setPreferredSize(
                new Dimension(300, 4)
        );

        line.setMaximumSize(
                new Dimension(300, 4)
        );

        line.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(line);

        panel.add(
                Box.createVerticalStrut(38)
        );

        // -----------------------------------------------------
        // ADMIN PORTAL
        // -----------------------------------------------------

        JLabel portal =
                new JLabel("ADMIN PORTAL");

        portal.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        portal.setForeground(ORANGE);

        portal.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(portal);

        panel.add(
                Box.createVerticalStrut(20)
        );

        // -----------------------------------------------------
        // DESCRIPTION
        // -----------------------------------------------------

        JLabel description =
                new JLabel(
                        "<html>"
                                + "<div style='width:330px;'>"
                                + "Welcome to the Lucky King "
                                + "Administration Portal."
                                + "<br><br>"
                                + "Authorized administrators can "
                                + "manage players, credits, game "
                                + "operations and system information."
                                + "<br><br>"
                                + "<b>Administrator access only.</b>"
                                + "</div>"
                                + "</html>"
                );

        description.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        description.setForeground(
                new Color(220, 235, 255)
        );

        description.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(description);

        panel.add(
                Box.createVerticalGlue()
        );

        // -----------------------------------------------------
        // SECURITY
        // -----------------------------------------------------

        JLabel security =
                new JLabel("SECURE ADMIN ACCESS");

        security.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        security.setForeground(
                new Color(180, 210, 245)
        );

        security.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(security);

        panel.add(
                Box.createVerticalStrut(8)
        );

        JLabel securityText =
                new JLabel(
                        "Authorized personnel only."
                );

        securityText.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        securityText.setForeground(
                new Color(180, 210, 245)
        );

        securityText.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(securityText);

        return panel;
    }

    // =========================================================
    // RIGHT PANEL
    // =========================================================

    private JPanel createRightPanel() {

        JPanel panel = new JPanel();

        panel.setBackground(WHITE);

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

        // -----------------------------------------------------
        // TITLE
        // -----------------------------------------------------

        JLabel title =
                new JLabel(
                        "Administrator Sign In"
                );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        title.setForeground(DARK_TEXT);

        title.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(title);

        panel.add(
                Box.createVerticalStrut(8)
        );

        JLabel subtitle =
                new JLabel(
                        "Enter your administrator credentials to continue."
                );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(SECONDARY_TEXT);

        subtitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(subtitle);

        panel.add(
                Box.createVerticalStrut(30)
        );

        // -----------------------------------------------------
        // USERNAME
        // -----------------------------------------------------

        JLabel usernameLabel =
                createLabel("ADMIN USERNAME");

        panel.add(usernameLabel);

        panel.add(
                Box.createVerticalStrut(8)
        );

        adminUsernameField =
                new JTextField();

        styleTextField(
                adminUsernameField
        );

        panel.add(adminUsernameField);

        panel.add(
                Box.createVerticalStrut(24)
        );

        // -----------------------------------------------------
        // PASSWORD
        // -----------------------------------------------------

        JLabel passwordLabel =
                createLabel("ADMIN PASSWORD");

        panel.add(passwordLabel);

        panel.add(
                Box.createVerticalStrut(8)
        );

        JPanel passwordPanel =
                new JPanel(new BorderLayout());

        passwordPanel.setBackground(
                FIELD_BACKGROUND
        );

        passwordPanel.setBorder(
                new LineBorder(
                        BORDER,
                        1
                )
        );

        passwordPanel.setPreferredSize(
                new Dimension(390, 48)
        );

        passwordPanel.setMinimumSize(
                new Dimension(390, 48)
        );

        passwordPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        48
                )
        );

        passwordPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        adminPasswordField =
                new JPasswordField();

        adminPasswordField.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        adminPasswordField.setForeground(
                DARK_TEXT
        );

        adminPasswordField.setBackground(
                FIELD_BACKGROUND
        );

        adminPasswordField.setEchoChar('•');

        adminPasswordField.setBorder(
                new EmptyBorder(
                        0,
                        14,
                        0,
                        8
                )
        );

        passwordPanel.add(
                adminPasswordField,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // SHOW PASSWORD
        // -----------------------------------------------------

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
                ROYAL_BLUE
        );

        showPasswordButton.setBackground(
                LIGHT_BLUE
        );

        showPasswordButton.setFocusPainted(false);

        showPasswordButton.setBorderPainted(true);

        showPasswordButton.setBorder(
                new LineBorder(
                        BORDER,
                        1
                )
        );

        showPasswordButton.setPreferredSize(
                new Dimension(70, 48)
        );

        showPasswordButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        showPasswordButton.addActionListener(
                e -> togglePassword()
        );

        passwordPanel.add(
                showPasswordButton,
                BorderLayout.EAST
        );

        panel.add(passwordPanel);

        adminPasswordField.addFocusListener(
                new FocusAdapter() {

                    @Override
                    public void focusGained(
                            FocusEvent e
                    ) {

                        passwordPanel.setBorder(
                                new LineBorder(
                                        ROYAL_BLUE,
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
                                        BORDER,
                                        1
                                )
                        );
                    }
                }
        );

        panel.add(
                Box.createVerticalStrut(30)
        );

        // -----------------------------------------------------
        // LOGIN BUTTON
        // -----------------------------------------------------

        loginButton =
                createMainButton(
                        "ADMIN LOGIN"
                );

        loginButton.addActionListener(
                e -> loginAdmin()
        );

        panel.add(loginButton);

        panel.add(
                Box.createVerticalStrut(12)
        );

        // -----------------------------------------------------
        // BACK BUTTON
        // -----------------------------------------------------

        backButton =
                createMainButton(
                        "BACK TO PLAYER LOGIN"
                );

        backButton.addActionListener(
                e -> goBack()
        );

        panel.add(backButton);

        panel.add(
                Box.createVerticalGlue()
        );

        // -----------------------------------------------------
        // FOOTER
        // -----------------------------------------------------

        JLabel footer =
                new JLabel(
                        "Lucky King Administration Portal"
                );

        footer.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        footer.setForeground(
                new Color(100, 125, 155)
        );

        footer.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(footer);

        // -----------------------------------------------------
        // ENTER KEY
        // -----------------------------------------------------

        adminUsernameField.addActionListener(
                e -> adminPasswordField.requestFocusInWindow()
        );

        adminPasswordField.addActionListener(
                e -> loginAdmin()
        );

        getRootPane().setDefaultButton(
                loginButton
        );

        return panel;
    }

    // =========================================================
    // CREATE LABEL
    // =========================================================

    private JLabel createLabel(String text) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        label.setForeground(DARK_TEXT);

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        return label;
    }

    // =========================================================
    // TEXT FIELD STYLE
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

        field.setForeground(DARK_TEXT);

        field.setBackground(
                FIELD_BACKGROUND
        );

        field.setBorder(
                new LineBorder(
                        BORDER,
                        1
                )
        );

        field.setPreferredSize(
                new Dimension(390, 48)
        );

        field.setMinimumSize(
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

        field.setMargin(
                new Insets(
                        0,
                        14,
                        0,
                        14
                )
        );

        field.addFocusListener(
                new FocusAdapter() {

                    @Override
                    public void focusGained(
                            FocusEvent e
                    ) {

                        field.setBorder(
                                new LineBorder(
                                        ROYAL_BLUE,
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
                                        BORDER,
                                        1
                                )
                        );
                    }
                }
        );
    }

    // =========================================================
    // MAIN BUTTON
    // =========================================================

    private JButton createMainButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        button.setBackground(ORANGE);

        button.setForeground(DARK_TEXT);

        button.setOpaque(true);

        button.setContentAreaFilled(true);

        button.setFocusPainted(false);

        button.setBorderPainted(true);

        button.setBorder(
                new LineBorder(
                        ORANGE,
                        1
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

        button.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                ORANGE_HOVER
                        );

                        button.setBorder(
                                new LineBorder(
                                        ORANGE_HOVER,
                                        1
                                )
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                ORANGE
                        );

                        button.setBorder(
                                new LineBorder(
                                        ORANGE,
                                        1
                                )
                        );
                    }

                    @Override
                    public void mousePressed(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                ORANGE_PRESSED
                        );

                        button.setBorder(
                                new LineBorder(
                                        ORANGE_PRESSED,
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
                                    ORANGE_HOVER
                            );

                        } else {

                            button.setBackground(
                                    ORANGE
                            );
                        }
                    }
                }
        );

        return button;
    }

    // =========================================================
    // SHOW / HIDE PASSWORD
    // =========================================================

    private void togglePassword() {

        if (adminPasswordField.getEchoChar() == 0) {

            adminPasswordField.setEchoChar('•');

            showPasswordButton.setText(
                    "SHOW"
            );

        } else {

            adminPasswordField.setEchoChar(
                    (char) 0
            );

            showPasswordButton.setText(
                    "HIDE"
            );
        }
    }

    // =========================================================
    // ADMIN LOGIN
    // =========================================================

    private void loginAdmin() {

        String username =
                adminUsernameField
                        .getText()
                        .trim();

        String password =
                new String(
                        adminPasswordField
                                .getPassword()
                );

        // -----------------------------------------------------
        // VALIDATION
        // -----------------------------------------------------

        if (username.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter administrator username.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            adminUsernameField.requestFocusInWindow();

            return;
        }

        if (password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter administrator password.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            adminPasswordField.requestFocusInWindow();

            return;
        }

        // -----------------------------------------------------
        // AUTHENTICATION
        // -----------------------------------------------------

        try {

            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "Starting administrator authentication..."
            );

            System.out.println(
                    "Admin Username: " + username
            );

            /*
             * IMPORTANT:
             *
             * AdminDao contains validateAdmin(...)
             *
             * Therefore we MUST call:
             *
             * adminDao.validateAdmin(...)
             *
             * NOT:
             *
             * adminDao.authenticateAdmin(...)
             */

            boolean authenticated =
                    adminDao.validateAdmin(
                            username,
                            password
                    );

            // -------------------------------------------------
            // LOGIN FAILED
            // -------------------------------------------------

            if (!authenticated) {

                System.out.println(
                        "Administrator authentication failed."
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid administrator username or password.",
                        "Admin Login Failed",
                        JOptionPane.WARNING_MESSAGE
                );

                adminPasswordField.selectAll();

                adminPasswordField.requestFocusInWindow();

                return;
            }

            // -------------------------------------------------
            // LOGIN SUCCESS
            // -------------------------------------------------

            System.out.println(
                    "Administrator authentication successful."
            );

            System.out.println(
                    "===================================="
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Administrator login successful.",
                    "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // -------------------------------------------------
            // OPEN ADMIN PANEL
            // -------------------------------------------------

            AdminUi adminUi =
                    new AdminUi();

            adminUi.setLocationRelativeTo(null);

            adminUi.setVisible(true);

            dispose();

        } catch (SQLException e) {

            e.printStackTrace();

            String message =
                    e.getMessage();

            if (message == null
                    || message.trim().isEmpty()) {

                message =
                        "Unable to authenticate administrator.";
            }

            JOptionPane.showMessageDialog(
                    this,
                    message,
                    "Admin Login Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open Admin Panel.\n\n"
                            + e.getClass().getName()
                            + "\n\n"
                            + e.getMessage(),
                    "Admin Login Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // BACK TO PLAYER LOGIN
    // =========================================================

    private void goBack() {

        dispose();

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

                    AdminLoginUi ui =
                            new AdminLoginUi();

                    ui.setLocationRelativeTo(null);

                    ui.setVisible(true);
                }
        );
    }
}