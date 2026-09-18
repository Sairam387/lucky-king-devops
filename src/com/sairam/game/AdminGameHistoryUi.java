package com.sairam.game;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * ============================================================
 * LUCKY KING - ADMIN GAME HISTORY
 * ============================================================
 *
 * Uses existing game_history table.
 *
 * Database columns:
 *
 * game_id
 * player_name
 * selected_type
 * selected_value
 * result_number
 * result_color
 * bet_amount
 * payout
 * status
 * played_at
 *
 * ============================================================
 */
public class AdminGameHistoryUi extends JFrame {

    private static final long serialVersionUID = 1L;

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color BG =
            new Color(246, 243, 250);

    private static final Color CARD_BG =
            Color.WHITE;

    private static final Color PURPLE_DARK =
            new Color(55, 24, 88);

    private static final Color PURPLE =
            new Color(89, 45, 132);

    private static final Color PURPLE_LIGHT =
            new Color(121, 73, 170);

    private static final Color GOLD =
            new Color(218, 165, 32);

    private static final Color GOLD_LIGHT =
            new Color(244, 205, 92);

    private static final Color TEXT_DARK =
            new Color(45, 39, 52);

    private static final Color TEXT_GRAY =
            new Color(112, 105, 120);

    private static final Color BORDER =
            new Color(225, 218, 232);

    private static final Color GREEN =
            new Color(35, 150, 90);

    private static final Color GREEN_LIGHT =
            new Color(232, 247, 238);

    private static final Color RED =
            new Color(205, 65, 70);

    private static final Color RED_LIGHT =
            new Color(253, 237, 238);

    private static final Color BLUE =
            new Color(55, 105, 185);

    private static final Color BLUE_LIGHT =
            new Color(235, 242, 252);

    private static final Color ORANGE =
            new Color(220, 125, 35);

    private static final Color ORANGE_LIGHT =
            new Color(253, 243, 230);

    private static final Color TABLE_HEADER =
            new Color(69, 38, 102);

    private static final Color TABLE_ALT =
            new Color(250, 248, 252);

    // =========================================================
    // COMPONENTS
    // =========================================================

    private JPanel mainPanel;

    private JTextField playerSearchField;

    private JComboBox<String> statusComboBox;

    private JTable historyTable;

    private DefaultTableModel tableModel;

    private JLabel totalGamesValue;

    private JLabel totalWinsValue;

    private JLabel totalLossesValue;

    private JLabel totalBetValue;

    private JLabel totalPayoutValue;

    private JLabel profitLossValue;

    private JLabel recordCountLabel;

    private JLabel lastUpdatedLabel;

