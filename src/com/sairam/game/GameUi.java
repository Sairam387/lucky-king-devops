package com.sairam.game;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * ============================================================
 * LUCKY KING - MODERN GAME UI
 * ============================================================
 *
 * UI redesign only.
 *
 * Existing game/database logic preserved:
 *
 * Game
 * Player
 * GameHistoryDAO
 * PlayerDao
 * TransactionDao
 * DepositUi
 * WithdrawUi
 * PlayerChangePasswordUi
 * LoginUi
 *
 * ============================================================
 */
public class GameUi extends JFrame {

    private static final long serialVersionUID = 1L;

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color BG = new Color(244, 246, 252);
    private static final Color CARD = Color.WHITE;

    private static final Color PURPLE_DARK = new Color(62, 32, 125);
    private static final Color PURPLE = new Color(112, 72, 220);
    private static final Color PURPLE_LIGHT = new Color(239, 233, 255);

    private static final Color GOLD = new Color(245, 190, 55);
    private static final Color GOLD_DARK = new Color(194, 140, 20);

    private static final Color RED = new Color(224, 65, 75);
    private static final Color RED_LIGHT = new Color(255, 238, 240);

    private static final Color BLUE = new Color(52, 126, 224);
    private static final Color BLUE_LIGHT = new Color(235, 244, 255);

    private static final Color YELLOW = new Color(239, 175, 38);
    private static final Color YELLOW_LIGHT = new Color(255, 248, 225);

    private static final Color GREEN = new Color(35, 155, 88);
    private static final Color GREEN_LIGHT = new Color(232, 249, 239);

    private static final Color TEXT = new Color(38, 39, 55);
    private static final Color TEXT_SECONDARY = new Color(103, 106, 125);
    private static final Color BORDER = new Color(225, 226, 235);

    // =========================================================
    // GAME SETTINGS
    // =========================================================

    private static final int STARTING_CREDITS = 1000;
    private static final int MIN_BET = 10;
    private static final int MAX_BET = 100000;

    private static final double NUMBER_MULTIPLIER = 20.0;
    private static final double YELLOW_MULTIPLIER = 10.0;
    private static final double COLOR_MULTIPLIER = 1.75;

    // =========================================================
    // GAME OBJECTS
    // =========================================================

    private Player player;
    private Game game;
    private GameHistoryDAO dao;
    private PlayerDao playerDAO;
    private TransactionDao transactionDAO;

    // =========================================================
    // UI COMPONENTS
    // =========================================================

    private JLabel playerLabel;
    private JLabel walletLabel;

    private JLabel resultLabel;
    private JLabel resultNumberLabel;
    private JLabel resultColorLabel;

    private JTextField betField;

    private ButtonGroup numberGroup;
    private ButtonGroup colorGroup;

    private JPanel numberPanel;
    private JPanel colorPanel;

    // =========================================================
    // SELECTED VALUES
    // =========================================================

    private int selectedNumber = -1;
    private String selectedColor = null;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public GameUi(String playerName, int credits) {

        player = new Player(playerName, credits);

        game = new Game();
        dao = new GameHistoryDAO();
        playerDAO = new PlayerDao();
        transactionDAO = new TransactionDao();

        setTitle("Lucky King | " + playerName);

        setSize(1200, 850);

        setMinimumSize(
                new Dimension(
                        1050,
                        760
                )
        );

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        createUI();

        updateWallet();
    }

    // =========================================================
    // OLD CONSTRUCTOR
    // =========================================================

    public GameUi(String playerName) {
        this(
                playerName,
                STARTING_CREDITS
        );
    }

    // =========================================================
    // CREATE UI
    // =========================================================

