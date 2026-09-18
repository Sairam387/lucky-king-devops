
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

public class PlayerRegistrationUi extends JFrame {

    private static final long serialVersionUID = 1L;

    // =========================================================
    // LUCKY KING / CEIPAL-STYLE COLORS
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

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    private JButton registerButton;
    private JButton clearButton;
    private JButton backButton;

    private JButton showPasswordButton;
    private JButton showConfirmPasswordButton;

    private PlayerDao playerDao;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public PlayerRegistrationUi() {

        playerDao = new PlayerDao();

        setTitle("LUCKY KING - CREATE ACCOUNT");

        setSize(1080, 700);
        setMinimumSize(new Dimension(1080, 700));
        setResizable(false);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
                new JPanel(new GridLayout(1, 2));

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

        JPanel line =
                new JPanel();

        line.setBackground(ROYAL_BLUE);

        line.setPreferredSize(
                new Dimension(
                        300,
                        4
                )
        );

        line.setMaximumSize(
                new Dimension(
                        300,
                        4
                )
        );

        line.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(line);

        panel.add(
                Box.createVerticalStrut(38)
        );

        // -----------------------------------------------------
        // CREATE ACCOUNT
        // -----------------------------------------------------

        JLabel heading =
                new JLabel("CREATE ACCOUNT");

        heading.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        heading.setForeground(ORANGE);

        heading.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(heading);

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
                                + "Create your Lucky King player account "
                                + "and start your gaming journey."
                                + "<br><br>"
                                + "After successful registration, "
                                + "your account will receive "
                                + "<b>1000 starting credits</b>."
                                + "<br><br>"
                                + "Keep your username and password "
                                + "secure and do not share them."
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
                new Color(
                        220,
                        235,
                        255
                )
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
                new JLabel(
                        "SECURE ACCOUNT CREATION"
                );

        security.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        security.setForeground(
                new Color(
                        180,
                        210,
                        245
                )
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
                        "Password is securely processed."
                );

        securityText.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        securityText.setForeground(
                new Color(
                        180,
                        210,
                        245
                )
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

        JPanel panel =
                new JPanel();

        panel.setBackground(WHITE);

        panel.setBorder(
                new EmptyBorder(
                        40,
                        65,
                        35,
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
                new JLabel("Create New Account");

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        title.setForeground(DARK_TEXT);

        title.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(title);

        panel.add(
                Box.createVerticalStrut(7)
        );

        JLabel subtitle =
                new JLabel(
                        "Enter your details to create a player account."
                );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(
                SECONDARY_TEXT
        );

        subtitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(subtitle);

        panel.add(
                Box.createVerticalStrut(22)
        );

        // -----------------------------------------------------
        // USERNAME
        // -----------------------------------------------------

        panel.add(
                createLabel("PLAYER NAME")
        );

        panel.add(
                Box.createVerticalStrut(7)
        );

        usernameField =
                new JTextField();

        styleTextField(
                usernameField
        );

        panel.add(usernameField);

        panel.add(
                Box.createVerticalStrut(5)
        );

        JLabel usernameRequirement =
                new JLabel(
                        "6–12 characters • Letters, numbers • _ @ # $ % &"
                );

        usernameRequirement.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        usernameRequirement.setForeground(
                SECONDARY_TEXT
        );

        usernameRequirement.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(usernameRequirement);

        panel.add(
                Box.createVerticalStrut(16)
        );

        // -----------------------------------------------------
        // PASSWORD
        // -----------------------------------------------------

        panel.add(
                createLabel("PASSWORD")
        );

        panel.add(
                Box.createVerticalStrut(7)
        );

        JPanel passwordPanel =
                createPasswordPanel(
                        false
                );

        panel.add(passwordPanel);

        panel.add(
                Box.createVerticalStrut(5)
        );

        JLabel passwordRequirement =
                new JLabel(
                        "Minimum 6 characters • At least one letter and one number"
                );

        passwordRequirement.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        passwordRequirement.setForeground(
                SECONDARY_TEXT
        );

        passwordRequirement.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(passwordRequirement);

        panel.add(
                Box.createVerticalStrut(16)
        );

        // -----------------------------------------------------
        // CONFIRM PASSWORD
        // -----------------------------------------------------

        panel.add(
                createLabel("CONFIRM PASSWORD")
        );

        panel.add(
                Box.createVerticalStrut(7)
        );

        JPanel confirmPasswordPanel =
                createPasswordPanel(
                        true
                );

        panel.add(confirmPasswordPanel);

        panel.add(
                Box.createVerticalStrut(22)
        );

        // -----------------------------------------------------
        // BUTTONS
        // -----------------------------------------------------

        registerButton =
                createMainButton(
                        "REGISTER"
                );

        clearButton =
                createMainButton(
                        "CLEAR"
                );

        backButton =
                createMainButton(
                        "BACK"
                );

        registerButton.addActionListener(
                e -> registerPlayer()
        );

        clearButton.addActionListener(
                e -> clearFields()
        );

        backButton.addActionListener(
                e -> goBack()
        );

        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                10,
                                0
                        )
                );

        buttonPanel.setOpaque(false);

        buttonPanel.setPreferredSize(
                new Dimension(
                        390,
                        48
                )
        );

        buttonPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        48
                )
        );

        buttonPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        buttonPanel.add(registerButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        panel.add(buttonPanel);

        panel.add(
                Box.createVerticalStrut(18)
        );

        // -----------------------------------------------------
        // INFORMATION BOX
        // -----------------------------------------------------

        JPanel infoPanel =
                new JPanel(
                        new BorderLayout()
                );

        infoPanel.setBackground(
                new Color(
                        242,
                        247,
                        255
                )
        );

        infoPanel.setBorder(
                new LineBorder(
                        new Color(
                                210,
                                225,
                                245
                        ),
                        1
                )
        );

        infoPanel.setPreferredSize(
                new Dimension(
                        390,
                        58
                )
        );

        infoPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        58
                )
        );

        JLabel infoLabel =
                new JLabel(
                        "<html>"
                                + "<b>Starting Credits: 1000</b>"
                                + "&nbsp;&nbsp; | &nbsp;&nbsp;"
                                + "Password securely processed"
                                + "</html>"
                );

        infoLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        infoLabel.setForeground(
                DARK_TEXT
        );

        infoLabel.setBorder(
                new EmptyBorder(
                        0,
                        12,
                        0,
                        12
                )
        );

        infoPanel.add(
                infoLabel,
                BorderLayout.CENTER
        );

        panel.add(infoPanel);

        panel.add(
                Box.createVerticalGlue()
        );

        // -----------------------------------------------------
        // FOOTER
        // -----------------------------------------------------

        JLabel footer =
                new JLabel(
                        "Lucky King Player Registration"
                );

        footer.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        footer.setForeground(
                new Color(
                        100,
                        125,
                        155
                )
        );

        footer.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(footer);

        // -----------------------------------------------------
        // ENTER KEY
        // -----------------------------------------------------

        confirmPasswordField.addActionListener(
                e -> registerPlayer()
        );

        passwordField.addActionListener(
                e -> confirmPasswordField.requestFocusInWindow()
        );

        usernameField.addActionListener(
                e -> passwordField.requestFocusInWindow()
        );

        return panel;
    }

    // =========================================================
    // PASSWORD PANEL
    // =========================================================

    private JPanel createPasswordPanel(
            boolean confirm
    ) {

        JPanel passwordPanel =
                new JPanel(
                        new BorderLayout()
                );

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
                new Dimension(
                        390,
                        48
                )
        );

        passwordPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        48
                )
        );

        JPasswordField field =
                new JPasswordField();

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
                new EmptyBorder(
                        0,
                        14,
                        0,
                        8
                )
        );

        field.setEchoChar('•');

        if (confirm) {

            confirmPasswordField = field;

        } else {

            passwordField = field;
        }

        JButton showButton =
                new JButton("SHOW");

        showButton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        showButton.setForeground(
                ROYAL_BLUE
        );

        showButton.setBackground(
                LIGHT_BLUE
        );

        showButton.setFocusPainted(false);

        showButton.setBorder(
                new LineBorder(
                        BORDER,
                        1
                )
        );

        showButton.setPreferredSize(
                new Dimension(
                        70,
                        48
                )
        );

        showButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        showButton.addActionListener(
                e -> {

                    if (field.getEchoChar() == 0) {

                        field.setEchoChar('•');
                        showButton.setText("SHOW");

                    } else {

                        field.setEchoChar((char) 0);
                        showButton.setText("HIDE");
                    }
                }
        );

        if (confirm) {

            showConfirmPasswordButton =
                    showButton;

        } else {

            showPasswordButton =
                    showButton;
        }

        passwordPanel.add(
                field,
                BorderLayout.CENTER
        );

        passwordPanel.add(
                showButton,
                BorderLayout.EAST
        );

        passwordPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        field.addFocusListener(
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

        return passwordPanel;
    }

    // =========================================================
    // CREATE LABEL
    // =========================================================

    private JLabel createLabel(
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
                DARK_TEXT
        );

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

        field.setForeground(
                DARK_TEXT
        );

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
                new Dimension(
                        390,
                        48
                )
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
    // CREATE MAIN BUTTON
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
                        12
                )
        );

        button.setBackground(
                ORANGE
        );

        button.setForeground(
                DARK_TEXT
        );

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
                new Dimension(
                        123,
                        48
                )
        );

        button.setMinimumSize(
                new Dimension(
                        100,
                        48
                )
        );

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        48
                )
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
    // REGISTER PLAYER
    // =========================================================

    private void registerPlayer() {

        String username =
                usernameField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        String confirmPassword =
                new String(
                        confirmPasswordField.getPassword()
                );

        // -----------------------------------------------------
        // USERNAME EMPTY
        // -----------------------------------------------------

        if (username.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a player name.",
                    "Registration Error",
                    JOptionPane.WARNING_MESSAGE
            );

            usernameField.requestFocus();

            return;
        }

        // -----------------------------------------------------
        // USERNAME VALIDATION
        // -----------------------------------------------------

        if (!CredentialRules.isValidUsername(
                username
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    CredentialRules.USERNAME_REQUIREMENTS,
                    "Invalid Player Name",
                    JOptionPane.WARNING_MESSAGE
            );

            usernameField.requestFocus();

            return;
        }

        // -----------------------------------------------------
        // PASSWORD EMPTY
        // -----------------------------------------------------

        if (password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a password.",
                    "Registration Error",
                    JOptionPane.WARNING_MESSAGE
            );

            passwordField.requestFocus();

            return;
        }

        // -----------------------------------------------------
        // PASSWORD VALIDATION
        // -----------------------------------------------------

        if (!CredentialRules.isValidPassword(
                password
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    CredentialRules.PASSWORD_REQUIREMENTS,
                    "Invalid Password",
                    JOptionPane.WARNING_MESSAGE
            );

            passwordField.requestFocus();

            return;
        }

        // -----------------------------------------------------
        // CONFIRM PASSWORD
        // -----------------------------------------------------

        if (confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please confirm your password.",
                    "Registration Error",
                    JOptionPane.WARNING_MESSAGE
            );

            confirmPasswordField.requestFocus();

            return;
        }

        // -----------------------------------------------------
        // PASSWORD MATCH
        // -----------------------------------------------------

        if (!password.equals(
                confirmPassword
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    "Passwords do not match.",
                    "Password Error",
                    JOptionPane.WARNING_MESSAGE
            );

            confirmPasswordField.requestFocus();

            return;
        }

        // -----------------------------------------------------
        // DAO REGISTRATION
        // -----------------------------------------------------

        try {

            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "Starting player registration..."
            );

            System.out.println(
                    "Player Name: "
                            + username
            );

            Player player =
                    playerDao.registerPlayer(
                            username,
                            password,
                            confirmPassword
                    );

            if (player == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Registration failed.\n\n"
                                + "No player account was created.",
                        "Registration Failed",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            System.out.println(
                    "Player registration successful."
            );

            System.out.println(
                    "Player Name: "
                            + player.getName()
            );

            System.out.println(
                    "Starting Credits: "
                            + player.getCredits()
            );

            System.out.println(
                    "===================================="
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Registration successful!\n\n"
                            + "Player Name: "
                            + player.getName()
                            + "\n"
                            + "Starting Credits: "
                            + player.getCredits()
                            + "\n"
                            + "Status: ACTIVE\n\n"
                            + "You can now login.",
                    "Registration Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearFields();

            dispose();

        } catch (SQLException e) {

            e.printStackTrace();

            String message =
                    e.getMessage();

            if (message == null
                    || message.trim().isEmpty()) {

                message =
                        "Unknown database error.";
            }

            // -------------------------------------------------
            // DUPLICATE USERNAME
            // -------------------------------------------------

            if (message.toLowerCase()
                    .contains("already exists")) {

                JOptionPane.showMessageDialog(
                        this,
                        "Player name already exists.\n\n"
                                + "Please choose another name.",
                        "Player Name Exists",
                        JOptionPane.WARNING_MESSAGE
                );

                usernameField.requestFocus();

                return;
            }

            // -------------------------------------------------
            // DATABASE ERROR
            // -------------------------------------------------

            JOptionPane.showMessageDialog(
                    this,
                    "Registration failed.\n\n"
                            + message,
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unexpected error.\n\n"
                            + e.getMessage(),
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // CLEAR
    // =========================================================

    private void clearFields() {

        usernameField.setText("");

        passwordField.setText("");

        confirmPasswordField.setText("");

        usernameField.requestFocusInWindow();
    }

    // =========================================================
    // BACK
    // =========================================================

    private void goBack() {

        dispose();

        SwingUtilities.invokeLater(
                () -> {

                    LoginUi loginUi =
                            new LoginUi();

                    loginUi.setLocationRelativeTo(null);

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

                    PlayerRegistrationUi ui =
                            new PlayerRegistrationUi();

                    ui.setLocationRelativeTo(null);

                    ui.setVisible(true);
                }
        );
    }
}