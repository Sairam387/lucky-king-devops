package com.sairam.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class AdminUi extends JFrame {

    private static final long serialVersionUID = 1L;

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color BG = new Color(244, 246, 251);
    private static final Color WHITE = Color.WHITE;

    private static final Color NAVY = new Color(25, 30, 55);
    private static final Color NAVY_LIGHT = new Color(38, 45, 75);

    private static final Color PURPLE = new Color(108, 76, 220);
    private static final Color PURPLE_DARK = new Color(82, 57, 175);

    private static final Color GOLD = new Color(235, 177, 45);
    private static final Color BLUE = new Color(55, 130, 235);
    private static final Color GREEN = new Color(45, 170, 105);
    private static final Color RED = new Color(220, 70, 80);
    private static final Color ORANGE = new Color(235, 130, 55);

    private static final Color TEXT = new Color(38, 42, 58);
    private static final Color TEXT_SECONDARY = new Color(105, 110, 130);
    private static final Color BORDER = new Color(225, 228, 237);
    private static final Color TABLE_HEADER = new Color(35, 40, 65);

    private static final int DEFAULT_STARTING_CREDITS = 1000;

    // =========================================================
    // DAO
    // =========================================================

    private AdminDao adminDao;
    private TransactionDao transactionDao;
    private DepositDao depositDao;
    private WithdrawDao withdrawDao;

    private JButton depositRequestsButton;
    private JButton withdrawRequestsButton;

    // =========================================================
    // DASHBOARD LABELS
    // =========================================================

    private JLabel totalPlayersLabel;
    private JLabel totalGamesLabel;
    private JLabel totalWinsLabel;
    private JLabel totalLossesLabel;
    private JLabel totalBetLabel;
    private JLabel totalPayoutLabel;
    private JLabel profitLossLabel;

    // =========================================================
    // PLAYER TABLE
    // =========================================================

    private JTextField searchField;

    private JTable playerTable;
    private DefaultTableModel playerTableModel;

    // Scalable player list / pagination
    private List<Object[]> currentPlayerRows;
    private int currentPlayerPage = 1;
    private static final int PLAYERS_PER_PAGE = 50;
    private JLabel playerCountLabel;
    private JLabel playerPageLabel;
    private JButton playerPrevButton;
    private JButton playerNextButton;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public AdminUi() {

        adminDao = new AdminDao();
        transactionDao = new TransactionDao();
        depositDao = new DepositDao();
        withdrawDao = new WithdrawDao();

        setTitle("Lucky King - Admin Dashboard");

        setSize(1400, 900);

        setMinimumSize(
                new Dimension(1150, 750)
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        buildUi();

        loadDashboardData();

        loadPlayers();
        updateDepositRequestButton();
        updateWithdrawRequestButton();
    }

    // =========================================================
    // MAIN UI
    // =========================================================

    private void buildUi() {

        JPanel root = new JPanel(
                new BorderLayout()
        );

        root.setBackground(BG);

        setContentPane(root);

        root.add(
                createTopBar(),
                BorderLayout.NORTH
        );

        JPanel center = new JPanel(
                new BorderLayout(0, 18)
        );

        center.setBackground(BG);

        center.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        24,
                        10,
                        24
                )
        );

        center.add(
                createDashboardSection(),
                BorderLayout.NORTH
        );

        center.add(
                createPlayerSection(),
                BorderLayout.CENTER
        );

        root.add(
                center,
                BorderLayout.CENTER
        );

        root.add(
                createBottomBar(),
                BorderLayout.SOUTH
        );
    }

    // =========================================================
    // TOP BAR
    // =========================================================

    private JPanel createTopBar() {

        JPanel panel = new JPanel(
                new BorderLayout()
        );

        panel.setBackground(NAVY);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        18,
                        26,
                        18,
                        26
                )
        );

        // LEFT
        JPanel left = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        14,
                        0
                )
        );

        left.setOpaque(false);

        JLabel crown = new JLabel("♛");

        crown.setForeground(GOLD);

        crown.setFont(
                new Font(
                        "Segoe UI Symbol",
                        Font.BOLD,
                        34
                )
        );

        JLabel title = new JLabel(
                "LUCKY KING"
        );

        title.setForeground(WHITE);

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        27
                )
        );

        JLabel subtitle = new JLabel(
                "ADMIN CONTROL PANEL"
        );

        subtitle.setForeground(
                new Color(185, 190, 210)
        );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        JPanel titleBox = new JPanel(
                new GridBagLayout()
        );

        titleBox.setOpaque(false);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        titleBox.add(
                title,
                gbc
        );

        gbc.gridy = 1;

        gbc.insets =
                new Insets(2, 0, 0, 0);

        titleBox.add(
                subtitle,
                gbc
        );

        left.add(crown);
        left.add(titleBox);

        panel.add(
                left,
                BorderLayout.WEST
        );

        // RIGHT
        JPanel right = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT,
                        12,
                        5
                )
        );

        right.setOpaque(false);

        JLabel statusDot = new JLabel(
                "● ONLINE"
        );

        statusDot.setForeground(
                new Color(80, 215, 135)
        );

        statusDot.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        JLabel admin = new JLabel(
                "ADMINISTRATOR"
        );

        admin.setForeground(
                new Color(220, 223, 235)
        );

        admin.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        right.add(statusDot);
        right.add(admin);

        panel.add(
                right,
                BorderLayout.EAST
        );

        return panel;
    }

    // =========================================================
    // DASHBOARD
    // =========================================================

    private JPanel createDashboardSection() {

        JPanel outer = new JPanel(
                new BorderLayout(
                        0,
                        12
                )
        );

        outer.setOpaque(false);

        JLabel heading = new JLabel(
                "Dashboard Overview"
        );

        heading.setForeground(TEXT);

        heading.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        21
                )
        );

        outer.add(
                heading,
                BorderLayout.NORTH
        );

        JPanel cards = new JPanel(
                new GridLayout(
                        1,
                        7,
                        12,
                        0
                )
        );

        cards.setOpaque(false);

        totalPlayersLabel =
                createStatCard(
                        cards,
                        "PLAYERS",
                        "♟",
                        PURPLE
                );

        totalGamesLabel =
                createStatCard(
                        cards,
                        "GAMES",
                        "🎮",
                        BLUE
                );

        totalWinsLabel =
                createStatCard(
                        cards,
                        "WINS",
                        "✓",
                        GREEN
                );

        totalLossesLabel =
                createStatCard(
                        cards,
                        "LOSSES",
                        "×",
                        RED
                );

        totalBetLabel =
                createStatCard(
                        cards,
                        "TOTAL BET",
                        "₹",
                        ORANGE
                );

        totalPayoutLabel =
                createStatCard(
                        cards,
                        "TOTAL PAYOUT",
                        "◆",
                        GOLD
                );

        profitLossLabel =
                createStatCard(
                        cards,
                        "PROFIT / LOSS",
                        "↕",
                        PURPLE_DARK
                );

        outer.add(
                cards,
                BorderLayout.CENTER
        );

        return outer;
    }

    // =========================================================
    // STAT CARD
    // =========================================================

    private JLabel createStatCard(
            JPanel parent,
            String title,
            String icon,
            Color accent
    ) {

        JPanel card = new JPanel(
                new BorderLayout()
        );

        card.setBackground(WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                14,
                                15,
                                14,
                                15
                        )
                )
        );

        JPanel top = new JPanel(
                new BorderLayout()
        );

        top.setOpaque(false);

        JLabel iconLabel = new JLabel(
                icon,
                SwingConstants.CENTER
        );

        iconLabel.setPreferredSize(
                new Dimension(
                        34,
                        34
                )
        );

        iconLabel.setForeground(accent);

        iconLabel.setFont(
                new Font(
                        "Segoe UI Symbol",
                        Font.BOLD,
                        21
                )
        );

        JLabel titleLabel = new JLabel(
                title
        );

        titleLabel.setForeground(
                TEXT_SECONDARY
        );

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        top.add(
                iconLabel,
                BorderLayout.WEST
        );

        top.add(
                titleLabel,
                BorderLayout.CENTER
        );

        JLabel value = new JLabel(
                "0"
        );

        value.setForeground(TEXT);

        value.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        25
                )
        );

        value.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        0,
                        0,
                        0
                )
        );

        card.add(
                top,
                BorderLayout.NORTH
        );

        card.add(
                value,
                BorderLayout.CENTER
        );

        parent.add(card);

        return value;
    }

    // =========================================================
    // PLAYER SECTION
    // =========================================================

    private JPanel createPlayerSection() {

        JPanel panel = new JPanel(
                new BorderLayout(
                        0,
                        12
                )
        );

        panel.setBackground(WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                16,
                                18,
                                18,
                                18
                        )
                )
        );

        panel.add(
                createPlayerToolbar(),
                BorderLayout.NORTH
        );

        createPlayerTable();

        JScrollPane scrollPane =
                new JScrollPane(
                        playerTable
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        panel.add(
                createPlayerPaginationBar(),
                BorderLayout.SOUTH
        );

        return panel;
    }

    // =========================================================
    // PLAYER PAGINATION
    // =========================================================

    private JPanel createPlayerPaginationBar() {

        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(WHITE);
        bar.setBorder(BorderFactory.createEmptyBorder(10, 2, 0, 2));

        playerCountLabel = new JLabel("0 players");
        playerCountLabel.setForeground(TEXT_SECONDARY);
        playerCountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        center.setOpaque(false);

        playerPrevButton = createButton("◀ PREVIOUS", NAVY_LIGHT, WHITE);
        playerPrevButton.setPreferredSize(new Dimension(120, 34));
        playerPrevButton.addActionListener(e -> {
            if (currentPlayerPage > 1) {
                currentPlayerPage--;
                renderPlayerPage();
            }
        });

        playerPageLabel = new JLabel("Page 1 of 1", SwingConstants.CENTER);
        playerPageLabel.setPreferredSize(new Dimension(110, 34));
        playerPageLabel.setForeground(TEXT);
        playerPageLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        playerNextButton = createButton("NEXT ▶", NAVY_LIGHT, WHITE);
        playerNextButton.setPreferredSize(new Dimension(120, 34));
        playerNextButton.addActionListener(e -> {
            int totalPages = getTotalPlayerPages();
            if (currentPlayerPage < totalPages) {
                currentPlayerPage++;
                renderPlayerPage();
            }
        });

        center.add(playerPrevButton);
        center.add(playerPageLabel);
        center.add(playerNextButton);

        JLabel hint = new JLabel("Showing 50 players per page");
        hint.setForeground(TEXT_SECONDARY);
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        bar.add(playerCountLabel, BorderLayout.WEST);
        bar.add(center, BorderLayout.CENTER);
        bar.add(hint, BorderLayout.EAST);

        return bar;
    }

    private int getTotalPlayerPages() {
        int total = currentPlayerRows == null ? 0 : currentPlayerRows.size();
        return Math.max(1, (total + PLAYERS_PER_PAGE - 1) / PLAYERS_PER_PAGE);
    }

    private void renderPlayerPage() {
        if (playerTableModel == null) {
            return;
        }

        playerTableModel.setRowCount(0);

        int total = currentPlayerRows == null ? 0 : currentPlayerRows.size();
        int totalPages = getTotalPlayerPages();

        if (currentPlayerPage > totalPages) {
            currentPlayerPage = totalPages;
        }
        if (currentPlayerPage < 1) {
            currentPlayerPage = 1;
        }

        int start = (currentPlayerPage - 1) * PLAYERS_PER_PAGE;
        int end = Math.min(start + PLAYERS_PER_PAGE, total);

        for (int i = start; i < end; i++) {
            Object[] player = currentPlayerRows.get(i);
            playerTableModel.addRow(player);
        }

        if (playerCountLabel != null) {
            if (total == 0) {
                playerCountLabel.setText("0 players");
            } else {
                playerCountLabel.setText(
                        "Showing " + (start + 1) + "–" + end + " of " + total + " players"
                );
            }
        }

        if (playerPageLabel != null) {
            playerPageLabel.setText("Page " + currentPlayerPage + " of " + totalPages);
        }

        if (playerPrevButton != null) {
            playerPrevButton.setEnabled(currentPlayerPage > 1);
        }

        if (playerNextButton != null) {
            playerNextButton.setEnabled(currentPlayerPage < totalPages);
        }

        updateButtonStates();
    }

    // =========================================================
    // PLAYER TOOLBAR
    // =========================================================

    private JPanel createPlayerToolbar() {

        JPanel panel = new JPanel(
                new BorderLayout()
        );

        panel.setBackground(WHITE);

        JPanel left = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        8,
                        0
                )
        );

        left.setOpaque(false);

        JLabel title = new JLabel(
                "Player Management"
        );

        title.setForeground(TEXT);

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        left.add(title);

        JPanel right = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT,
                        8,
                        0
                )
        );

        right.setOpaque(false);

        searchField = new JTextField(22);

        searchField.setPreferredSize(
                new Dimension(
                        230,
                        36
                )
        );

        searchField.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        searchField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                10,
                                5,
                                10
                        )
                )
        );

        searchField.setToolTipText(
                "Search player name"
        );

        JButton clear =
                createButton(
                        "CLEAR",
                        Color.WHITE,
                        TEXT
                );

        clear.setPreferredSize(
                new Dimension(
                        85,
                        36
                )
        );

        clear.addActionListener(
                e -> {
                    searchField.setText("");
                    loadPlayers();
            updateDepositRequestButton();
            updateWithdrawRequestButton();
                }
        );

        JButton refresh =
                createButton(
                        "↻ REFRESH",
                        PURPLE,
                        WHITE
                );

        refresh.setPreferredSize(
                new Dimension(
                        115,
                        36
                )
        );

        refresh.addActionListener(
                e -> {
                    loadDashboardData();
                    loadPlayers();
            updateDepositRequestButton();
            updateWithdrawRequestButton();
                }
        );

        right.add(searchField);
        right.add(clear);
        right.add(refresh);

        panel.add(
                left,
                BorderLayout.WEST
        );

        panel.add(
                right,
                BorderLayout.EAST
        );

        searchField
                .getDocument()
                .addDocumentListener(
                        new DocumentListener() {

                            @Override
                            public void insertUpdate(
                                    DocumentEvent e
                            ) {
                                searchPlayers();
                            }

                            @Override
                            public void removeUpdate(
                                    DocumentEvent e
                            ) {
                                searchPlayers();
                            }

                            @Override
                            public void changedUpdate(
                                    DocumentEvent e
                            ) {
                                searchPlayers();
                            }
                        }
                );

        return panel;
    }

    // =========================================================
    // PLAYER TABLE
    // =========================================================

    private void createPlayerTable() {

        playerTableModel =
                new DefaultTableModel(
                        new Object[]{
                                "PLAYER NAME",
                                "WALLET CREDITS"
                        },
                        0
                ) {

                    private static final long serialVersionUID =
                            1L;

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        playerTable =
                new JTable(
                        playerTableModel
                );

        playerTable.setRowHeight(42);

        playerTable.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        playerTable.setForeground(TEXT);

        playerTable.setBackground(WHITE);

        playerTable.setSelectionBackground(
                new Color(
                        232,
                        225,
                        255
                )
        );

        playerTable.setSelectionForeground(
                TEXT
        );

        playerTable.setGridColor(
                new Color(
                        238,
                        240,
                        246
                )
        );

        playerTable.setShowVerticalLines(false);

        playerTable.setShowHorizontalLines(true);

        playerTable.setAutoCreateRowSorter(true);

        playerTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        playerTable.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                42
                        )
                );

        playerTable.getTableHeader()
                .setBackground(
                        TABLE_HEADER
                );

        playerTable.getTableHeader()
                .setForeground(
                        WHITE
                );

        playerTable.getTableHeader()
                .setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                12
                        )
                );

        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        playerTable
                .getColumnModel()
                .getColumn(1)
                .setCellRenderer(
                        centerRenderer
                );

        playerTable
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(500);

        playerTable
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(220);

        playerTable
                .getSelectionModel()
                .addListSelectionListener(
                        e -> {
                            if (!e.getValueIsAdjusting()) {
                                updateButtonStates();
                            }
                        }
                );

        playerTable.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {

                        if (e.getClickCount() == 2
                                && playerTable
                                        .getSelectedRow()
                                        >= 0) {

                            showPlayerDetails(
                                    getSelectedPlayer()
                            );
                        }
                    }
                }
        );
    }

    // =========================================================
    // BOTTOM ACTION BAR
    // =========================================================

    private JPanel createBottomBar() {

        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG);
        outer.setBorder(BorderFactory.createEmptyBorder(8, 24, 12, 24));

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(BG);

        main.add(createActionGroup(
                "PLAYER MANAGEMENT", NAVY,
                createActionButton("+ ADD PLAYER", PURPLE, WHITE, e -> addPlayer(), 145),
                createActionButton("✎ EDIT", BLUE, WHITE, e -> editSelectedPlayer(), 120),
                createActionButton("🔐 PASSWORD", NAVY, WHITE, e -> resetSelectedPlayerPassword(), 145),
                createActionButton("✕ DELETE", RED, WHITE, e -> deleteSelectedPlayer(), 120),
                createActionButton("📊 STATISTICS", GREEN, WHITE, e -> openSelectedPlayerStatistics(), 145)
        ));

        main.add(Box.createVerticalStrut(6));

        main.add(createActionGroup(
                "WALLET & REQUESTS", GOLD,
                createActionButton("₹ CREDITS", ORANGE, WHITE, e -> changeSelectedCredits(), 130),
                createActionButton("↻ RESET WALLET", GOLD, NAVY, e -> resetSelectedWallet(), 155),
                createActionButton("💰 DEPOSITS", GREEN, WHITE, e -> openDepositRequests(), 145),
                createActionButton("💸 WITHDRAWALS", RED, WHITE, e -> openWithdrawRequests(), 155)
        ));

        main.add(Box.createVerticalStrut(6));

        main.add(createActionGroup(
                "REPORTS & SYSTEM", PURPLE,
                createActionButton("▣ TRANSACTIONS", PURPLE_DARK, WHITE, e -> openSelectedPlayerTransactions(), 155),
                createActionButton("🎮 GAME HISTORY", BLUE, WHITE, e -> openSelectedGameHistory(), 155),
                createActionButton("📈 REPORTS", NAVY_LIGHT, WHITE, e -> openReports(), 130),
                createActionButton("LOGOUT", new Color(105, 110, 125), WHITE, e -> logout(), 120),
                createActionButton("EXIT", RED, WHITE, e -> exitApplication(), 100)
        ));

        outer.add(main, BorderLayout.CENTER);
        return outer;
    }

    private JPanel createActionGroup(String title, Color titleColor, JButton... buttons) {

        JPanel group = new JPanel(new BorderLayout(0, 4));
        group.setBackground(WHITE);
        group.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.LineBorder(BORDER, 1),
                new javax.swing.border.EmptyBorder(5, 8, 7, 8)
        ));

        JLabel groupTitle = new JLabel(title);
        groupTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        groupTitle.setForeground(titleColor);
        groupTitle.setBorder(BorderFactory.createEmptyBorder(0, 4, 2, 0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 0));
        buttonPanel.setBackground(WHITE);

        for (JButton button : buttons) {
            buttonPanel.add(button);
        }

        group.add(groupTitle, BorderLayout.NORTH);
        group.add(buttonPanel, BorderLayout.CENTER);
        return group;
    }

    private JButton createActionButton(
            String text, Color background, Color foreground,
            java.awt.event.ActionListener listener, int width) {

        JButton button = createButton(text, background, foreground);
        button.setPreferredSize(new Dimension(width, 38));
        button.setMinimumSize(new Dimension(width, 38));
        button.setMaximumSize(new Dimension(width, 38));
        button.addActionListener(listener);
        return button;
    }

    // =========================================================
    // BUTTON FACTORY
    // =========================================================

    private JButton createButton(
            String text,
            Color background,
            Color foreground
    ) {

        JButton button = new JButton(text);

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        button.setForeground(foreground);

        button.setBackground(background);

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
                        125,
                        38
                )
        );

        return button;
    }

    // =========================================================
    // DASHBOARD DATA
    // =========================================================

    private void loadDashboardData() {

        try {

            int totalPlayers =
                    adminDao.getTotalPlayers();

            int totalGames =
                    adminDao.getTotalGames();

            int totalWins =
                    adminDao.getTotalWins();

            int totalLosses =
                    adminDao.getTotalLosses();

            int totalBet =
                    adminDao.getTotalBet();

            int totalPayout =
                    adminDao.getTotalPayout();

            int profitLoss =
                    adminDao.getTotalProfitLoss();

            totalPlayersLabel.setText(
                    String.valueOf(
                            totalPlayers
                    )
            );

            totalGamesLabel.setText(
                    String.valueOf(
                            totalGames
                    )
            );

            totalWinsLabel.setText(
                    String.valueOf(
                            totalWins
                    )
            );

            totalLossesLabel.setText(
                    String.valueOf(
                            totalLosses
                    )
            );

            totalBetLabel.setText(
                    "₹ " + totalBet
            );

            totalPayoutLabel.setText(
                    "₹ " + totalPayout
            );

            profitLossLabel.setText(
                    "₹ " + profitLoss
            );

            if (profitLoss > 0) {

                profitLossLabel.setForeground(
                        GREEN
                );

            } else if (profitLoss < 0) {

                profitLossLabel.setForeground(
                        RED
                );

            } else {

                profitLossLabel.setForeground(
                        TEXT
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();

            showDatabaseError(
                    "Unable to load dashboard statistics.",
                    e
            );
        }
    }

    // =========================================================
    // LOAD PLAYERS
    // =========================================================

    private void loadPlayers() {

        if (playerTableModel == null) {
            return;
        }

        try {
            List<Object[]> players = adminDao.getAllPlayers();
            currentPlayerRows = players == null ? new java.util.ArrayList<>() : players;
            currentPlayerPage = 1;
            renderPlayerPage();

        } catch (SQLException e) {
            e.printStackTrace();
            showDatabaseError(
                    "Unable to load players.",
                    e
            );
        }
    }

    // =========================================================
    // SEARCH PLAYERS
    // =========================================================

    private void searchPlayers() {

        if (searchField == null || playerTableModel == null) {
            return;
        }

        String keyword = searchField.getText().trim();

        try {
            List<Object[]> players;

            if (keyword.isEmpty()) {
                players = adminDao.getAllPlayers();
            } else {
                players = adminDao.searchPlayers(keyword);
            }

            currentPlayerRows = players == null
                    ? new java.util.ArrayList<>()
                    : players;

            currentPlayerPage = 1;
            renderPlayerPage();

        } catch (SQLException e) {
            e.printStackTrace();
            showDatabaseError(
                    "Unable to search players.",
                    e
            );
        }
    }

    // =========================================================
    // SELECTED PLAYER
    // =========================================================

    private String getSelectedPlayer() {

        if (playerTable == null) {
            return null;
        }

        int selectedRow =
                playerTable.getSelectedRow();

        if (selectedRow < 0) {
            return null;
        }

        int modelRow =
                playerTable
                        .convertRowIndexToModel(
                                selectedRow
                        );

        Object value =
                playerTableModel.getValueAt(
                        modelRow,
                        0
                );

        if (value == null) {
            return null;
        }

        return value.toString();
    }

    private void updateButtonStates() {
        // Kept intentionally.
        // Buttons use validation when clicked.
    }

    // =========================================================
    // ADD PLAYER
    // =========================================================

    private void addPlayer() {

        JTextField nameField =
                new JTextField();

        JTextField creditsField =
                new JTextField(
                        String.valueOf(
                                DEFAULT_STARTING_CREDITS
                        )
                );

        JPanel panel =
                createFormPanel();

        addFormRow(
                panel,
                0,
                "Player Name",
                nameField
        );

        addFormRow(
                panel,
                1,
                "Starting Credits",
                creditsField
        );

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Add New Player",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String playerName =
                nameField.getText()
                        .trim();

        String creditsText =
                creditsField.getText()
                        .trim();

        if (playerName.isEmpty()) {

            showWarning(
                    "Player name is required.",
                    "Invalid Player"
            );

            return;
        }

        if (playerName.length() > 100) {

            showWarning(
                    "Player name cannot exceed 100 characters.",
                    "Invalid Player"
            );

            return;
        }

        int credits;

        try {

            credits =
                    Integer.parseInt(
                            creditsText
                    );

        } catch (NumberFormatException e) {

            showWarning(
                    "Credits must be a valid number.",
                    "Invalid Credits"
            );

            return;
        }

        if (credits < 0) {

            showWarning(
                    "Credits cannot be negative.",
                    "Invalid Credits"
            );

            return;
        }

        try {

            adminDao.addPlayer(
                    playerName,
                    credits
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Player created successfully.\n\n"
                            + "Player: "
                            + playerName
                            + "\nWallet: ₹ "
                            + credits,
                    "Player Created",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadPlayers();
            updateDepositRequestButton();
            updateWithdrawRequestButton();

            loadDashboardData();

        } catch (SQLException e) {

            e.printStackTrace();

            showDatabaseError(
                    "Unable to add player.",
                    e
            );
        }
    }

    // =========================================================
    // EDIT PLAYER
    // =========================================================

    private void editSelectedPlayer() {

        String playerName =
                getSelectedPlayer();

        if (playerName == null) {

            showWarning(
                    "Please select a player first.",
                    "Edit Player"
            );

            return;
        }

        try {

            int currentCredits =
                    adminDao.getPlayerCredits(
                            playerName
                    );

            JTextField playerField =
                    new JTextField(
                            playerName
                    );

            playerField.setEditable(false);

            JTextField creditsField =
                    new JTextField(
                            String.valueOf(
                                    currentCredits
                            )
                    );

            JPanel panel =
                    createFormPanel();

            addFormRow(
                    panel,
                    0,
                    "Player Name",
                    playerField
            );

            addFormRow(
                    panel,
                    1,
                    "Wallet Credits",
                    creditsField
            );

            int result =
                    JOptionPane.showConfirmDialog(
                            this,
                            panel,
                            "Edit Player",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            int newCredits;

            try {

                newCredits =
                        Integer.parseInt(
                                creditsField
                                        .getText()
                                        .trim()
                        );

            } catch (NumberFormatException e) {

                showWarning(
                        "Credits must be a valid number.",
                        "Invalid Credits"
                );

                return;
            }

            if (newCredits < 0) {

                showWarning(
                        "Credits cannot be negative.",
                        "Invalid Credits"
                );

                return;
            }

            if (newCredits == currentCredits) {

                showInfo(
                        "No changes were made.",
                        "Edit Player"
                );

                return;
            }

            int confirmation =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Update wallet?\n\n"
                                    + "Player: "
                                    + playerName
                                    + "\n"
                                    + "Old Balance: ₹ "
                                    + currentCredits
                                    + "\n"
                                    + "New Balance: ₹ "
                                    + newCredits
                                    + "\n\n"
                                    + "A wallet transaction will be recorded.",
                            "Confirm Wallet Update",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

            if (confirmation
                    != JOptionPane.YES_OPTION) {
                return;
            }

            adminDao.updateCreditsWithTransaction(
                    playerName,
                    newCredits
            );

            showInfo(
                    "Player wallet updated successfully.",
                    "Success"
            );

            loadPlayers();
            updateDepositRequestButton();
            updateWithdrawRequestButton();
            loadDashboardData();

        } catch (SQLException e) {

            e.printStackTrace();

            showDatabaseError(
                    "Unable to edit player.",
                    e
            );
        }
    }

    // =========================================================
    // RESET PASSWORD
    // =========================================================

    private void resetSelectedPlayerPassword() {

        String playerName =
                getSelectedPlayer();

        if (playerName == null) {

            showWarning(
                    "Please select a player first.",
                    "Reset Password"
            );

            return;
        }

        JPasswordField passwordField =
                new JPasswordField();

        JPasswordField confirmField =
                new JPasswordField();

        JPanel panel =
                createFormPanel();

        addFormRow(
                panel,
                0,
                "Player",
                createReadOnlyField(
                        playerName
                )
        );

        addFormRow(
                panel,
                1,
                "New Password",
                passwordField
        );

        addFormRow(
                panel,
                2,
                "Confirm Password",
                confirmField
        );

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Reset Player Password",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String newPassword =
                new String(
                        passwordField.getPassword()
                );

        String confirmPassword =
                new String(
                        confirmField.getPassword()
                );

        try {

            if (newPassword.isEmpty()) {

                showWarning(
                        "New password is required.",
                        "Invalid Password"
                );

                return;
            }

            if (newPassword.length() < 6) {

                showWarning(
                        "Password must contain at least 6 characters.",
                        "Invalid Password"
                );

                return;
            }

            if (!newPassword.equals(
                    confirmPassword
            )) {

                showWarning(
                        "Passwords do not match.",
                        "Password Mismatch"
                );

                return;
            }

            int confirmation =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Reset password for:\n\n"
                                    + playerName
                                    + "\n\n"
                                    + "The existing password will be replaced.",
                            "Confirm Password Reset",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

            if (confirmation
                    != JOptionPane.YES_OPTION) {
                return;
            }

            adminDao.resetPlayerPassword(
                    playerName,
                    newPassword
            );

            showInfo(
                    "Password reset successfully.\n\n"
                            + "Player: "
                            + playerName,
                    "Password Reset"
            );

        } catch (SQLException e) {

            e.printStackTrace();

            showDatabaseError(
                    "Unable to reset password.",
                    e
            );

        } catch (IllegalArgumentException e) {

            showWarning(
                    e.getMessage(),
                    "Invalid Password"
            );

        } finally {

            java.util.Arrays.fill(
                    passwordField.getPassword(),
                    '\0'
            );

            java.util.Arrays.fill(
                    confirmField.getPassword(),
                    '\0'
            );
        }
    }

    // =========================================================
    // CHANGE CREDITS
    // =========================================================

    private void changeSelectedCredits() {

        String playerName =
                getSelectedPlayer();

        if (playerName == null) {

            showWarning(
                    "Please select a player first.",
                    "Change Credits"
            );

            return;
        }

        try {

            int currentCredits =
                    adminDao.getPlayerCredits(
                            playerName
                    );

            String input =
                    JOptionPane.showInputDialog(
                            this,
                            "Current Wallet: ₹ "
                                    + currentCredits
                                    + "\n\n"
                                    + "Enter new wallet credits:",
                            "Change Wallet Credits",
                            JOptionPane.QUESTION_MESSAGE
                    );

            if (input == null) {
                return;
            }

            int newCredits;

            try {

                newCredits =
                        Integer.parseInt(
                                input.trim()
                        );

            } catch (NumberFormatException e) {

                showWarning(
                        "Please enter a valid number.",
                        "Invalid Credits"
                );

                return;
            }

            if (newCredits < 0) {

                showWarning(
                        "Credits cannot be negative.",
                        "Invalid Credits"
                );

                return;
            }

            if (newCredits == currentCredits) {
                return;
            }

            int confirmation =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Player: "
                                    + playerName
                                    + "\n\n"
                                    + "Old Wallet: ₹ "
                                    + currentCredits
                                    + "\n"
                                    + "New Wallet: ₹ "
                                    + newCredits
                                    + "\n\n"
                                    + "A wallet transaction will be recorded.",
                            "Confirm Credits Change",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

            if (confirmation
                    != JOptionPane.YES_OPTION) {
                return;
            }

            adminDao.updateCreditsWithTransaction(
                    playerName,
                    newCredits
            );

            showInfo(
                    "Wallet credits updated successfully.",
                    "Success"
            );

            loadPlayers();
            updateDepositRequestButton();
            updateWithdrawRequestButton();
            loadDashboardData();

        } catch (SQLException e) {

            e.printStackTrace();

            showDatabaseError(
                    "Unable to change credits.",
                    e
            );
        }
    }

    // =========================================================
    // RESET WALLET
    // =========================================================

    private void resetSelectedWallet() {

        String playerName =
                getSelectedPlayer();

        if (playerName == null) {

            showWarning(
                    "Please select a player first.",
                    "Reset Wallet"
            );

            return;
        }

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Reset wallet for:\n\n"
                                + playerName
                                + "\n\n"
                                + "New balance: ₹ "
                                + DEFAULT_STARTING_CREDITS
                                + "\n\n"
                                + "A wallet reset transaction will be recorded.",
                        "Confirm Wallet Reset",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (confirmation
                != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            adminDao.resetCreditsWithTransaction(
                    playerName
            );

            showInfo(
                    "Wallet reset successfully.\n\n"
                            + "Player: "
                            + playerName
                            + "\nWallet: ₹ "
                            + DEFAULT_STARTING_CREDITS,
                    "Wallet Reset"
            );

            loadPlayers();
            updateDepositRequestButton();
            updateWithdrawRequestButton();
            loadDashboardData();

        } catch (SQLException e) {

            e.printStackTrace();

            showDatabaseError(
                    "Unable to reset wallet.",
                    e
            );
        }
    }

    // =========================================================
    // DELETE PLAYER
    // =========================================================

    private void deleteSelectedPlayer() {

        String playerName =
                getSelectedPlayer();

        if (playerName == null) {

            showWarning(
                    "Please select a player first.",
                    "Delete Player"
            );

            return;
        }

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "DELETE PLAYER?\n\n"
                                + "Player: "
                                + playerName
                                + "\n\n"
                                + "This will delete:\n"
                                + "• Player account\n"
                                + "• Game history\n"
                                + "• Wallet transactions\n\n"
                                + "This action cannot be undone.",
                        "Permanent Player Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.ERROR_MESSAGE
                );

        if (confirmation
                != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            adminDao.deletePlayer(
                    playerName
            );

            showInfo(
                    "Player deleted successfully.",
                    "Player Deleted"
            );

            loadPlayers();
            updateDepositRequestButton();
            updateWithdrawRequestButton();
            loadDashboardData();

        } catch (SQLException e) {

            e.printStackTrace();

            showDatabaseError(
                    "Unable to delete player.",
                    e
            );
        }
    }

    // =========================================================
    // PLAYER STATISTICS
    // =========================================================

    private void openSelectedPlayerStatistics() {

        String playerName =
                getSelectedPlayer();

        if (playerName == null) {

            showWarning(
                    "Please select a player first.",
                    "Player Statistics"
            );

            return;
        }

        showPlayerDetails(
                playerName
        );
    }

    // =========================================================
    // WALLET TRANSACTIONS
    // =========================================================

    private void openSelectedPlayerTransactions() {

        String playerName =
                getSelectedPlayer();

        if (playerName == null) {

            showWarning(
                    "Please select a player first.",
                    "Wallet Transactions"
            );

            return;
        }

        try {

            List<Transaction> transactions =
                    transactionDao.getPlayerTransactions(
                            playerName
                    );

            showTransactionTable(
                    transactions,
                    "Wallet Transactions - "
                            + playerName
            );

        } catch (SQLException e) {

            e.printStackTrace();

            showDatabaseError(
                    "Unable to load wallet transactions.",
                    e
            );
        }
    }

    // =========================================================
    // TRANSACTION TABLE
    // =========================================================

    private void showTransactionTable(
            List<Transaction> transactions,
            String title
    ) {

        if (transactions == null
                || transactions.isEmpty()) {

            showInfo(
                    "No wallet transactions found for this player.",
                    title
            );

            return;
        }

        String[] columns = {
                "ID",
                "PLAYER",
                "TYPE",
                "AMOUNT",
                "DATE & TIME"
        };

        Object[][] rows =
                new Object[
                        transactions.size()
                ][
                        columns.length
                ];

        SimpleDateFormat dateFormat =
                new SimpleDateFormat(
                        "dd-MM-yyyy HH:mm:ss"
                );

        for (int i = 0;
                i < transactions.size();
                i++) {

            Transaction transaction =
                    transactions.get(i);

            rows[i][0] =
                    transaction.getTransactionId();

            rows[i][1] =
                    transaction.getPlayerName();

            rows[i][2] =
                    transaction.getTransactionType();

            rows[i][3] =
                    "₹ "
                            + transaction.getAmount();

            Timestamp timestamp =
                    transaction.getTransactionDate();

            rows[i][4] =
                    timestamp != null
                            ? dateFormat.format(
                                    timestamp
                            )
                            : "";
        }

        JTable table =
                new JTable(
                        new DefaultTableModel(
                                rows,
                                columns
                        ) {

                            private static final long serialVersionUID =
                                    1L;

                            @Override
                            public boolean isCellEditable(
                                    int row,
                                    int column
                            ) {
                                return false;
                            }
                        }
                );

        styleTable(table);

        JScrollPane scrollPane =
                new JScrollPane(
                        table
                );

        scrollPane.setPreferredSize(
                new Dimension(
                        850,
                        470
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
    // GAME HISTORY
    // =========================================================

    private void openSelectedGameHistory() {

        String playerName =
                getSelectedPlayer();

        if (playerName == null) {

            showWarning(
                    "Please select a player first.",
                    "Game History"
            );

            return;
        }

        try {

            if (!adminDao.playerExists(
                    playerName
            )) {

                showWarning(
                        "Player no longer exists.",
                        "Game History"
                );

                loadPlayers();
            updateDepositRequestButton();
            updateWithdrawRequestButton();

                return;
            }

            AdminGameHistoryUi historyUi =
                    new AdminGameHistoryUi();

            historyUi.setLocationRelativeTo(
                    this
            );

            historyUi.setVisible(true);

        } catch (SQLException e) {

            e.printStackTrace();

            showDatabaseError(
                    "Unable to open game history.",
                    e
            );
        }
    }

    // =========================================================
    // PLAYER DETAILS
    // =========================================================

    // =========================================================
    // DEPOSIT REQUESTS
    // =========================================================

    private void updateDepositRequestButton() {

        if (depositRequestsButton == null || depositDao == null) {
            return;
        }

        try {
            int pending = depositDao.getPendingDepositCount();

            if (pending > 0) {
                depositRequestsButton.setText(
                        "💰 DEPOSITS (" + pending + ")"
                );
                depositRequestsButton.setBackground(GREEN);
            } else {
                depositRequestsButton.setText("💰 DEPOSITS");
                depositRequestsButton.setBackground(NAVY_LIGHT);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            depositRequestsButton.setText("💰 DEPOSITS");
            depositRequestsButton.setBackground(NAVY_LIGHT);
        }
    }

    private void openDepositRequests() {

        JDialog dialog = new JDialog(
                this,
                "Lucky King - Deposit Requests",
                true
        );

        dialog.setSize(1050, 620);
        dialog.setMinimumSize(new Dimension(900, 520));
        dialog.setLocationRelativeTo(this);

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBackground(BG);
        root.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        JLabel title = new JLabel("💰  DEPOSIT REQUESTS");
        title.setForeground(WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel subtitle = new JLabel("Review and approve player wallet deposits");
        subtitle.setForeground(new Color(205, 210, 225));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 2));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);

        JLabel pendingLabel = new JLabel();
        pendingLabel.setForeground(GOLD);
        pendingLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pendingLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(pendingLabel, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        String[] columns = {
                "REQUEST ID", "PLAYER", "AMOUNT", "STATUS", "CREATED AT"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        root.add(scroll, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setBackground(BG);

        JButton approve = createButton("✓ APPROVE", GREEN, WHITE);
        JButton reject = createButton("✕ REJECT", RED, WHITE);
        JButton refresh = createButton("↻ REFRESH", BLUE, WHITE);
        JButton close = createButton("CLOSE", NAVY_LIGHT, WHITE);

        buttons.add(approve);
        buttons.add(reject);
        buttons.add(refresh);
        buttons.add(close);
        root.add(buttons, BorderLayout.SOUTH);

        Runnable load = () -> {
            model.setRowCount(0);
            try {
                List<Object[]> rows = depositDao.getPendingDepositRequests();
                pendingLabel.setText("Pending: " + rows.size());
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                for (Object[] row : rows) {
                    Timestamp created = (Timestamp) row[4];
                    model.addRow(new Object[] {
                            row[0], row[1], "₹ " + row[2], row[3],
                            created == null ? "-" : format.format(created)
                    });
                }
            } catch (SQLException e) {
                showDatabaseError("Unable to load deposit requests.", e);
            }
        };

        approve.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected < 0) {
                showWarning("Please select a deposit request.", "Deposit Requests");
                return;
            }

            int requestId = ((Number) model.getValueAt(selected, 0)).intValue();
            String player = String.valueOf(model.getValueAt(selected, 1));
            String amount = String.valueOf(model.getValueAt(selected, 2));

            int choice = JOptionPane.showConfirmDialog(
                    dialog,
                    "Approve deposit request?\n\nRequest ID: " + requestId
                            + "\nPlayer: " + player + "\nAmount: " + amount,
                    "Approve Deposit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (choice != JOptionPane.YES_OPTION) return;

            try {
                depositDao.approveDeposit(requestId);
                showInfo("Deposit approved successfully.", "Deposit Approved");
                loadDashboardData();
                loadPlayers();
                updateDepositRequestButton();
                load.run();
            } catch (SQLException ex) {
                showDatabaseError("Unable to approve deposit request.", ex);
            }
        });

        reject.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected < 0) {
                showWarning("Please select a deposit request.", "Deposit Requests");
                return;
            }

            int requestId = ((Number) model.getValueAt(selected, 0)).intValue();
            String player = String.valueOf(model.getValueAt(selected, 1));
            String amount = String.valueOf(model.getValueAt(selected, 2));

            int choice = JOptionPane.showConfirmDialog(
                    dialog,
                    "Reject deposit request?\n\nRequest ID: " + requestId
                            + "\nPlayer: " + player + "\nAmount: " + amount,
                    "Reject Deposit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (choice != JOptionPane.YES_OPTION) return;

            try {
                depositDao.rejectDeposit(requestId);
                showInfo("Deposit request rejected.", "Deposit Rejected");
                updateDepositRequestButton();
                load.run();
            } catch (SQLException ex) {
                showDatabaseError("Unable to reject deposit request.", ex);
            }
        });

        refresh.addActionListener(e -> {
            loadDashboardData();
            loadPlayers();
            updateDepositRequestButton();
            load.run();
        });

        close.addActionListener(e -> dialog.dispose());

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() >= 0) {
                    int id = ((Number) model.getValueAt(
                            table.getSelectedRow(), 0)).intValue();
                    showDepositRequestDetails(dialog, id);
                }
            }
        });

        dialog.setContentPane(root);
        load.run();
        dialog.setVisible(true);
    }

    private void showDepositRequestDetails(JDialog parent, int requestId) {
        try {
            Object[] row = depositDao.getDepositRequest(requestId);
            if (row == null) {
                showWarning("Deposit request not found.", "Deposit Details");
                return;
            }

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String created = row[4] == null ? "-" : format.format((Timestamp) row[4]);
            String approved = row[5] == null ? "-" : format.format((Timestamp) row[5]);

            JOptionPane.showMessageDialog(
                    parent,
                    "Request ID : " + row[0]
                            + "\nPlayer     : " + row[1]
                            + "\nAmount     : ₹ " + row[2]
                            + "\nStatus     : " + row[3]
                            + "\nCreated At : " + created
                            + "\nApproved At: " + approved,
                    "Deposit Request Details",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (SQLException e) {
            showDatabaseError("Unable to load deposit details.", e);
        }
    }

    // =========================================================
    // WITHDRAW REQUESTS
    // =========================================================

    private void updateWithdrawRequestButton() {

        if (withdrawRequestsButton == null || withdrawDao == null) {
            return;
        }

        try {
            int pending = withdrawDao.getPendingWithdrawCount();

            if (pending > 0) {
                withdrawRequestsButton.setText(
                        "💸 WITHDRAWALS (" + pending + ")"
                );
                withdrawRequestsButton.setBackground(RED);
            } else {
                withdrawRequestsButton.setText("💸 WITHDRAWALS");
                withdrawRequestsButton.setBackground(NAVY_LIGHT);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            withdrawRequestsButton.setText("💸 WITHDRAWALS");
            withdrawRequestsButton.setBackground(NAVY_LIGHT);
        }
    }

    private void openWithdrawRequests() {

        JDialog dialog = new JDialog(
                this,
                "Lucky King - Withdrawal Requests",
                true
        );

        dialog.setSize(1050, 620);
        dialog.setMinimumSize(new Dimension(900, 520));
        dialog.setLocationRelativeTo(this);

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBackground(BG);
        root.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        JLabel title = new JLabel("💸  WITHDRAWAL REQUESTS");
        title.setForeground(WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel subtitle = new JLabel("Review and process player wallet withdrawals");
        subtitle.setForeground(new Color(205, 210, 225));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 2));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);

        JLabel pendingLabel = new JLabel();
        pendingLabel.setForeground(GOLD);
        pendingLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pendingLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(pendingLabel, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        String[] columns = {
                "REQUEST ID", "PLAYER", "AMOUNT", "STATUS", "CREATED AT"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        root.add(scroll, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setBackground(BG);

        JButton approve = createButton("✓ APPROVE", GREEN, WHITE);
        JButton reject = createButton("✕ REJECT", RED, WHITE);
        JButton refresh = createButton("↻ REFRESH", BLUE, WHITE);
        JButton close = createButton("CLOSE", NAVY_LIGHT, WHITE);

        buttons.add(approve);
        buttons.add(reject);
        buttons.add(refresh);
        buttons.add(close);
        root.add(buttons, BorderLayout.SOUTH);

        Runnable load = () -> {
            model.setRowCount(0);
            try {
                List<Object[]> rows = withdrawDao.getPendingWithdrawRequests();
                pendingLabel.setText("Pending: " + rows.size());
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                for (Object[] row : rows) {
                    Timestamp created = (Timestamp) row[4];
                    model.addRow(new Object[] {
                            row[0], row[1], "₹ " + row[2], row[3],
                            created == null ? "-" : format.format(created)
                    });
                }
            } catch (SQLException e) {
                showDatabaseError("Unable to load withdrawal requests.", e);
            }
        };

        approve.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected < 0) {
                showWarning("Please select a withdrawal request.", "Withdrawal Requests");
                return;
            }

            int requestId = ((Number) model.getValueAt(selected, 0)).intValue();
            String player = String.valueOf(model.getValueAt(selected, 1));
            String amount = String.valueOf(model.getValueAt(selected, 2));

            int choice = JOptionPane.showConfirmDialog(
                    dialog,
                    "Approve withdrawal request?\n\nRequest ID: " + requestId
                            + "\nPlayer: " + player + "\nAmount: " + amount
                            + "\n\nThe amount will be deducted from the player's wallet.",
                    "Approve Withdrawal",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (choice != JOptionPane.YES_OPTION) return;

            try {
                withdrawDao.approveWithdraw(requestId);
                showInfo("Withdrawal approved successfully.\nThe player balance has been updated.",
                        "Withdrawal Approved");
                loadDashboardData();
                loadPlayers();
                updateWithdrawRequestButton();
                load.run();
            } catch (SQLException ex) {
                showDatabaseError("Unable to approve withdrawal request.", ex);
            }
        });

        reject.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected < 0) {
                showWarning("Please select a withdrawal request.", "Withdrawal Requests");
                return;
            }

            int requestId = ((Number) model.getValueAt(selected, 0)).intValue();
            String player = String.valueOf(model.getValueAt(selected, 1));
            String amount = String.valueOf(model.getValueAt(selected, 2));

            int choice = JOptionPane.showConfirmDialog(
                    dialog,
                    "Reject withdrawal request?\n\nRequest ID: " + requestId
                            + "\nPlayer: " + player + "\nAmount: " + amount
                            + "\n\nThe amount will NOT be deducted.",
                    "Reject Withdrawal",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (choice != JOptionPane.YES_OPTION) return;

            try {
                withdrawDao.rejectWithdraw(requestId);
                showInfo("Withdrawal request rejected.", "Withdrawal Rejected");
                updateWithdrawRequestButton();
                load.run();
            } catch (SQLException ex) {
                showDatabaseError("Unable to reject withdrawal request.", ex);
            }
        });

        refresh.addActionListener(e -> {
            loadDashboardData();
            loadPlayers();
            updateWithdrawRequestButton();
            load.run();
        });

        close.addActionListener(e -> dialog.dispose());

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() >= 0) {
                    int id = ((Number) model.getValueAt(
                            table.getSelectedRow(), 0)).intValue();
                    showWithdrawRequestDetails(dialog, id);
                }
            }
        });

        dialog.setContentPane(root);
        load.run();
        dialog.setVisible(true);
    }

    private void showWithdrawRequestDetails(JDialog parent, int requestId) {
        try {
            Object[] row = withdrawDao.getWithdrawRequest(requestId);
            if (row == null) {
                showWarning("Withdrawal request not found.", "Withdrawal Details");
                return;
            }

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String created = row[4] == null ? "-" : format.format((Timestamp) row[4]);
            String approved = row[5] == null ? "-" : format.format((Timestamp) row[5]);

            JOptionPane.showMessageDialog(
                    parent,
                    "Request ID : " + row[0]
                            + "\nPlayer     : " + row[1]
                            + "\nAmount     : ₹ " + row[2]
                            + "\nStatus     : " + row[3]
                            + "\nCreated At : " + created
                            + "\nApproved At: " + approved,
                    "Withdrawal Request Details",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (SQLException e) {
            showDatabaseError("Unable to load withdrawal details.", e);
        }
    }

    private void showPlayerDetails(
            String playerName
    ) {

        if (playerName == null
                || playerName.trim().isEmpty()) {

            return;
        }

        try {

            if (!adminDao.playerExists(
                    playerName
            )) {

                showWarning(
                        "Player no longer exists.",
                        "Player Details"
                );

                loadPlayers();
            updateDepositRequestButton();
            updateWithdrawRequestButton();

                return;
            }

            PlayerDetailsUi detailsUi =
                    new PlayerDetailsUi(
                            playerName
                    );

            detailsUi.setLocationRelativeTo(
                    this
            );

            detailsUi.setVisible(true);

        } catch (SQLException e) {

            e.printStackTrace();

            showDatabaseError(
                    "Unable to open player details.",
                    e
            );
        }
    }

    // =========================================================
    // REPORTS
    // =========================================================

    private void openReports() {

        ReportsUi reportsUi =
                new ReportsUi();

        reportsUi.setLocationRelativeTo(
                this
        );

        reportsUi.setVisible(true);
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private void logout() {

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to logout?",
                        "Logout",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (confirmation
                != JOptionPane.YES_OPTION) {

            return;
        }

        dispose();

        AdminLoginUi loginUi =
                new AdminLoginUi();

        loginUi.setLocationRelativeTo(null);

        loginUi.setVisible(true);
    }

    // =========================================================
    // EXIT
    // =========================================================

    private void exitApplication() {

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to exit Lucky King?",
                        "Exit Application",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (confirmation
                == JOptionPane.YES_OPTION) {

            System.exit(0);
        }
    }

    // =========================================================
    // FORM HELPERS
    // =========================================================

    private JPanel createFormPanel() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        return panel;
    }

    private void addFormRow(
            JPanel panel,
            int row,
            String label,
            java.awt.Component component
    ) {

        GridBagConstraints left =
                new GridBagConstraints();

        left.gridx = 0;
        left.gridy = row;
        left.weightx = 0;
        left.anchor =
                GridBagConstraints.WEST;

        left.insets =
                new Insets(
                        6,
                        6,
                        6,
                        12
                );

        JLabel labelComponent =
                new JLabel(
                        label
                );

        labelComponent.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        panel.add(
                labelComponent,
                left
        );

        GridBagConstraints right =
                new GridBagConstraints();

        right.gridx = 1;
        right.gridy = row;
        right.weightx = 1;
        right.fill =
                GridBagConstraints.HORIZONTAL;

        right.insets =
                new Insets(
                        6,
                        6,
                        6,
                        6
                );

        if (component instanceof JTextField) {

            component.setPreferredSize(
                    new Dimension(
                            240,
                            34
                    )
            );
        }

        panel.add(
                component,
                right
        );
    }

    private JTextField createReadOnlyField(
            String text
    ) {

        JTextField field =
                new JTextField(text);

        field.setEditable(false);

        return field;
    }

    // =========================================================
    // TABLE STYLE
    // =========================================================

    private void styleTable(
            JTable table
    ) {

        table.setRowHeight(35);

        table.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        table.setSelectionBackground(
                new Color(
                        232,
                        225,
                        255
                )
        );

        table.setSelectionForeground(
                TEXT
        );

        table.setShowVerticalLines(false);

        table.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                38
                        )
                );

        table.getTableHeader()
                .setBackground(
                        TABLE_HEADER
                );

        table.getTableHeader()
                .setForeground(
                        WHITE
                );

        table.getTableHeader()
                .setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                12
                        )
                );
    }

    // =========================================================
    // MESSAGE HELPERS
    // =========================================================

    private void showWarning(
            String message,
            String title
    ) {

        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void showInfo(
            String message,
            String title
    ) {

        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showDatabaseError(
            String message,
            Exception e
    ) {

        JOptionPane.showMessageDialog(
                this,
                message
                        + "\n\n"
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

                    AdminUi adminUi =
                            new AdminUi();

                    adminUi.setVisible(true);
                }
        );
    }
}