    private void createUI() {

        JPanel root = new JPanel(new BorderLayout());

        root.setBackground(BG);

        root.add(
                createHeader(),
                BorderLayout.NORTH
        );

        root.add(
                createMainContent(),
                BorderLayout.CENTER
        );

        root.add(
                createBottomMenu(),
                BorderLayout.SOUTH
        );

        setContentPane(root);
    }

    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeader() {

        JPanel header = new JPanel(
                new BorderLayout()
        );

        header.setBackground(PURPLE_DARK);

        header.setBorder(
                new EmptyBorder(
                        18,
                        24,
                        18,
                        24
                )
        );

        // -----------------------------------------------------
        // BRAND
        // -----------------------------------------------------

        JPanel brand = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        12,
                        0
                )
        );

        brand.setOpaque(false);

        JLabel crown = new JLabel("♛");

        crown.setForeground(GOLD);

        crown.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        42
                )
        );

        brand.add(crown);

        JPanel brandText = new JPanel();

        brandText.setOpaque(false);

        brandText.setLayout(
                new BoxLayout(
                        brandText,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel logo = new JLabel(
                "LUCKY KING"
        );

        logo.setForeground(Color.WHITE);

        logo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        JLabel subtitle = new JLabel(
                "NUMBER • COLOR • LUCK"
        );

        subtitle.setForeground(
                new Color(
                        214,
                        205,
                        240
                )
        );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        10
                )
        );

        brandText.add(logo);

        brandText.add(
                Box.createVerticalStrut(3)
        );

        brandText.add(subtitle);

        brand.add(brandText);

        header.add(
                brand,
                BorderLayout.WEST
        );

        // -----------------------------------------------------
        // CENTER WELCOME
        // -----------------------------------------------------

        JPanel welcomePanel = new JPanel();

        welcomePanel.setOpaque(false);

        welcomePanel.setLayout(
                new BoxLayout(
                        welcomePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel welcome = new JLabel(
                "WELCOME BACK"
        );

        welcome.setForeground(GOLD);

        welcome.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        welcome.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        playerLabel = new JLabel(
                player.getName()
        );

        playerLabel.setForeground(Color.WHITE);

        playerLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        19
                )
        );

        playerLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        welcomePanel.add(welcome);

        welcomePanel.add(
                Box.createVerticalStrut(4)
        );

        welcomePanel.add(playerLabel);

        header.add(
                welcomePanel,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // WALLET
        // -----------------------------------------------------

        JPanel wallet = new JPanel(
                new BorderLayout(
                        10,
                        0
                )
        );

        wallet.setBackground(
                new Color(
                        255,
                        255,
                        255,
                        25
                )
        );

        wallet.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                new Color(
                                        255,
                                        255,
                                        255,
                                        70
                                ),
                                1
                        ),
                        new EmptyBorder(
                                9,
                                13,
                                9,
                                15
                        )
                )
        );

        JLabel walletIcon = new JLabel("₹");

        walletIcon.setForeground(GOLD);

        walletIcon.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        27
                )
        );

        wallet.add(
                walletIcon,
                BorderLayout.WEST
        );

        JPanel walletText = new JPanel();

        walletText.setOpaque(false);

        walletText.setLayout(
                new BoxLayout(
                        walletText,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel walletTitle = new JLabel(
                "WALLET BALANCE"
        );

        walletTitle.setForeground(
                new Color(
                        220,
                        215,
                        238
                )
        );

        walletTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        9
                )
        );

        walletLabel = new JLabel();

        walletLabel.setForeground(GOLD);

        walletLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        19
                )
        );

        walletText.add(walletTitle);

        walletText.add(
                Box.createVerticalStrut(3)
        );

        walletText.add(walletLabel);

        wallet.add(
                walletText,
                BorderLayout.CENTER
        );

        header.add(
                wallet,
                BorderLayout.EAST
        );

        return header;
    }

    // =========================================================
    // MAIN CONTENT
    // =========================================================

    private JPanel createMainContent() {

        JPanel main = new JPanel(
                new BorderLayout(
                        16,
                        0
                )
        );

        main.setBackground(BG);

        main.setBorder(
                new EmptyBorder(
                        18,
                        22,
                        15,
                        22
                )
        );

        main.add(
                createGameArea(),
                BorderLayout.CENTER
        );

        main.add(
                createQuickPanel(),
                BorderLayout.EAST
        );

        return main;
    }

    // =========================================================
    // GAME AREA
    // =========================================================

    private JPanel createGameArea() {

        JPanel area = new JPanel(
                new BorderLayout(
                        0,
                        14
                )
        );

        area.setOpaque(false);

        area.add(
                createGameTitle(),
                BorderLayout.NORTH
        );

        area.add(
                createSelectionCard(),
                BorderLayout.CENTER
        );

        return area;
    }

    // =========================================================
    // GAME TITLE
    // =========================================================

    private JPanel createGameTitle() {

        JPanel panel = new JPanel(
                new BorderLayout()
        );

        panel.setBackground(CARD);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                16,
                                20,
                                16,
                                20
                        )
                )
        );

        JPanel text = new JPanel();

        text.setOpaque(false);

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title = new JLabel(
                "NUMBER & COLOR GAME"
        );

        title.setForeground(PURPLE_DARK);

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        JLabel subtitle = new JLabel(
                "Select your lucky number or color and place your bet."
        );

        subtitle.setForeground(TEXT_SECONDARY);

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        text.add(title);

        text.add(
                Box.createVerticalStrut(4)
        );

        text.add(subtitle);

        panel.add(
                text,
                BorderLayout.WEST
        );

        JPanel multipliers = new JPanel(
                new GridLayout(
                        1,
                        4,
                        7,
                        0
                )
        );

        multipliers.setOpaque(false);

        multipliers.add(
                createMultiplier(
                        "NUMBER",
                        "20x",
                        PURPLE
                )
        );

        multipliers.add(
                createMultiplier(
                        "RED",
                        "1.75x",
                        RED
                )
        );

        multipliers.add(
                createMultiplier(
                        "BLUE",
                        "1.75x",
                        BLUE
                )
        );

        multipliers.add(
                createMultiplier(
                        "YELLOW",
                        "10x",
                        YELLOW
                )
        );

        panel.add(
                multipliers,
                BorderLayout.EAST
        );

        return panel;
    }

    // =========================================================
    // MULTIPLIER
    // =========================================================

    private JPanel createMultiplier(
            String name,
            String value,
            Color color
    ) {

        JPanel panel = new JPanel(
                new GridLayout(
                        2,
                        1
                )
        );

        panel.setBackground(
                new Color(
                        249,
                        249,
                        253
                )
        );

        panel.setBorder(
                new LineBorder(
                        BORDER,
                        1
                )
        );

        JLabel nameLabel = new JLabel(
                name,
                SwingConstants.CENTER
        );

        nameLabel.setForeground(color);

        nameLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        9
                )
        );

        JLabel valueLabel = new JLabel(
                value,
                SwingConstants.CENTER
        );

        valueLabel.setForeground(TEXT);

        valueLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        panel.add(nameLabel);
        panel.add(valueLabel);

        return panel;
    }

    // =========================================================
    // SELECTION CARD
    // =========================================================

    private JPanel createSelectionCard() {

        JPanel card = new JPanel(
                new BorderLayout(
                        0,
                        10
                )
        );

        card.setBackground(CARD);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                18,
                                20,
                                18,
                                20
                        )
                )
        );

        card.add(
                createSelectionArea(),
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // SELECTION AREA
    // =========================================================

    private JPanel createSelectionArea() {

        JPanel container = new JPanel();

        container.setOpaque(false);

        container.setLayout(
                new BoxLayout(
                        container,
                        BoxLayout.Y_AXIS
                )
        );

        // -----------------------------------------------------
        // NUMBER TITLE
        // -----------------------------------------------------

        container.add(
                createStepLabel(
                        "01",
                        "SELECT YOUR LUCKY NUMBER"
                )
        );

        container.add(
                Box.createVerticalStrut(8)
        );

        numberPanel = new JPanel(
                new GridLayout(
                        2,
                        5,
                        9,
                        9
                )
        );

        numberPanel.setOpaque(false);

        numberGroup = new ButtonGroup();

        for (int i = 0; i <= 9; i++) {

            final int number = i;

            JRadioButton button =
                    createNumberButton(
                            String.valueOf(i)
                    );

            button.addActionListener(
                    e -> {

                        selectedNumber = number;

                        selectedColor = null;

                        if (colorGroup != null) {
                            colorGroup.clearSelection();
                        }

                        resultLabel.setText(
                                "NUMBER " + number + " SELECTED"
                        );

                        resultLabel.setForeground(
                                PURPLE
                        );
                    }
            );

            numberGroup.add(button);

            numberPanel.add(button);
        }

        container.add(numberPanel);

        container.add(
                Box.createVerticalStrut(15)
        );

        // -----------------------------------------------------
        // COLOR TITLE
        // -----------------------------------------------------

        container.add(
                createStepLabel(
                        "02",
                        "OR CHOOSE A COLOR"
                )
        );

        container.add(
                Box.createVerticalStrut(8)
        );

        colorPanel = new JPanel(
                new GridLayout(
                        1,
                        3,
                        9,
                        0
                )
        );

        colorPanel.setOpaque(false);

        colorGroup = new ButtonGroup();

        addColorButton(
                colorPanel,
                "RED",
                RED,
                RED_LIGHT
        );

        addColorButton(
                colorPanel,
                "BLUE",
                BLUE,
                BLUE_LIGHT
        );

        addColorButton(
                colorPanel,
                "YELLOW",
                YELLOW,
                YELLOW_LIGHT
        );

        container.add(colorPanel);

        container.add(
                Box.createVerticalStrut(15)
        );

        // -----------------------------------------------------
        // BET TITLE
        // -----------------------------------------------------

        container.add(
                createStepLabel(
                        "03",
                        "ENTER BET AMOUNT"
                )
        );

        container.add(
                Box.createVerticalStrut(8)
        );

        container.add(
                createBetSection()
        );

        container.add(
                Box.createVerticalStrut(12)
        );

        // -----------------------------------------------------
        // PLAY SECTION
        // -----------------------------------------------------

        container.add(
                createPlaySection()
        );

        container.add(
                Box.createVerticalStrut(12)
        );

        // -----------------------------------------------------
        // RESULT
        // -----------------------------------------------------

        container.add(
                createResultPanel()
        );

        return container;
    }

    // =========================================================
    // STEP LABEL
    // =========================================================

    private JPanel createStepLabel(
            String number,
            String text
    ) {

        JPanel panel = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        0,
                        0
                )
        );

        panel.setOpaque(false);

        JLabel numberLabel = new JLabel(
                number
        );

        numberLabel.setForeground(GOLD_DARK);

        numberLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        JLabel textLabel = new JLabel(
                "   " + text
        );

        textLabel.setForeground(TEXT);

        textLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        panel.add(numberLabel);

        panel.add(textLabel);

        return panel;
    }

    // =========================================================
    // NUMBER BUTTON
    // =========================================================

    private JRadioButton createNumberButton(
            String text
    ) {

        JRadioButton button =
                new JRadioButton(text);

        button.setPreferredSize(
                new Dimension(
                        70,
                        48
                )
        );

        button.setMinimumSize(
                new Dimension(
                        50,
                        42
                )
        );

        button.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        19
                )
        );

        button.setForeground(PURPLE_DARK);

        button.setBackground(
                new Color(
                        250,
                        249,
                        254
                )
        );

        button.setFocusPainted(false);

        button.setOpaque(true);

        button.setBorder(
                new LineBorder(
                        new Color(
                                218,
                                213,
                                232
                        ),
                        1
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

                        if (!button.isSelected()) {

                            button.setBackground(
                                    PURPLE_LIGHT
                            );
                        }
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        if (!button.isSelected()) {

                            button.setBackground(
                                    new Color(
                                            250,
                                            249,
                                            254
                                    )
                            );
                        }
                    }
                }
        );

        return button;
    }

    // =========================================================
    // COLOR BUTTON
    // =========================================================

    private void addColorButton(
            JPanel panel,
            String colorName,
            Color color,
            Color lightColor
    ) {

        JRadioButton button =
                new JRadioButton(
                        colorName
                );

        button.setPreferredSize(
                new Dimension(
                        120,
                        46
                )
        );

        button.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(color);

        button.setBackground(
                Color.WHITE
        );

        button.setFocusPainted(false);

        button.setOpaque(true);

        button.setBorder(
                new LineBorder(
                        BORDER,
                        1
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.addActionListener(
                e -> {

                    selectedColor = colorName;

                    selectedNumber = -1;

                    if (numberGroup != null) {
                        numberGroup.clearSelection();
                    }

                    resultLabel.setText(
                            colorName + " SELECTED"
                    );

                    resultLabel.setForeground(color);

                    button.setBackground(
                            lightColor
                    );
                }
        );

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                lightColor
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        if (!button.isSelected()) {

                            button.setBackground(
                                    Color.WHITE
                            );
                        }
                    }
                }
        );

        colorGroup.add(button);

        panel.add(button);
    }

    // =========================================================
    // BET SECTION
    // =========================================================

    private JPanel createBetSection() {

        JPanel panel = new JPanel(
                new BorderLayout(
                        10,
                        0
                )
        );

        panel.setOpaque(false);

        JPanel inputPanel = new JPanel(
                new BorderLayout()
        );

        inputPanel.setBackground(
                new Color(
                        250,
                        249,
                        253
                )
        );

        inputPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                PURPLE,
                                1
                        ),
                        new EmptyBorder(
                                0,
                                10,
                                0,
                                10
                        )
                )
        );

        JLabel rupee = new JLabel("₹");

        rupee.setForeground(PURPLE);

        rupee.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        inputPanel.add(
                rupee,
                BorderLayout.WEST
        );

        betField = new JTextField(
                String.valueOf(
                        MIN_BET
                )
        );

        betField.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        betField.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        betField.setForeground(TEXT);

        betField.setBackground(
                new Color(
                        250,
                        249,
                        253
                )
        );

        betField.setBorder(
                null
        );

        inputPanel.add(
                betField,
                BorderLayout.CENTER
        );

        inputPanel.setPreferredSize(
                new Dimension(
                        190,
                        46
                )
        );

        panel.add(
                inputPanel,
                BorderLayout.WEST
        );

        JPanel quickBets = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        6,
                        0
                )
        );

        quickBets.setOpaque(false);

        quickBets.add(
                createQuickBetButton(
                        "₹10",
                        10
                )
        );

        quickBets.add(
                createQuickBetButton(
                        "₹50",
                        50
                )
        );

        quickBets.add(
                createQuickBetButton(
                        "₹100",
                        100
                )
        );

        quickBets.add(
                createQuickBetButton(
                        "₹500",
                        500
                )
        );

        panel.add(
                quickBets,
                BorderLayout.CENTER
        );

        JLabel limits = new JLabel(
                "Min 10 • Max 100,000"
        );

        limits.setForeground(
                TEXT_SECONDARY
        );

        limits.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        10
                )
        );

        panel.add(
                limits,
                BorderLayout.EAST
        );

        return panel;
    }

    // =========================================================
    // QUICK BET BUTTON
    // =========================================================

    private JButton createQuickBetButton(
            String text,
            int amount
    ) {

        JButton button = new JButton(text);

        button.setPreferredSize(
                new Dimension(
                        58,
                        34
                )
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        10
                )
        );

        button.setForeground(PURPLE);

        button.setBackground(
                PURPLE_LIGHT
        );

        button.setFocusPainted(false);

        button.setBorder(
                new LineBorder(
                        new Color(
                                220,
                                211,
                                245
                        ),
                        1
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.addActionListener(
                e -> betField.setText(
                        String.valueOf(amount)
                )
        );

        return button;
    }

    // =========================================================
    // PLAY SECTION
    // =========================================================

    private JPanel createPlaySection() {

        JPanel panel = new JPanel(
                new BorderLayout(
                        15,
                        0
                )
        );

        panel.setBackground(
                new Color(
                        249,
                        248,
                        253
                )
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                10,
                                14,
                                10,
                                14
                        )
                )
        );

        resultLabel = new JLabel(
                "Choose a number or color to play"
        );

        resultLabel.setForeground(
                TEXT_SECONDARY
        );

        resultLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        panel.add(
                resultLabel,
                BorderLayout.CENTER
        );

        JButton playButton = new JButton(
                "♛  PLAY NOW"
        );

        playButton.setPreferredSize(
                new Dimension(
                        190,
                        45
                )
        );

        playButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        playButton.setForeground(
                Color.WHITE
        );

        playButton.setBackground(
                PURPLE
        );

        playButton.setFocusPainted(false);

        playButton.setBorder(
                new LineBorder(
                        PURPLE_DARK,
                        1
                )
        );

        playButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        playButton.addActionListener(
                e -> playGame()
        );

        playButton.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        playButton.setBackground(
                                PURPLE_DARK
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        playButton.setBackground(
                                PURPLE
                        );
                    }
                }
        );

        panel.add(
                playButton,
                BorderLayout.EAST
        );

        return panel;
    }

    // =========================================================
    // RESULT PANEL
    // =========================================================

    private JPanel createResultPanel() {

        JPanel panel = new JPanel(
                new BorderLayout(
                        15,
                        0
                )
        );

        panel.setBackground(
                new Color(
                        248,
                        247,
                        252
                )
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                11,
                                15,
                                11,
                                15
                        )
                )
        );

        JPanel latest = new JPanel();

        latest.setOpaque(false);

        latest.setLayout(
                new BoxLayout(
                        latest,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title = new JLabel(
                "LATEST RESULT"
        );

        title.setForeground(
                TEXT_SECONDARY
        );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        9
                )
        );

        resultNumberLabel = new JLabel(
                "—"
        );

        resultNumberLabel.setForeground(
                PURPLE_DARK
        );

        resultNumberLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        latest.add(title);

        latest.add(
                Box.createVerticalStrut(2)
        );

        latest.add(
                resultNumberLabel
        );

        panel.add(
                latest,
                BorderLayout.WEST
        );

        resultColorLabel = new JLabel(
                "WAITING FOR NEXT GAME",
                SwingConstants.CENTER
        );

        resultColorLabel.setForeground(
                TEXT_SECONDARY
        );

        resultColorLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        panel.add(
                resultColorLabel,
                BorderLayout.CENTER
        );

        JLabel status = new JLabel(
                "LUCKY KING"
        );

        status.setForeground(
                GOLD_DARK
        );

        status.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        10
                )
        );

        panel.add(
                status,
                BorderLayout.EAST
        );

        return panel;
    }

    // =========================================================
    // QUICK PANEL
    // =========================================================

    private JPanel createQuickPanel() {

        JPanel panel = new JPanel(
                new BorderLayout(
                        0,
                        15
                )
        );

        panel.setBackground(CARD);

        panel.setPreferredSize(
                new Dimension(
                        220,
                        0
                )
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                18,
                                15,
                                15,
                                15
                        )
                )
        );

        // -----------------------------------------------------
        // TITLE
        // -----------------------------------------------------

        JPanel title = new JPanel();

        title.setOpaque(false);

        title.setLayout(
                new BoxLayout(
                        title,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel heading = new JLabel(
                "QUICK ACTIONS"
        );

        heading.setForeground(
                PURPLE_DARK
        );

        heading.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        JLabel subtitle = new JLabel(
                "Wallet & account"
        );

        subtitle.setForeground(
                TEXT_SECONDARY
        );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        10
                )
        );

        title.add(heading);

        title.add(
                Box.createVerticalStrut(3)
        );

        title.add(subtitle);

        panel.add(
                title,
                BorderLayout.NORTH
        );

        // -----------------------------------------------------
        // ACTIONS
        // -----------------------------------------------------

        JPanel actions = new JPanel(
                new GridLayout(
                        4,
                        1,
                        0,
                        9
                )
        );

        actions.setOpaque(false);

        actions.add(
                createActionButton(
                        "＋  DEPOSIT",
                        BLUE,
                        BLUE_LIGHT,
                        e -> openDeposit()
                )
        );

        actions.add(
                createActionButton(
                        "↗  WITHDRAW",
                        PURPLE,
                        PURPLE_LIGHT,
                        e -> openWithdraw()
                )
        );

        actions.add(
                createActionButton(
                        "▣  TRANSACTIONS",
                        TEXT,
                        new Color(
                                247,
                                247,
                                250
                        ),
                        e -> showWalletTransactions()
                )
        );

        actions.add(
                createActionButton(
                        "▤  MY HISTORY",
                        TEXT,
                        new Color(
                                247,
                                247,
                                250
                        ),
                        e -> showMyHistory()
                )
        );

        panel.add(
                actions,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // RESPONSIBLE PLAY
        // -----------------------------------------------------

        JPanel tip = new JPanel(
                new BorderLayout()
        );

        tip.setBackground(
                new Color(
                        249,
                        247,
                        253
                )
        );

        tip.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                10,
                                10,
                                10,
                                10
                        )
                )
        );

        JLabel tipLabel = new JLabel(
                "<html>"
                        + "<b>♛ LUCKY KING</b><br>"
                        + "<font color='#666A7D'>"
                        + "Choose your number or color "
                        + "and enjoy the game."
                        + "</font>"
                        + "</html>"
        );

        tipLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        10
                )
        );

        tip.add(
                tipLabel,
                BorderLayout.CENTER
        );

        panel.add(
                tip,
                BorderLayout.SOUTH
        );

        return panel;
    }

    // =========================================================
    // ACTION BUTTON
    // =========================================================

    private JButton createActionButton(
            String text,
            Color foreground,
            Color background,
            java.awt.event.ActionListener listener
    ) {

        JButton button = new JButton(text);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        button.setForeground(
                foreground
        );

        button.setBackground(
                background
        );

        button.setFocusPainted(false);

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                0,
                                12,
                                0,
                                8
                        )
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.addActionListener(listener);

        Color original =
                background;

        Color hover =
                background.brighter();

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                hover
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                original
                        );
                    }
                }
        );

        return button;
    }

    // =========================================================
    // BOTTOM MENU
    // =========================================================

    private JPanel createBottomMenu() {

        JPanel outer = new JPanel(
                new BorderLayout()
        );

        outer.setBackground(
                Color.WHITE
        );

        outer.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                9,
                                15,
                                9,
                                15
                        )
                )
        );

        JPanel left = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        7,
                        0
                )
        );

        left.setOpaque(false);

        addMenuButton(
                left,
                "STATISTICS",
                e -> showStatistics()
        );

        addMenuButton(
                left,
                "MY STATISTICS",
                e -> showMyStatistics()
        );

        addMenuButton(
                left,
                "LEADERBOARD",
                e -> showLeaderboard()
        );

        JPanel right = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT,
                        7,
                        0
                )
        );

        right.setOpaque(false);

        addMenuButton(
                right,
                "RESET",
                e -> resetGame()
        );

        addMenuButton(
                right,
                "PASSWORD",
                e -> openChangePassword()
        );

        addMenuButton(
                right,
                "LOGOUT",
                e -> logout()
        );

        outer.add(
                left,
                BorderLayout.WEST
        );

        outer.add(
                right,
                BorderLayout.EAST
        );

        return outer;
    }

    // =========================================================
    // MENU BUTTON
    // =========================================================

    private void addMenuButton(
            JPanel panel,
            String text,
            java.awt.event.ActionListener listener
    ) {

        JButton button =
                createMenuButton(text);

        button.addActionListener(listener);

        panel.add(button);
    }

    // =========================================================
    // CREATE MENU BUTTON
    // =========================================================

    private JButton createMenuButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setPreferredSize(
                new Dimension(
                        120,
                        32
                )
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        9
                )
        );

        button.setForeground(
                PURPLE_DARK
        );

        button.setBackground(
                new Color(
                        249,
                        248,
                        253
                )
        );

        button.setFocusPainted(false);

        button.setBorder(
                new LineBorder(
                        BORDER,
                        1
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
                                PURPLE_LIGHT
                        );

                        button.setForeground(
                                PURPLE
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                new Color(
                                        249,
                                        248,
                                        253
                                )
                        );

                        button.setForeground(
                                PURPLE_DARK
                        );
                    }
                }
        );

        return button;
    }

    // =========================================================
    // PLAY GAME
    // =========================================================

    private void playGame() {

        // -----------------------------------------------------
        // SELECTION VALIDATION
        // -----------------------------------------------------

        if (
                selectedNumber == -1
                        && selectedColor == null
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a number or color.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // -----------------------------------------------------
        // BET VALIDATION
        // -----------------------------------------------------

        String betText =
                betField.getText().trim();

        if (betText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter bet amount.",
                    "Bet Required",
                    JOptionPane.WARNING_MESSAGE
            );

            betField.requestFocus();

            return;
        }

        int betAmount;

        try {

            betAmount =
                    Integer.parseInt(
                            betText
                    );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid whole number.",
                    "Invalid Bet",
                    JOptionPane.WARNING_MESSAGE
            );

            betField.requestFocus();

            return;
        }

        // -----------------------------------------------------
        // MINIMUM
        // -----------------------------------------------------

        if (betAmount < MIN_BET) {

            JOptionPane.showMessageDialog(
                    this,
                    "Minimum bet is "
                            + MIN_BET
                            + " credits.",
                    "Invalid Bet",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // -----------------------------------------------------
        // MAXIMUM
        // -----------------------------------------------------

        if (betAmount > MAX_BET) {

            JOptionPane.showMessageDialog(
                    this,
                    "Maximum bet is "
                            + MAX_BET
                            + " credits.",
                    "Invalid Bet",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // -----------------------------------------------------
        // WALLET
        // -----------------------------------------------------

        if (betAmount > player.getCredits()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Insufficient credits.\n\n"
                            + "Current Wallet: "
                            + player.getCredits()
                            + " credits",
                    "Insufficient Credits",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // -----------------------------------------------------
        // GENERATE RESULT
        // -----------------------------------------------------

        Result result =
                game.generateResult();

        int resultNumber =
                result.getNumber();

        String resultColor =
                result.getColor();

        boolean win = false;

        String selectedType;
        String selectedValue;

        // -----------------------------------------------------
        // NUMBER BET
        // -----------------------------------------------------

        if (selectedNumber != -1) {

            selectedType = "NUMBER";

            selectedValue =
                    String.valueOf(
                            selectedNumber
                    );

            if (
                    selectedNumber
                            == resultNumber
            ) {

                win = true;
            }

        } else {

            // -------------------------------------------------
            // COLOR BET
            // -------------------------------------------------

            selectedType = "COLOR";

            selectedValue =
                    selectedColor;

            if (
                    selectedColor != null
                            && selectedColor.equals(
                            resultColor
                    )
            ) {

                win = true;
            }
        }

        // -----------------------------------------------------
        // MULTIPLIER
        // -----------------------------------------------------

        double multiplier;

        if (
                selectedType.equals(
                        "NUMBER"
                )
        ) {

            multiplier =
                    NUMBER_MULTIPLIER;

        } else if (
                "YELLOW".equalsIgnoreCase(
                        selectedValue
                )
        ) {

            multiplier =
                    YELLOW_MULTIPLIER;

        } else {

            multiplier =
                    COLOR_MULTIPLIER;
        }

        // -----------------------------------------------------
        // PAYOUT
        // -----------------------------------------------------

        int payout = 0;

        if (win) {

            payout =
                    (int) Math.round(
                            betAmount
                                    * multiplier
                    );
        }

        // -----------------------------------------------------
        // STATUS
        // -----------------------------------------------------

        String status =
                win
                        ? "WON"
                        : "LOST";

        int balanceBefore =
                player.getCredits();

        // -----------------------------------------------------
        // DATABASE TRANSACTION
        //
        // PRESERVED EXACTLY
        //
        // GameHistoryDAO handles:
        //
        // 1. Wallet lock
        // 2. Balance validation
        // 3. Wallet update
        // 4. Game history
        // 5. Wallet transaction
        // 6. Commit
        //
        // DO NOT ADD:
        //
        // transactionDAO.recordGameEntry()
        // transactionDAO.recordGameWin()
        //
        // -----------------------------------------------------

        try {

            int finalCredits =
                    dao.saveGameWithWalletUpdate(
                            player.getName(),
                            selectedType,
                            selectedValue,
                            resultNumber,
                            resultColor,
                            betAmount,
                            payout,
                            status
                    );

            player =
                    new Player(
                            player.getName(),
                            finalCredits
                    );

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Game transaction failed.\n\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // -----------------------------------------------------
        // SHOW RESULT
        // -----------------------------------------------------

        showResult(
                resultNumber,
                resultColor,
                win,
                payout
        );

        updateWallet();

        // -----------------------------------------------------
        // RESULT MESSAGE
        // -----------------------------------------------------

        String message;

        if (win) {

            message =
                    "🎉 YOU WON! 🎉\n\n"
                            + "Result: "
                            + resultNumber
                            + " - "
                            + resultColor
                            + "\n\n"
                            + "Bet: "
                            + betAmount
                            + " credits\n"
                            + "Multiplier: "
                            + multiplier
                            + "x\n"
                            + "Payout: "
                            + payout
                            + " credits\n\n"
                            + "Balance Before: "
                            + balanceBefore
                            + "\n"
                            + "Balance After: "
                            + player.getCredits();

        } else {

            message =
                    "BETTER LUCK NEXT TIME!\n\n"
                            + "Result: "
                            + resultNumber
                            + " - "
                            + resultColor
                            + "\n\n"
                            + "Bet: "
                            + betAmount
                            + " credits\n"
                            + "Payout: 0 credits\n\n"
                            + "Balance Before: "
                            + balanceBefore
                            + "\n"
                            + "Balance After: "
                            + player.getCredits();
        }

        JOptionPane.showMessageDialog(
                this,
                message,
                win
                        ? "YOU WON!"
                        : "GAME RESULT",
                win
                        ? JOptionPane.INFORMATION_MESSAGE
                        : JOptionPane.WARNING_MESSAGE
        );

        // -----------------------------------------------------
        // RESET
        // -----------------------------------------------------

        resetSelection();

        // -----------------------------------------------------
        // EMPTY WALLET
        // -----------------------------------------------------

        if (player.getCredits() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Your wallet is empty.\n\n"
                            + "Please deposit credits to continue playing.",
                    "Wallet Empty",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    // =========================================================
    // RESET SELECTION
    // =========================================================

    private void resetSelection() {

        selectedNumber = -1;

        selectedColor = null;

        if (numberGroup != null) {
            numberGroup.clearSelection();
        }

        if (colorGroup != null) {
            colorGroup.clearSelection();
        }

        if (betField != null) {

            betField.setText(
                    String.valueOf(
                            MIN_BET
                    )
            );
        }

        if (resultLabel != null) {

            resultLabel.setText(
                    "Choose a number or color to play"
            );

            resultLabel.setForeground(
                    TEXT_SECONDARY
            );
        }
    }

    // =========================================================
    // SHOW RESULT
    // =========================================================

    private void showResult(
            int number,
            String color,
            boolean win,
            int payout
    ) {

        if (win) {

            resultLabel.setText(
                    "🎉 YOU WON!  +"
                            + payout
                            + " CREDITS"
            );

            resultLabel.setForeground(
                    GREEN
            );

        } else {

            resultLabel.setText(
                    "RESULT • BET LOST"
            );

            resultLabel.setForeground(
                    RED
            );
        }

        resultNumberLabel.setText(
                "Number: " + number
        );

        resultColorLabel.setText(
                "Color: " + color
        );

        if (
                "RED".equalsIgnoreCase(
                        color
                )
        ) {

            resultColorLabel.setForeground(
                    RED
            );

        } else if (
                "BLUE".equalsIgnoreCase(
                        color
                )
        ) {

            resultColorLabel.setForeground(
                    BLUE
            );

        } else {

            resultColorLabel.setForeground(
                    YELLOW_DARK()
            );
        }
    }

    // =========================================================
    // YELLOW TEXT COLOR
    // =========================================================

    private Color YELLOW_DARK() {

        return new Color(
                195,
                137,
                15
        );
    }

    // =========================================================
    // UPDATE WALLET
    // =========================================================

    private void updateWallet() {

        if (walletLabel != null) {

            walletLabel.setText(
                    "₹ "
                            + player.getCredits()
                            + " Credits"
            );
        }
    }

    // =========================================================
    // DEPOSIT
    // =========================================================

    private void openDeposit() {

        try {

            DepositUi depositUi =
                    new DepositUi(
                            player.getName()
                    );

            depositUi.setLocationRelativeTo(
                    this
            );

            depositUi.setVisible(true);

            /*
             * DepositUi creates a PENDING
             * deposit request.
             *
             * Credits are not directly
             * added here.
             *
             * Admin approval updates
             * player credits.
             */

            refreshPlayerCredits();

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open Deposit.\n\n"
                            + e.getMessage(),
                    "Deposit Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // REFRESH PLAYER CREDITS
    // =========================================================

    private void refreshPlayerCredits() {

        try {

            Player updatedPlayer =
                    playerDAO.getOrCreatePlayer(
                            player.getName()
                    );

            if (updatedPlayer != null) {

                player =
                        new Player(
                                updatedPlayer.getName(),
                                updatedPlayer.getCredits()
                        );

                updateWallet();

                if (playerLabel != null) {

                    playerLabel.setText(
                            player.getName()
                    );
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to refresh wallet balance.\n\n"
                            + e.getMessage(),
                    "Wallet Refresh Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // GAME HISTORY
    // =========================================================

    private void showHistory() {

        try {

            List<Object[]> data =
                    dao.getHistoryTable();

            showHistoryTable(
                    data,
                    "GAME HISTORY"
            );

        } catch (SQLException e) {

            showDatabaseError(e);
        }
    }

    // =========================================================
    // MY HISTORY
    // =========================================================

    private void showMyHistory() {

        try {

            List<Object[]> data =
                    dao.getPlayerHistory(
                            player.getName()
                    );

            showHistoryTable(
                    data,
                    "MY HISTORY - "
                            + player.getName()
            );

        } catch (SQLException e) {

            showDatabaseError(e);
        }
    }

    // =========================================================
    // HISTORY TABLE
    // =========================================================

    private void showHistoryTable(
            List<Object[]> data,
            String title
    ) {

        if (
                data == null
                        || data.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "No game history available.",
                    title,
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        String[] columns = {
                "Game ID",
                "Player",
                "Selected",
                "Result",
                "Bet",
                "Payout",
                "Status",
                "Time"
        };

        DefaultTableModel model =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };

        for (Object[] sourceRow : data) {

            if (
                    sourceRow == null
                            || sourceRow.length
                            < columns.length
            ) {
                continue;
            }

            model.addRow(sourceRow);
        }

        JTable table =
                new JTable(model);

        styleTable(table);

        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.setPreferredSize(
                new Dimension(
                        950,
                        460
                )
        );

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================================================
    // WALLET TRANSACTIONS
    // =========================================================

    private void showWalletTransactions() {

        try {

            List<Transaction> transactions =
                    transactionDAO.getPlayerTransactions(
                            player.getName()
                    );

            showTransactionTable(
                    transactions,
                    "WALLET TRANSACTIONS - "
                            + player.getName()
            );

        } catch (SQLException e) {

            showDatabaseError(e);
        }
    }

    // =========================================================
    // TRANSACTION TABLE
    // =========================================================

    private void showTransactionTable(
            List<Transaction> transactions,
            String title
    ) {

        if (
                transactions == null
                        || transactions.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "No wallet transactions available.",
                    title,
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        String[] columns = {
                "Transaction ID",
                "Player",
                "Type",
                "Amount",
                "Date & Time"
        };

        DefaultTableModel model =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };

        SimpleDateFormat dateFormat =
                new SimpleDateFormat(
                        "dd-MM-yyyy HH:mm:ss"
                );

        for (
                Transaction transaction :
                transactions
        ) {

            Object[] row =
                    new Object[5];

            row[0] =
                    transaction.getTransactionId();

            row[1] =
                    transaction.getPlayerName();

            row[2] =
                    transaction.getTransactionType();

            row[3] =
                    transaction.getAmount();

            Timestamp timestamp =
                    transaction.getTransactionDate();

            row[4] =
                    timestamp != null
                            ? dateFormat.format(timestamp)
                            : "";

            model.addRow(row);
        }

        JTable table =
                new JTable(model);

        styleTable(table);

        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.setPreferredSize(
                new Dimension(
                        800,
                        450
                )
        );

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================================================
    // TABLE STYLE
    // =========================================================

    private void styleTable(
            JTable table
    ) {

        table.setRowHeight(30);

        table.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        table.setForeground(TEXT);

        table.setGridColor(
                new Color(
                        228,
                        228,
                        237
                )
        );

        table.setSelectionBackground(
                PURPLE_LIGHT
        );

        table.setSelectionForeground(
                TEXT
        );

        table.setAutoCreateRowSorter(true);

        table.setFillsViewportHeight(true);

        table.getTableHeader().setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        table.getTableHeader().setBackground(
                PURPLE_DARK
        );

        table.getTableHeader().setForeground(
                Color.WHITE
        );

        table.getTableHeader().setPreferredSize(
                new Dimension(
                        0,
                        35
                )
        );

        DefaultTableCellRenderer center =
                new DefaultTableCellRenderer();

        center.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        for (
                int i = 0;
                i < table.getColumnCount();
                i++
        ) {

            table.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(center);
        }
    }

    // =========================================================
    // ALL STATISTICS
    // =========================================================

    private void showStatistics() {

        try {

            Object[] statistics =
                    dao.getStatistics();

            if (
                    statistics == null
                            || statistics.length < 7
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid statistics data returned from database.",
                        "Statistics Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            String message =
                    "TOTAL GAMES : "
                            + getIntValue(
                            statistics,
                            0
                    )
                            + "\n\n"
                            + "WINS : "
                            + getIntValue(
                            statistics,
                            1
                    )
                            + "\n\n"
                            + "LOSSES : "
                            + getIntValue(
                            statistics,
                            2
                    )
                            + "\n\n"
                            + "WIN RATE : "
                            + String.format(
                            "%.2f%%",
                            getDoubleValue(
                                    statistics,
                                    3
                            )
                    )
                            + "\n\n"
                            + "TOTAL BET : "
                            + getIntValue(
                            statistics,
                            4
                    )
                            + "\n\n"
                            + "TOTAL PAYOUT : "
                            + getIntValue(
                            statistics,
                            5
                    )
                            + "\n\n"
                            + "PROFIT / LOSS : "
                            + getIntValue(
                            statistics,
                            6
                    );

            JOptionPane.showMessageDialog(
                    this,
                    message,
                    "STATISTICS",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            showDatabaseError(e);
        }
    }

    // =========================================================
    // MY STATISTICS
    // =========================================================

    private void showMyStatistics() {

        try {

            Object[] statistics =
                    dao.getPlayerStatistics(
                            player.getName()
                    );

            if (
                    statistics == null
                            || statistics.length < 8
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid player statistics data returned from database.",
                        "Statistics Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            showMyStatisticsDialog(
                    statistics
            );

        } catch (Exception e) {

            showDatabaseError(e);
        }
    }

    // =========================================================
    // MY STATISTICS DIALOG
    // =========================================================

    private void showMyStatisticsDialog(
            Object[] statistics
    ) {

        String playerName =
                statistics[0] == null
                        ? player.getName()
                        : statistics[0].toString();

        int totalGames =
                getIntValue(
                        statistics,
                        1
                );

        int wins =
                getIntValue(
                        statistics,
                        2
                );

        int losses =
                getIntValue(
                        statistics,
                        3
                );

        double winPercentage =
                getDoubleValue(
                        statistics,
                        4
                );

        int totalBet =
                getIntValue(
                        statistics,
                        5
                );

        int totalPayout =
                getIntValue(
                        statistics,
                        6
                );

        int profitLoss =
                getIntValue(
                        statistics,
                        7
                );

        String message =
                "PLAYER : "
                        + playerName
                        + "\n\n"
                        + "TOTAL GAMES : "
                        + totalGames
                        + "\n\n"
                        + "WINS : "
                        + wins
                        + "\n\n"
                        + "LOSSES : "
                        + losses
                        + "\n\n"
                        + "WIN RATE : "
                        + String.format(
                        "%.2f%%",
                        winPercentage
                )
                        + "\n\n"
                        + "TOTAL BET : "
                        + totalBet
                        + "\n\n"
                        + "TOTAL PAYOUT : "
                        + totalPayout
                        + "\n\n"
                        + "PROFIT / LOSS : "
                        + profitLoss;

        JOptionPane.showMessageDialog(
                this,
                message,
                "MY STATISTICS",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================================================
    // INTEGER HELPER
    // =========================================================

    private int getIntValue(
            Object[] data,
            int index
    ) {

        if (
                data == null
                        || index < 0
                        || index >= data.length
                        || data[index] == null
        ) {

            return 0;
        }

        if (
                data[index]
                        instanceof Number
        ) {

            return ((Number) data[index])
                    .intValue();
        }

        try {

            return Integer.parseInt(
                    data[index].toString()
            );

        } catch (NumberFormatException e) {

            return 0;
        }
    }

    // =========================================================
    // DOUBLE HELPER
    // =========================================================

    private double getDoubleValue(
            Object[] data,
            int index
    ) {

        if (
                data == null
                        || index < 0
                        || index >= data.length
                        || data[index] == null
        ) {

            return 0.0;
        }

        if (
                data[index]
                        instanceof Number
        ) {

            return ((Number) data[index])
                    .doubleValue();
        }

        try {

            return Double.parseDouble(
                    data[index].toString()
            );

        } catch (NumberFormatException e) {

            return 0.0;
        }
    }

    // =========================================================
    // LEADERBOARD
    // =========================================================

    private void showLeaderboard() {

        try {

            List<Object[]> data =
                    dao.getLeaderboard();

            if (
                    data == null
                            || data.isEmpty()
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "No leaderboard data available.",
                        "Leaderboard",
                        JOptionPane.INFORMATION_MESSAGE
                );

                return;
            }

            String[] columns = {
                    "Rank",
                    "Player",
                    "Games",
                    "Wins",
                    "Profit/Loss"
            };

            DefaultTableModel model =
                    new DefaultTableModel(
                            columns,
                            0
                    ) {

                        private static final long serialVersionUID = 1L;

                        @Override
                        public boolean isCellEditable(
                                int row,
                                int column
                        ) {

                            return false;
                        }
                    };

            for (Object[] sourceRow : data) {

                if (
                        sourceRow == null
                                || sourceRow.length
                                < columns.length
                ) {

                    continue;
                }

                model.addRow(sourceRow);
            }

            JTable table =
                    new JTable(model);

            styleTable(table);

            JScrollPane scrollPane =
                    new JScrollPane(table);

            scrollPane.setPreferredSize(
                    new Dimension(
                            750,
                            420
                    )
            );

            JOptionPane.showMessageDialog(
                    this,
                    scrollPane,
                    "LEADERBOARD",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException e) {

            showDatabaseError(e);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error displaying leaderboard:\n"
                            + e.getMessage(),
                    "Leaderboard Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // WITHDRAW
    // =========================================================

    private void openWithdraw() {

        try {

            WithdrawUi withdrawUi =
                    new WithdrawUi(
                            player.getName()
                    );

            withdrawUi.setLocationRelativeTo(
                    this
            );

            withdrawUi.setVisible(true);

            refreshPlayerCredits();

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open Withdraw.\n\n"
                            + e.getMessage(),
                    "Withdraw Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // RESET GAME
    // =========================================================

    private void resetGame() {

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Reset wallet to "
                                + STARTING_CREDITS
                                + " credits?",
                        "Reset Game",
                        JOptionPane.YES_NO_OPTION
                );

        if (
                choice
                        != JOptionPane.YES_OPTION
        ) {

            return;
        }

        try {

            playerDAO.updateCredits(
                    player.getName(),
                    STARTING_CREDITS
            );

            player =
                    new Player(
                            player.getName(),
                            STARTING_CREDITS
                    );

            updateWallet();

            resultLabel.setText(
                    "Wallet reset successfully"
            );

            resultLabel.setForeground(
                    PURPLE
            );

            resultNumberLabel.setText(
                    "—"
            );

            resultColorLabel.setText(
                    "READY FOR NEXT GAME"
            );

            resultColorLabel.setForeground(
                    TEXT_SECONDARY
            );

            resetSelection();

            JOptionPane.showMessageDialog(
                    this,
                    "Wallet reset to "
                            + STARTING_CREDITS
                            + " credits.",
                    "Reset Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException e) {

            showDatabaseError(e);
        }
    }

    // =========================================================
    // CHANGE PASSWORD
    // =========================================================

    private void openChangePassword() {

        try {

            PlayerChangePasswordUi changePasswordUi =
                    new PlayerChangePasswordUi(
                            player.getName()
                    );

            changePasswordUi.setLocationRelativeTo(
                    this
            );

            changePasswordUi.setVisible(true);

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open Change Password.\n\n"
                            + "Error:\n"
                            + e.getMessage(),
                    "Change Password Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private void logout() {

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to logout?",
                        "Logout",
                        JOptionPane.YES_NO_OPTION
                );

        if (
                choice
                        != JOptionPane.YES_OPTION
        ) {

            return;
        }

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
    // EXIT
    // =========================================================

    private void exitApplication() {

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to exit Lucky King?",
                        "Exit",
                        JOptionPane.YES_NO_OPTION
                );

        if (
                choice
                        == JOptionPane.YES_OPTION
        ) {

            System.exit(0);
        }
    }

    // =========================================================
    // DATABASE ERROR
    // =========================================================

    private void showDatabaseError(
            Exception e
    ) {

        JOptionPane.showMessageDialog(
                this,
                "Database Error:\n\n"
                        + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    try {

                        PlayerDao playerDAO =
                                new PlayerDao();

                        Player player =
                                playerDAO.getOrCreatePlayer(
                                        "Sairam"
                                );

                        GameUi gameUi =
                                new GameUi(
                                        player.getName(),
                                        player.getCredits()
                                );

                        gameUi.setVisible(true);

                    } catch (Exception e) {

                        e.printStackTrace();

                        JOptionPane.showMessageDialog(
                                null,
                                "Unable to start Lucky King.\n\n"
                                        + e.getMessage(),
                                "Startup Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
        );
    }
}