    private Timer searchTimer;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public AdminGameHistoryUi() {

        setTitle("Lucky King - Admin Game History");

        setSize(1450, 820);

        setMinimumSize(
                new Dimension(
                        1180,
                        700
                )
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setResizable(true);

        initializeUi();

        loadDashboard();

        loadHistory();
    }

    // =========================================================
    // INITIALIZE UI
    // =========================================================

    private void initializeUi() {

        mainPanel =
                new JPanel(
                        new BorderLayout()
                );

        mainPanel.setBackground(BG);

        setContentPane(mainPanel);

        mainPanel.add(
                createHeader(),
                BorderLayout.NORTH
        );

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout(
                                0,
                                16
                        )
                );

        centerPanel.setBackground(BG);

        centerPanel.setBorder(
                new EmptyBorder(
                        18,
                        22,
                        22,
                        22
                )
        );

        centerPanel.add(
                createSummaryPanel(),
                BorderLayout.NORTH
        );

        centerPanel.add(
                createHistorySection(),
                BorderLayout.CENTER
        );

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );
    }

    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeader() {

        GradientPanel header =
                new GradientPanel(
                        PURPLE_DARK,
                        PURPLE
                );

        header.setLayout(
                new BorderLayout()
        );

        header.setPreferredSize(
                new Dimension(
                        100,
                        105
                )
        );

        header.setBorder(
                new EmptyBorder(
                        16,
                        25,
                        16,
                        25
                )
        );

        // -----------------------------------------------------
        // LEFT
        // -----------------------------------------------------

        JPanel left =
                new JPanel(
                        new BorderLayout(
                                14,
                                0
                        )
                );

        left.setOpaque(false);

        BoxIcon logo =
                new BoxIcon(
                        GOLD,
                        Color.WHITE,
                        58,
                        "LK"
                );

        left.add(
                logo,
                BorderLayout.WEST
        );

        JPanel titlePanel =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        titlePanel.setOpaque(false);

        JLabel title =
                new JLabel(
                        "LUCKY KING"
                );

        title.setForeground(
                Color.WHITE
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        25
                )
        );

        JLabel subtitle =
                new JLabel(
                        "ADMIN • GAME HISTORY"
                );

        subtitle.setForeground(
                GOLD_LIGHT
        );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        titlePanel.add(title);

        titlePanel.add(subtitle);

        left.add(
                titlePanel,
                BorderLayout.CENTER
        );

        header.add(
                left,
                BorderLayout.WEST
        );

        // -----------------------------------------------------
        // RIGHT
        // -----------------------------------------------------

        JPanel right =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        right.setOpaque(false);

        JLabel adminLabel =
                new JLabel(
                        "ADMIN CONTROL PANEL",
                        SwingConstants.RIGHT
                );

        adminLabel.setForeground(
                Color.WHITE
        );

        adminLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        lastUpdatedLabel =
                new JLabel(
                        "Loading...",
                        SwingConstants.RIGHT
                );

        lastUpdatedLabel.setForeground(
                GOLD_LIGHT
        );

        lastUpdatedLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        right.add(adminLabel);

        right.add(lastUpdatedLabel);

        header.add(
                right,
                BorderLayout.EAST
        );

        return header;
    }

    // =========================================================
    // SUMMARY PANEL
    // =========================================================

    private JPanel createSummaryPanel() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                1,
                                6,
                                12,
                                0
                        )
                );

        panel.setOpaque(false);

        totalGamesValue =
                createValueLabel(
                        "0"
                );

        totalWinsValue =
                createValueLabel(
                        "0"
                );

        totalLossesValue =
                createValueLabel(
                        "0"
                );

        totalBetValue =
                createValueLabel(
                        "Rs. 0"
                );

        totalPayoutValue =
                createValueLabel(
                        "Rs. 0"
                );

        profitLossValue =
                createValueLabel(
                        "Rs. 0"
                );

        panel.add(
                createSummaryCard(
                        "TOTAL GAMES",
                        totalGamesValue,
                        new BoxIcon(
                                PURPLE,
                                Color.WHITE,
                                42,
                                "G"
                        ),
                        PURPLE
                )
        );

        panel.add(
                createSummaryCard(
                        "TOTAL WINS",
                        totalWinsValue,
                        new BoxIcon(
                                GREEN,
                                Color.WHITE,
                                42,
                                "W"
                        ),
                        GREEN
                )
        );

        panel.add(
                createSummaryCard(
                        "TOTAL LOSSES",
                        totalLossesValue,
                        new BoxIcon(
                                RED,
                                Color.WHITE,
                                42,
                                "X"
                        ),
                        RED
                )
        );

        panel.add(
                createSummaryCard(
                        "TOTAL BET",
                        totalBetValue,
                        new BoxIcon(
                                BLUE,
                                Color.WHITE,
                                42,
                                "B"
                        ),
                        BLUE
                )
        );

        panel.add(
                createSummaryCard(
                        "TOTAL PAYOUT",
                        totalPayoutValue,
                        new BoxIcon(
                                ORANGE,
                                Color.WHITE,
                                42,
                                "P"
                        ),
                        ORANGE
                )
        );

        panel.add(
                createSummaryCard(
                        "PROFIT / LOSS",
                        profitLossValue,
                        new BoxIcon(
                                GOLD,
                                PURPLE_DARK,
                                42,
                                "L"
                        ),
                        GOLD
                )
        );

        return panel;
    }

    // =========================================================
    // VALUE LABEL
    // =========================================================

    private JLabel createValueLabel(
            String value
    ) {

        JLabel label =
                new JLabel(
                        value,
                        SwingConstants.RIGHT
                );

        label.setForeground(
                TEXT_DARK
        );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        return label;
    }

    // =========================================================
    // SUMMARY CARD
    // =========================================================

    private JPanel createSummaryCard(
            String title,
            JLabel valueLabel,
            JComponent icon,
            Color accent
    ) {

        RoundedPanel card =
                new RoundedPanel(
                        18,
                        CARD_BG
                );

        card.setLayout(
                new BorderLayout(
                        12,
                        0
                )
        );

        card.setBorder(
                new EmptyBorder(
                        15,
                        14,
                        15,
                        14
                )
        );

        JPanel iconWrapper =
                new JPanel(
                        new GridBagLayout()
                );

        iconWrapper.setOpaque(false);

        iconWrapper.add(icon);

        card.add(
                iconWrapper,
                BorderLayout.WEST
        );

        JPanel textPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                1,
                                0,
                                4
                        )
                );

        textPanel.setOpaque(false);

        JLabel titleLabel =
                new JLabel(
                        title
                );

        titleLabel.setForeground(
                TEXT_GRAY
        );

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        valueLabel.setForeground(
                accent
        );

        textPanel.add(
                titleLabel
        );

        textPanel.add(
                valueLabel
        );

        card.add(
                textPanel,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // HISTORY SECTION
    // =========================================================

    private JPanel createHistorySection() {

        RoundedPanel section =
                new RoundedPanel(
                        18,
                        Color.WHITE
                );

        section.setLayout(
                new BorderLayout(
                        0,
                        12
                )
        );

        section.setBorder(
                new EmptyBorder(
                        18,
                        18,
                        18,
                        18
                )
        );

        // -----------------------------------------------------
        // TOP
        // -----------------------------------------------------

        JPanel top =
                new JPanel(
                        new BorderLayout(
                                0,
                                12
                        )
                );

        top.setOpaque(false);

        JPanel titlePanel =
                new JPanel(
                        new BorderLayout()
                );

        titlePanel.setOpaque(false);

        JPanel titleLeft =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                0,
                                0
                        )
                );

        titleLeft.setOpaque(false);

        JLabel title =
                new JLabel(
                        "GAME HISTORY"
                );

        title.setForeground(
                TEXT_DARK
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        recordCountLabel =
                new JLabel(
                        "0 records"
                );

        recordCountLabel.setForeground(
                TEXT_GRAY
        );

        recordCountLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        titleLeft.add(title);

        titleLeft.add(
                Box.createHorizontalStrut(
                        12
                )
        );

        titleLeft.add(
                recordCountLabel
        );

        titlePanel.add(
                titleLeft,
                BorderLayout.WEST
        );

        top.add(
                titlePanel,
                BorderLayout.NORTH
        );

        // -----------------------------------------------------
        // FILTER BAR
        // -----------------------------------------------------

        JPanel filterPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                8,
                                0
                        )
                );

        filterPanel.setOpaque(false);

        JLabel playerLabel =
                new JLabel(
                        "PLAYER"
                );

        playerLabel.setForeground(
                TEXT_GRAY
        );

        playerLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        filterPanel.add(
                playerLabel
        );

        playerSearchField =
                new JTextField();

        playerSearchField.setPreferredSize(
                new Dimension(
                        220,
                        38
                )
        );

        playerSearchField.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        playerSearchField.setForeground(
                TEXT_DARK
        );

        playerSearchField.setBackground(
                Color.WHITE
        );

        playerSearchField.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                BORDER,
                                1,
                                true
                        ),
                        new EmptyBorder(
                                0,
                                12,
                                0,
                                12
                        )
                )
        );

        filterPanel.add(
                playerSearchField
        );

        JLabel statusLabel =
                new JLabel(
                        "STATUS"
                );

        statusLabel.setForeground(
                TEXT_GRAY
        );

        statusLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        filterPanel.add(
                statusLabel
        );

        statusComboBox =
                new JComboBox<>(
                        new String[]{
                                "ALL",
                                "WON",
                                "LOST"
                        }
                );

        statusComboBox.setPreferredSize(
                new Dimension(
                        125,
                        38
                )
        );

        statusComboBox.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        statusComboBox.setBackground(
                Color.WHITE
        );

        filterPanel.add(
                statusComboBox
        );

        JButton searchButton =
                createActionButton(
                        "SEARCH",
                        PURPLE
                );

        JButton clearButton =
                createActionButton(
                        "CLEAR",
                        TEXT_GRAY
                );

        JButton refreshButton =
                createActionButton(
                        "REFRESH",
                        GOLD
                );

        searchButton.addActionListener(
                e -> loadHistory()
        );

        clearButton.addActionListener(
                e -> {

                    playerSearchField.setText("");

                    statusComboBox.setSelectedIndex(
                            0
                    );

                    loadHistory();
                }
        );

        refreshButton.addActionListener(
                e -> {

                    loadDashboard();

                    loadHistory();
                }
        );

        filterPanel.add(
                searchButton
        );

        filterPanel.add(
                clearButton
        );

        filterPanel.add(
                refreshButton
        );

        top.add(
                filterPanel,
                BorderLayout.CENTER
        );

        section.add(
                top,
                BorderLayout.NORTH
        );

        // -----------------------------------------------------
        // TABLE MODEL
        // -----------------------------------------------------

        tableModel =
                new DefaultTableModel() {

                    private static final long serialVersionUID =
                            1L;

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }

                    @Override
                    public Class<?> getColumnClass(
                            int column
                    ) {

                        switch (column) {

                            case 0:
                            case 4:
                            case 6:
                            case 7:
                                return Integer.class;

                            default:
                                return String.class;
                        }
                    }
                };

        String[] columns = {

                "GAME ID",
                "PLAYER NAME",
                "SELECTED TYPE",
                "SELECTED VALUE",
                "RESULT NUMBER",
                "RESULT COLOR",
                "BET AMOUNT",
                "PAYOUT",
                "STATUS",
                "DATE / TIME"
        };

        tableModel.setColumnIdentifiers(
                columns
        );

        historyTable =
                new JTable(
                        tableModel
                );

        styleTable();

        JScrollPane scrollPane =
                new JScrollPane(
                        historyTable
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        scrollPane.getViewport()
                .setBackground(
                        Color.WHITE
                );

        section.add(
                scrollPane,
                BorderLayout.CENTER
        );

        installSearchListeners();

        return section;
    }

    // =========================================================
    // BUTTON
    // =========================================================

    private JButton createActionButton(
            String text,
            Color accent
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setPreferredSize(
                new Dimension(
                        105,
                        38
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

        button.setBackground(
                accent
        );

        button.setForeground(
                Color.WHITE
        );

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
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
                                accent.darker()
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                accent
                        );
                    }
                }
        );

        return button;
    }

    // =========================================================
    // TABLE STYLE
    // =========================================================

    private void styleTable() {

        historyTable.setRowHeight(
                36
        );

        historyTable.setShowGrid(
                true
        );

        historyTable.setGridColor(
                new Color(
                        235,
                        230,
                        238
                )
        );

        historyTable.setIntercellSpacing(
                new Dimension(
                        1,
                        1
                )
        );

        historyTable.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        historyTable.setForeground(
                TEXT_DARK
        );

        historyTable.setBackground(
                Color.WHITE
        );

        historyTable.setSelectionBackground(
                new Color(
                        235,
                        225,
                        245
                )
        );

        historyTable.setSelectionForeground(
                TEXT_DARK
        );

        historyTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        JTableHeader header =
                historyTable.getTableHeader();

        header.setPreferredSize(
                new Dimension(
                        100,
                        42
                )
        );

        header.setBackground(
                TABLE_HEADER
        );

        header.setForeground(
                Color.WHITE
        );

        header.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        header.setReorderingAllowed(
                false
        );

        int[] widths = {

                80,
                150,
                145,
                150,
                120,
                120,
                130,
                130,
                150,
                175
        };

        for (
                int i = 0;
                i < widths.length;
                i++
        ) {

            historyTable
                    .getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(
                            widths[i]
                    );
        }

        historyTable.setDefaultRenderer(
                Object.class,
                new HistoryCellRenderer()
        );

        historyTable.setDefaultRenderer(
                Integer.class,
                new NumberCellRenderer()
        );

        historyTable
                .getColumnModel()
                .getColumn(6)
                .setCellRenderer(
                        new CurrencyRenderer()
                );

        historyTable
                .getColumnModel()
                .getColumn(7)
                .setCellRenderer(
                        new CurrencyRenderer()
                );

        historyTable
                .getColumnModel()
                .getColumn(8)
                .setCellRenderer(
                        new StatusRenderer()
                );

        historyTable
                .getColumnModel()
                .getColumn(9)
                .setCellRenderer(
                        new DateRenderer()
                );

        historyTable
                .getColumnModel()
                .getColumn(5)
                .setCellRenderer(
                        new ResultColorRenderer()
                );
    }

    // =========================================================
    // SEARCH LISTENERS
    // =========================================================

    private void installSearchListeners() {

        searchTimer =
                new Timer(
                        450,
                        e -> loadHistory()
                );

        searchTimer.setRepeats(
                false
        );

        playerSearchField
                .getDocument()
                .addDocumentListener(
                        new DocumentListener() {

                            @Override
                            public void insertUpdate(
                                    DocumentEvent e
                            ) {

                                scheduleSearch();
                            }

                            @Override
                            public void removeUpdate(
                                    DocumentEvent e
                            ) {

                                scheduleSearch();
                            }

                            @Override
                            public void changedUpdate(
                                    DocumentEvent e
                            ) {

                                scheduleSearch();
                            }
                        }
                );

        statusComboBox.addActionListener(
                e -> loadHistory()
        );
    }

    // =========================================================
    // SEARCH TIMER
    // =========================================================

    private void scheduleSearch() {

        if (searchTimer != null) {

            searchTimer.restart();
        }
    }

    // =========================================================
    // LOAD DASHBOARD
    // =========================================================

    private void loadDashboard() {

        String sql =
                "SELECT "
                        + "COUNT(*) AS total_games, "
                        + "COALESCE("
                        + "SUM("
                        + "CASE "
                        + "WHEN UPPER(status) = 'WON' "
                        + "THEN 1 ELSE 0 "
                        + "END"
                        + "), 0"
                        + ") AS total_wins, "
                        + "COALESCE("
                        + "SUM("
                        + "CASE "
                        + "WHEN UPPER(status) = 'LOST' "
                        + "THEN 1 ELSE 0 "
                        + "END"
                        + "), 0"
                        + ") AS total_losses, "
                        + "COALESCE("
                        + "SUM(bet_amount), 0"
                        + ") AS total_bet, "
                        + "COALESCE("
                        + "SUM(payout), 0"
                        + ") AS total_payout "
                        + "FROM game_history";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        );

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {

                int totalGames =
                        resultSet.getInt(
                                "total_games"
                        );

                int totalWins =
                        resultSet.getInt(
                                "total_wins"
                        );

                int totalLosses =
                        resultSet.getInt(
                                "total_losses"
                        );

                int totalBet =
                        resultSet.getInt(
                                "total_bet"
                        );

                int totalPayout =
                        resultSet.getInt(
                                "total_payout"
                        );

                int profitLoss =
                        totalPayout
                                - totalBet;

                totalGamesValue.setText(
                        String.valueOf(
                                totalGames
                        )
                );

                totalWinsValue.setText(
                        String.valueOf(
                                totalWins
                        )
                );

                totalLossesValue.setText(
                        String.valueOf(
                                totalLosses
                        )
                );

                totalBetValue.setText(
                        formatCurrency(
                                totalBet
                        )
                );

                totalPayoutValue.setText(
                        formatCurrency(
                                totalPayout
                        )
                );

                profitLossValue.setText(
                        formatCurrency(
                                profitLoss
                        )
                );

                if (profitLoss >= 0) {

                    profitLossValue.setForeground(
                            GREEN
                    );

                } else {

                    profitLossValue.setForeground(
                            RED
                    );
                }
            }

            lastUpdatedLabel.setText(
                    "Updated: "
                            + new SimpleDateFormat(
                                    "dd MMM yyyy • hh:mm:ss a",
                                    Locale.ENGLISH
                            ).format(
                                    new java.util.Date()
                            )
            );

        } catch (SQLException e) {

            e.printStackTrace();

            setDashboardValuesToZero();

            lastUpdatedLabel.setText(
                    "Database unavailable"
            );
        }
    }

    // =========================================================
    // LOAD HISTORY
    // =========================================================

    private void loadHistory() {

        String playerName =
                playerSearchField == null
                        ? ""
                        : playerSearchField
                                .getText()
                                .trim();

        String status =
                statusComboBox == null
                        ? "ALL"
                        : String.valueOf(
                                statusComboBox
                                        .getSelectedItem()
                        );

        StringBuilder sql =
                new StringBuilder();

        /*
         * IMPORTANT:
         *
         * Your PostgreSQL table uses PLAYED_AT.
         * Do NOT use CREATED_AT here.
         */
        sql.append(
                "SELECT "
                        + "game_id, "
                        + "player_name, "
                        + "selected_type, "
                        + "selected_value, "
                        + "result_number, "
                        + "result_color, "
                        + "bet_amount, "
                        + "payout, "
                        + "status, "
                        + "played_at "
                        + "FROM game_history "
                        + "WHERE 1 = 1 "
        );

        List<Object> parameters =
                new ArrayList<>();

        if (!playerName.isEmpty()) {

            sql.append(
                    "AND LOWER(player_name) "
                            + "LIKE LOWER(?) "
            );

            parameters.add(
                    "%"
                            + playerName
                            + "%"
            );
        }

        if (
                !"ALL".equalsIgnoreCase(
                        status
                )
        ) {

            sql.append(
                    "AND UPPER(status) "
                            + "= UPPER(?) "
            );

            parameters.add(
                    status
            );
        }

        sql.append(
                "ORDER BY game_id DESC"
        );

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql.toString()
                        )
        ) {

            for (
                    int i = 0;
                    i < parameters.size();
                    i++
            ) {

                statement.setObject(
                        i + 1,
                        parameters.get(i)
                );
            }

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                tableModel.setRowCount(
                        0
                );

                int count = 0;

                while (
                        resultSet.next()
                ) {

                    /*
                     * IMPORTANT:
                     *
                     * The timestamp is PLAYED_AT.
                     */

                    Object[] row = {

                            resultSet.getInt(
                                    "game_id"
                            ),

                            safeString(
                                    resultSet.getString(
                                            "player_name"
                                    )
                            ),

                            safeString(
                                    resultSet.getString(
                                            "selected_type"
                                    )
                            ),

                            safeString(
                                    resultSet.getString(
                                            "selected_value"
                                    )
                            ),

                            resultSet.getInt(
                                    "result_number"
                            ),

                            safeString(
                                    resultSet.getString(
                                            "result_color"
                                    )
                            ),

                            resultSet.getInt(
                                    "bet_amount"
                            ),

                            resultSet.getInt(
                                    "payout"
                            ),

                            safeString(
                                    resultSet.getString(
                                            "status"
                                    )
                            ),

                            resultSet.getTimestamp(
                                    "played_at"
                            )
                    };

                    tableModel.addRow(
                            row
                    );

                    count++;
                }

                recordCountLabel.setText(
                        count
                                + (
                                count == 1
                                        ? " record"
                                        : " records"
                        )
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();

            tableModel.setRowCount(
                    0
            );

            recordCountLabel.setText(
                    "0 records"
            );
        }
    }

    // =========================================================
    // ZERO VALUES
    // =========================================================

    private void setDashboardValuesToZero() {

        if (totalGamesValue != null) {

            totalGamesValue.setText(
                    "0"
            );
        }

        if (totalWinsValue != null) {

            totalWinsValue.setText(
                    "0"
            );
        }

        if (totalLossesValue != null) {

            totalLossesValue.setText(
                    "0"
            );
        }

        if (totalBetValue != null) {

            totalBetValue.setText(
                    "Rs. 0"
            );
        }

        if (totalPayoutValue != null) {

            totalPayoutValue.setText(
                    "Rs. 0"
            );
        }

        if (profitLossValue != null) {

            profitLossValue.setText(
                    "Rs. 0"
            );

            profitLossValue.setForeground(
                    GOLD
            );
        }
    }

    // =========================================================
    // CURRENCY
    // =========================================================

    private String formatCurrency(
            int amount
    ) {

        return "Rs. "
                + String.format(
                        Locale.US,
                        "%,d",
                        amount
                );
    }

    // =========================================================
    // SAFE STRING
    // =========================================================

    private String safeString(
            String value
    ) {

        return value == null
                ? ""
                : value;
    }

    // =========================================================
    // REAL BOX ICON
    // =========================================================

    private static class BoxIcon
            extends JPanel {

        private static final long serialVersionUID =
                1L;

        private final Color borderColor;

        private final Color fillColor;

        private final int size;

        private final String text;

        BoxIcon(
                Color borderColor,
                Color fillColor,
                int size,
                String text
        ) {

            this.borderColor =
                    borderColor;

            this.fillColor =
                    fillColor;

            this.size =
                    size;

            this.text =
                    text;

            setOpaque(false);

            setPreferredSize(
                    new Dimension(
                            size,
                            size
                    )
            );

            setMinimumSize(
                    new Dimension(
                            size,
                            size
                    )
            );

            setMaximumSize(
                    new Dimension(
                            size,
                            size
                    )
            );
        }

        @Override
        protected void paintComponent(
                Graphics graphics
        ) {

            Graphics2D g =
                    (Graphics2D)
                            graphics.create();

            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int padding = 2;

            // Outer box
            g.setColor(
                    borderColor
            );

            g.fillRoundRect(
                    padding,
                    padding,
                    size - 4,
                    size - 4,
                    12,
                    12
            );

            // Inner box
            g.setColor(
                    fillColor
            );

            g.fillRoundRect(
                    padding + 3,
                    padding + 3,
                    size - 10,
                    size - 10,
                    9,
                    9
            );

            // Text inside box
            g.setColor(
                    borderColor
            );

            g.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            Math.max(
                                    11,
                                    size / 4
                            )
                    )
            );

            FontMetrics fm =
                    g.getFontMetrics();

            int textWidth =
                    fm.stringWidth(
                            text
                    );

            int textHeight =
                    fm.getAscent();

            int x =
                    (size - textWidth)
                            / 2;

            int y =
                    (size - textHeight)
                            / 2
                            + textHeight;

            g.drawString(
                    text,
                    x,
                    y
            );

            g.dispose();
        }
    }

    // =========================================================
    // ROUNDED PANEL
    // =========================================================

    private static class RoundedPanel
            extends JPanel {

        private static final long serialVersionUID =
                1L;

        private final int radius;

        private final Color backgroundColor;

        RoundedPanel(
                int radius,
                Color backgroundColor
        ) {

            this.radius =
                    radius;

            this.backgroundColor =
                    backgroundColor;

            setOpaque(false);
        }

        @Override
        protected void paintComponent(
                Graphics graphics
        ) {

            Graphics2D g =
                    (Graphics2D)
                            graphics.create();

            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g.setColor(
                    backgroundColor
            );

            g.fillRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    radius,
                    radius
            );

            g.setColor(
                    BORDER
            );

            g.drawRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    radius,
                    radius
            );

            g.dispose();

            super.paintComponent(
                    graphics
            );
        }
    }

    // =========================================================
    // GRADIENT PANEL
    // =========================================================

    private static class GradientPanel
            extends JPanel {

        private static final long serialVersionUID =
                1L;

        private final Color color1;

        private final Color color2;

        GradientPanel(
                Color color1,
                Color color2
        ) {

            this.color1 =
                    color1;

            this.color2 =
                    color2;

            setOpaque(false);
        }

        @Override
        protected void paintComponent(
                Graphics graphics
        ) {

            Graphics2D g =
                    (Graphics2D)
                            graphics.create();

            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            GradientPaint gradient =
                    new GradientPaint(
                            0,
                            0,
                            color1,
                            getWidth(),
                            getHeight(),
                            color2
                    );

            g.setPaint(
                    gradient
            );

            g.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );

            g.dispose();

            super.paintComponent(
                    graphics
            );
        }
    }

    // =========================================================
    // TABLE CELL RENDERER
    // =========================================================

    private static class HistoryCellRenderer
            extends DefaultTableCellRenderer {

        private static final long serialVersionUID =
                1L;

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            super.getTableCellRendererComponent(
                    table,
                    value,
                    isSelected,
                    hasFocus,
                    row,
                    column
            );

            setBorder(
                    new EmptyBorder(
                            0,
                            8,
                            0,
                            8
                    )
            );

            setHorizontalAlignment(
                    SwingConstants.LEFT
            );

            if (!isSelected) {

                if (row % 2 == 0) {

                    setBackground(
                            Color.WHITE
                    );

                } else {

                    setBackground(
                            TABLE_ALT
                    );
                }
            }

            setForeground(
                    TEXT_DARK
            );

            return this;
        }
    }

    // =========================================================
    // NUMBER RENDERER
    // =========================================================

    private static class NumberCellRenderer
            extends HistoryCellRenderer {

        private static final long serialVersionUID =
                1L;

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            super.getTableCellRendererComponent(
                    table,
                    value,
                    isSelected,
                    hasFocus,
                    row,
                    column
            );

            setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            return this;
        }
    }

    // =========================================================
    // CURRENCY RENDERER
    // =========================================================

    private static class CurrencyRenderer
            extends HistoryCellRenderer {

        private static final long serialVersionUID =
                1L;

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            super.getTableCellRendererComponent(
                    table,
                    value,
                    isSelected,
                    hasFocus,
                    row,
                    column
            );

            int amount = 0;

            if (value instanceof Number) {

                amount =
                        ((Number) value)
                                .intValue();
            }

            setText(
                    "Rs. "
                            + String.format(
                                    Locale.US,
                                    "%,d",
                                    amount
                            )
            );

            setHorizontalAlignment(
                    SwingConstants.RIGHT
            );

            setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            12
                    )
            );

            return this;
        }
    }

    // =========================================================
    // STATUS RENDERER
    // =========================================================

    private static class StatusRenderer
            extends DefaultTableCellRenderer {

        private static final long serialVersionUID =
                1L;

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            String status =
                    value == null
                            ? ""
                            : value.toString();

            JLabel label =
                    new JLabel(
                            status,
                            SwingConstants.CENTER
                    );

            label.setOpaque(true);

            label.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            11
                    )
            );

            label.setBorder(
                    new EmptyBorder(
                            5,
                            8,
                            5,
                            8
                    )
            );

            if (
                    status.equalsIgnoreCase(
                            "WON"
                    )
                    ||
                    status.toUpperCase(
                            Locale.ENGLISH
                    ).contains(
                            "WIN"
                    )
            ) {

                label.setForeground(
                        GREEN
                );

                label.setBackground(
                        GREEN_LIGHT
                );

            } else if (
                    status.equalsIgnoreCase(
                            "LOST"
                    )
                    ||
                    status.toUpperCase(
                            Locale.ENGLISH
                    ).contains(
                            "LOSS"
                    )
            ) {

                label.setForeground(
                        RED
                );

                label.setBackground(
                        RED_LIGHT
                );

            } else {

                label.setForeground(
                        TEXT_GRAY
                );

                label.setBackground(
                        new Color(
                                242,
                                239,
                                245
                        )
                );
            }

            return label;
        }
    }

    // =========================================================
    // RESULT COLOR RENDERER
    // =========================================================

    private static class ResultColorRenderer
            extends DefaultTableCellRenderer {

        private static final long serialVersionUID =
                1L;

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            String resultColor =
                    value == null
                            ? ""
                            : value.toString();

            JLabel label =
                    new JLabel(
                            resultColor,
                            SwingConstants.CENTER
                    );

            label.setOpaque(true);

            label.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            11
                    )
            );

            label.setBorder(
                    new EmptyBorder(
                            5,
                            8,
                            5,
                            8
                    )
            );

            if (
                    resultColor.equalsIgnoreCase(
                            "RED"
                    )
            ) {

                label.setForeground(
                        RED
                );

                label.setBackground(
                        RED_LIGHT
                );

            } else if (
                    resultColor.equalsIgnoreCase(
                            "BLUE"
                    )
            ) {

                label.setForeground(
                        BLUE
                );

                label.setBackground(
                        BLUE_LIGHT
                );

            } else if (
                    resultColor.equalsIgnoreCase(
                            "YELLOW"
                    )
            ) {

                label.setForeground(
                        new Color(
                                160,
                                120,
                                0
                        )
                );

                label.setBackground(
                        new Color(
                                255,
                                248,
                                215
                        )
                );

            } else {

                label.setForeground(
                        TEXT_DARK
                );

                label.setBackground(
                        Color.WHITE
                );
            }

            return label;
        }
    }

    // =========================================================
    // DATE RENDERER
    // =========================================================

    private static class DateRenderer
            extends DefaultTableCellRenderer {

        private static final long serialVersionUID =
                1L;

        private final SimpleDateFormat formatter =
                new SimpleDateFormat(
                        "dd MMM yyyy  hh:mm a",
                        Locale.ENGLISH
                );

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            super.getTableCellRendererComponent(
                    table,
                    value,
                    isSelected,
                    hasFocus,
                    row,
                    column
            );

            if (value instanceof Timestamp) {

                setText(
                        formatter.format(
                                (Timestamp) value
                        )
                );
            }

            setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            11
                    )
            );

            return this;
        }
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

                        UIManager.setLookAndFeel(
                                UIManager
                                        .getSystemLookAndFeelClassName()
                        );

                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                    AdminGameHistoryUi ui =
                            new AdminGameHistoryUi();

                    ui.setVisible(
                            true
                    );
                }
        );
    }
}