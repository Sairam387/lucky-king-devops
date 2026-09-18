package com.sairam.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
import javax.swing.table.TableCellRenderer;

public class WalletTransactionUi extends JFrame {

    private static final long serialVersionUID = 1L;

    // =========================================================
    // COLORS - LUCKY KING THEME
    // =========================================================

    private static final Color PURPLE_DARK =
            new Color(55, 25, 85);

    private static final Color PURPLE =
            new Color(88, 45, 135);

    private static final Color PURPLE_LIGHT =
            new Color(122, 72, 175);

    private static final Color GOLD =
            new Color(218, 165, 32);

    private static final Color GOLD_LIGHT =
            new Color(245, 211, 110);

    private static final Color BLACK =
            new Color(24, 24, 24);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color LIGHT_BG =
            new Color(246, 243, 250);

    private static final Color CARD_BG =
            Color.WHITE;

    private static final Color DARK_TEXT =
            new Color(43, 38, 50);

    private static final Color GREY_TEXT =
            new Color(112, 104, 120);

    private static final Color BORDER =
            new Color(225, 217, 232);

    private static final Color GREEN =
            new Color(35, 145, 85);

    private static final Color GREEN_LIGHT =
            new Color(232, 247, 238);

    private static final Color RED =
            new Color(205, 65, 70);

    private static final Color RED_LIGHT =
            new Color(253, 237, 238);

    private static final Color BLUE =
            new Color(55, 105, 190);

    private static final Color BLUE_LIGHT =
            new Color(235, 242, 252);

    private static final Color ORANGE =
            new Color(220, 125, 35);

    private static final Color ORANGE_LIGHT =
            new Color(255, 244, 228);

    // =========================================================
    // CURRENCY
    // =========================================================

    private static final NumberFormat CURRENCY_FORMAT =
            NumberFormat.getCurrencyInstance(
                    new Locale("en", "IN")
            );

    // =========================================================
    // FIELDS
    // =========================================================

    private JTextField searchField;

    private JTable transactionTable;

    private DefaultTableModel tableModel;

    private WalletTransactionDao transactionDao;

    private JLabel countLabel;

    private JLabel searchStatusLabel;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public WalletTransactionUi() {

        transactionDao =
                new WalletTransactionDao();

        setTitle(
                "Lucky King - Wallet Transactions"
        );

        setSize(
                1380,
                760
        );

        setMinimumSize(
                new Dimension(
                        1150,
                        650
                )
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setResizable(true);

        buildUi();

        loadAllTransactions();
    }

    // =========================================================
    // BUILD UI
    // =========================================================

    private void buildUi() {

        JPanel root =
                new JPanel(
                        new BorderLayout()
                );

        root.setBackground(
                LIGHT_BG
        );

        root.add(
                createHeaderPanel(),
                BorderLayout.NORTH
        );

        root.add(
                createCenterPanel(),
                BorderLayout.CENTER
        );

        root.add(
                createBottomPanel(),
                BorderLayout.SOUTH
        );

        setContentPane(root);
    }

    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeaderPanel() {

        GradientPanel header =
                new GradientPanel(
                        PURPLE_DARK,
                        PURPLE
                );

        header.setPreferredSize(
                new Dimension(
                        1380,
                        105
                )
        );

        header.setLayout(
                new BorderLayout()
        );

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        18,
                        25,
                        18,
                        25
                )
        );

        // -----------------------------------------------------
        // LEFT SECTION
        // -----------------------------------------------------

        JPanel leftPanel =
                new JPanel(
                        new BorderLayout(
                                15,
                                0
                        )
                );

        leftPanel.setOpaque(false);

        RoundIcon logo =
                new RoundIcon(
                        GOLD,
                        BLACK,
                        62,
                        "LK"
                );

        leftPanel.add(
                logo,
                BorderLayout.WEST
        );

        JPanel titlePanel =
                new JPanel(
                        new GridBagLayout()
                );

        titlePanel.setOpaque(false);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor =
                GridBagConstraints.WEST;

        gbc.insets =
                new Insets(
                        0,
                        0,
                        4,
                        0
                );

        JLabel title =
                new JLabel(
                        "WALLET TRANSACTIONS"
                );

        title.setForeground(
                WHITE
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        25
                )
        );

        titlePanel.add(
                title,
                gbc
        );

        gbc.gridy = 1;

        JLabel subtitle =
                new JLabel(
                        "Lucky King • Admin Transaction Management"
                );

        subtitle.setForeground(
                GOLD_LIGHT
        );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        titlePanel.add(
                subtitle,
                gbc
        );

        leftPanel.add(
                titlePanel,
                BorderLayout.CENTER
        );

        header.add(
                leftPanel,
                BorderLayout.WEST
        );

        // -----------------------------------------------------
        // RIGHT SECTION
        // -----------------------------------------------------

        JPanel rightPanel =
                new JPanel(
                        new GridBagLayout()
                );

        rightPanel.setOpaque(false);

        GridBagConstraints rightGbc =
                new GridBagConstraints();

        rightGbc.gridx = 0;
        rightGbc.gridy = 0;

        rightGbc.anchor =
                GridBagConstraints.EAST;

        JLabel adminTitle =
                new JLabel(
                        "ADMIN PANEL"
                );

        adminTitle.setForeground(
                GOLD_LIGHT
        );

        adminTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        rightPanel.add(
                adminTitle,
                rightGbc
        );

        rightGbc.gridy = 1;

        JLabel adminStatus =
                new JLabel(
                        "TRANSACTION HISTORY"
                );

        adminStatus.setForeground(
                WHITE
        );

        adminStatus.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        rightPanel.add(
                adminStatus,
                rightGbc
        );

        header.add(
                rightPanel,
                BorderLayout.EAST
        );

        return header;
    }

    // =========================================================
    // CENTER PANEL
    // =========================================================

    private JPanel createCenterPanel() {

        JPanel center =
                new JPanel(
                        new BorderLayout(
                                0,
                                15
                        )
                );

        center.setBackground(
                LIGHT_BG
        );

        center.setBorder(
                BorderFactory.createEmptyBorder(
                        18,
                        25,
                        10,
                        25
                )
        );

        center.add(
                createSearchCard(),
                BorderLayout.NORTH
        );

        createTransactionTable();

        JPanel tableCard =
                new RoundedPanel(
                        18,
                        CARD_BG
                );

        tableCard.setLayout(
                new BorderLayout()
        );

        tableCard.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        transactionTable
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        scrollPane.getViewport()
                .setBackground(
                        WHITE
                );

        tableCard.add(
                scrollPane,
                BorderLayout.CENTER
        );

        center.add(
                tableCard,
                BorderLayout.CENTER
        );

        return center;
    }

    // =========================================================
    // SEARCH CARD
    // =========================================================

    private JPanel createSearchCard() {

        RoundedPanel card =
                new RoundedPanel(
                        18,
                        CARD_BG
                );

        card.setLayout(
                new BorderLayout(
                        15,
                        0
                )
        );

        card.setBorder(
                BorderFactory.createEmptyBorder(
                        14,
                        18,
                        14,
                        18
                )
        );

        // -----------------------------------------------------
        // SEARCH LABEL
        // -----------------------------------------------------

        JPanel labelPanel =
                new JPanel(
                        new GridBagLayout()
                );

        labelPanel.setOpaque(false);

        GridBagConstraints labelGbc =
                new GridBagConstraints();

        labelGbc.gridx = 0;
        labelGbc.gridy = 0;
        labelGbc.anchor =
                GridBagConstraints.WEST;

        JLabel searchTitle =
                new JLabel(
                        "SEARCH PLAYER"
                );

        searchTitle.setForeground(
                DARK_TEXT
        );

        searchTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        labelPanel.add(
                searchTitle,
                labelGbc
        );

        labelGbc.gridy = 1;

        searchStatusLabel =
                new JLabel(
                        "Showing all transactions"
                );

        searchStatusLabel.setForeground(
                GREY_TEXT
        );

        searchStatusLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        10
                )
        );

        labelPanel.add(
                searchStatusLabel,
                labelGbc
        );

        card.add(
                labelPanel,
                BorderLayout.WEST
        );

        // -----------------------------------------------------
        // SEARCH FIELD
        // -----------------------------------------------------

        searchField =
                new JTextField();

        searchField.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        searchField.setForeground(
                DARK_TEXT
        );

        searchField.setBackground(
                WHITE
        );

        searchField.setCaretColor(
                PURPLE
        );

        searchField.setPreferredSize(
                new Dimension(
                        300,
                        40
                )
        );

        searchField.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorderCustom(
                                BORDER,
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                0,
                                12,
                                0,
                                12
                        )
                )
        );

        card.add(
                searchField,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // BUTTONS
        // -----------------------------------------------------

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        buttonPanel.setOpaque(false);

        JButton searchButton =
                createActionButton(
                        "SEARCH",
                        PURPLE,
                        100
                );

        JButton clearButton =
                createActionButton(
                        "CLEAR",
                        new Color(
                                100,
                                94,
                                110
                        ),
                        90
                );

        JButton refreshButton =
                createActionButton(
                        "REFRESH",
                        BLACK,
                        100
                );

        searchButton.addActionListener(
                e -> searchTransactions()
        );

        clearButton.addActionListener(
                e -> {

                    searchField.setText("");

                    loadAllTransactions();
                }
        );

        refreshButton.addActionListener(
                e -> {

                    String currentSearch =
                            searchField
                                    .getText()
                                    .trim();

                    if (
                            currentSearch.isEmpty()
                    ) {

                        loadAllTransactions();

                    } else {

                        searchTransactions();
                    }
                }
        );

        buttonPanel.add(
                searchButton
        );

        buttonPanel.add(
                clearButton
        );

        buttonPanel.add(
                refreshButton
        );

        card.add(
                buttonPanel,
                BorderLayout.EAST
        );

        // -----------------------------------------------------
        // ENTER KEY
        // -----------------------------------------------------

        searchField.addActionListener(
                e -> searchTransactions()
        );

        // -----------------------------------------------------
        // LIVE SEARCH
        // -----------------------------------------------------

        searchField
                .getDocument()
                .addDocumentListener(
                        new DocumentListener() {

                            @Override
                            public void insertUpdate(
                                    DocumentEvent e
                            ) {

                                searchTransactions();
                            }

                            @Override
                            public void removeUpdate(
                                    DocumentEvent e
                            ) {

                                searchTransactions();
                            }

                            @Override
                            public void changedUpdate(
                                    DocumentEvent e
                            ) {

                                searchTransactions();
                            }
                        }
                );

        return card;
    }

    // =========================================================
    // CREATE TRANSACTION TABLE
    // =========================================================

    private void createTransactionTable() {

        String[] columns = {

                "TRANSACTION ID",
                "PLAYER NAME",
                "TRANSACTION TYPE",
                "AMOUNT",
                "BALANCE BEFORE",
                "BALANCE AFTER",
                "DESCRIPTION",
                "DATE / TIME"
        };

        tableModel =
                new DefaultTableModel(
                        columns,
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

        transactionTable =
                new JTable(
                        tableModel
                );

        // -----------------------------------------------------
        // BASIC STYLE
        // -----------------------------------------------------

        transactionTable.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        transactionTable.setForeground(
                DARK_TEXT
        );

        transactionTable.setBackground(
                WHITE
        );

        transactionTable.setRowHeight(
                43
        );

        transactionTable.setGridColor(
                new Color(
                        235,
                        230,
                        238
                )
        );

        transactionTable.setShowGrid(
                true
        );

        transactionTable.setIntercellSpacing(
                new Dimension(
                        1,
                        1
                )
        );

        transactionTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        transactionTable.setSelectionBackground(
                new Color(
                        237,
                        228,
                        246
                )
        );

        transactionTable.setSelectionForeground(
                DARK_TEXT
        );

        transactionTable.setAutoCreateRowSorter(
                true
        );

        transactionTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        // -----------------------------------------------------
        // HEADER
        // -----------------------------------------------------

        transactionTable
                .getTableHeader()
                .setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                11
                        )
                );

        transactionTable
                .getTableHeader()
                .setBackground(
                        PURPLE_DARK
                );

        transactionTable
                .getTableHeader()
                .setForeground(
                        WHITE
                );

        transactionTable
                .getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                45
                        )
                );

        transactionTable
                .getTableHeader()
                .setReorderingAllowed(
                        false
                );

        // -----------------------------------------------------
        // COLUMN WIDTHS
        // -----------------------------------------------------

        int[] widths = {

                120,
                150,
                170,
                125,
                155,
                155,
                400,
                190
        };

        for (
                int i = 0;
                i < widths.length;
                i++
        ) {

            transactionTable
                    .getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(
                            widths[i]
                    );
        }

        // -----------------------------------------------------
        // RENDERERS
        // -----------------------------------------------------

        transactionTable
                .getColumnModel()
                .getColumn(0)
                .setCellRenderer(
                        new TransactionIdRenderer()
                );

        transactionTable
                .getColumnModel()
                .getColumn(1)
                .setCellRenderer(
                        new PlayerRenderer()
                );

        transactionTable
                .getColumnModel()
                .getColumn(2)
                .setCellRenderer(
                        new TransactionTypeRenderer()
                );

        transactionTable
                .getColumnModel()
                .getColumn(3)
                .setCellRenderer(
                        new CurrencyRenderer(
                                SwingConstants.RIGHT
                        )
                );

        transactionTable
                .getColumnModel()
                .getColumn(4)
                .setCellRenderer(
                        new CurrencyRenderer(
                                SwingConstants.RIGHT
                        )
                );

        transactionTable
                .getColumnModel()
                .getColumn(5)
                .setCellRenderer(
                        new CurrencyRenderer(
                                SwingConstants.RIGHT
                        )
                );

        transactionTable
                .getColumnModel()
                .getColumn(6)
                .setCellRenderer(
                        new DescriptionRenderer()
                );

        transactionTable
                .getColumnModel()
                .getColumn(7)
                .setCellRenderer(
                        new DateRenderer()
                );
    }

    // =========================================================
    // BOTTOM PANEL
    // =========================================================

    private JPanel createBottomPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                LIGHT_BG
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        5,
                        25,
                        12,
                        25
                )
        );

        // -----------------------------------------------------
        // COUNT
        // -----------------------------------------------------

        JPanel countPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                0,
                                0
                        )
                );

        countPanel.setOpaque(false);

        countLabel =
                new JLabel(
                        "0 Transactions"
                );

        countLabel.setForeground(
                GREY_TEXT
        );

        countLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        countPanel.add(
                countLabel
        );

        panel.add(
                countPanel,
                BorderLayout.WEST
        );

        // -----------------------------------------------------
        // CLOSE
        // -----------------------------------------------------

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                0,
                                0
                        )
                );

        buttonPanel.setOpaque(false);

        JButton closeButton =
                createActionButton(
                        "CLOSE",
                        new Color(
                                100,
                                94,
                                110
                        ),
                        120
                );

        closeButton.addActionListener(
                e -> dispose()
        );

        buttonPanel.add(
                closeButton
        );

        panel.add(
                buttonPanel,
                BorderLayout.EAST
        );

        return panel;
    }

    // =========================================================
    // LOAD ALL TRANSACTIONS
    // =========================================================

    private void loadAllTransactions() {

        try {

            List<Object[]> transactions =
                    transactionDao
                            .getAllTransactions();

            displayTransactions(
                    transactions
            );

            int count =
                    transactions == null
                            ? 0
                            : transactions.size();

            updateTransactionCount(
                    count
            );

            if (searchStatusLabel != null) {

                searchStatusLabel.setText(
                        "Showing all transactions"
                );
            }

            updateWindowTitle(
                    count
            );

        } catch (SQLException e) {

            handleDatabaseError(
                    "Unable to load wallet transactions.",
                    e
            );
        }
    }

    // =========================================================
    // SEARCH TRANSACTIONS
    // =========================================================

    private void searchTransactions() {

        if (searchField == null) {

            return;
        }

        String playerName =
                searchField
                        .getText()
                        .trim();

        if (
                playerName.isEmpty()
        ) {

            loadAllTransactions();

            return;
        }

        try {

            List<Object[]> transactions =
                    transactionDao
                            .getPlayerTransactions(
                                    playerName
                            );

            displayTransactions(
                    transactions
            );

            int count =
                    transactions == null
                            ? 0
                            : transactions.size();

            updateTransactionCount(
                    count
            );

            if (searchStatusLabel != null) {

                searchStatusLabel.setText(
                        "Filtered by: "
                                + playerName
                );
            }

            updateWindowTitle(
                    count
            );

        } catch (SQLException e) {

            handleDatabaseError(
                    "Unable to search wallet transactions.",
                    e
            );
        }
    }

    // =========================================================
    // DISPLAY TRANSACTIONS
    // =========================================================

    private void displayTransactions(
            List<Object[]> transactions
    ) {

        if (tableModel == null) {

            return;
        }

        tableModel.setRowCount(
                0
        );

        if (
                transactions == null
        ) {

            return;
        }

        for (
                Object[] transaction :
                transactions
        ) {

            if (
                    transaction == null
            ) {

                continue;
            }

            Object[] formattedRow =
                    formatTransactionRow(
                            transaction
                    );

            tableModel.addRow(
                    formattedRow
            );
        }
    }

    // =========================================================
    // FORMAT TRANSACTION ROW
    // =========================================================

    private Object[] formatTransactionRow(
            Object[] transaction
    ) {

        Object[] row =
                new Object[8];

        for (
                int i = 0;
                i < row.length;
                i++
        ) {

            row[i] = null;

            if (
                    i < transaction.length
            ) {

                row[i] =
                        transaction[i];
            }
        }

        /*
         * Index 3 = Amount
         * Index 4 = Balance Before
         * Index 5 = Balance After
         */

        row[3] =
                formatCurrency(
                        transaction.length > 3
                                ? transaction[3]
                                : 0
                );

        row[4] =
                formatCurrency(
                        transaction.length > 4
                                ? transaction[4]
                                : 0
                );

        row[5] =
                formatCurrency(
                        transaction.length > 5
                                ? transaction[5]
                                : 0
                );

        /*
         * Timestamp
         */

        if (
                transaction.length > 7
                        && transaction[7] instanceof Timestamp
        ) {

            row[7] =
                    transaction[7].toString();
        }

        return row;
    }

    // =========================================================
    // FORMAT CURRENCY
    // =========================================================

    private String formatCurrency(
            Object value
    ) {

        if (
                value == null
        ) {

            return CURRENCY_FORMAT.format(
                    0
            );
        }

        if (
                value instanceof Number
        ) {

            return CURRENCY_FORMAT.format(
                    ((Number) value)
                            .doubleValue()
            );
        }

        try {

            double amount =
                    Double.parseDouble(
                            value.toString()
                    );

            return CURRENCY_FORMAT.format(
                    amount
            );

        } catch (
                NumberFormatException e
        ) {

            return CURRENCY_FORMAT.format(
                    0
            );
        }
    }

    // =========================================================
    // UPDATE COUNT
    // =========================================================

    private void updateTransactionCount(
            int count
    ) {

        if (
                countLabel == null
        ) {

            return;
        }

        countLabel.setText(
                count
                        + (
                        count == 1
                                ? " Transaction"
                                : " Transactions"
                )
        );
    }

    // =========================================================
    // UPDATE WINDOW TITLE
    // =========================================================

    private void updateWindowTitle(
            int transactionCount
    ) {

        setTitle(
                "Lucky King - Wallet Transactions ("
                        + transactionCount
                        + " Transactions)"
        );
    }

    // =========================================================
    // DATABASE ERROR
    // =========================================================

    private void handleDatabaseError(
            String message,
            SQLException exception
    ) {

        exception.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                message
                        + "\n\n"
                        + "Error:\n"
                        + exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // =========================================================
    // ACTION BUTTON
    // =========================================================

    private JButton createActionButton(
            String text,
            Color background,
            int width
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setPreferredSize(
                new Dimension(
                        width,
                        38
                )
        );

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        button.setForeground(
                WHITE
        );

        button.setBackground(
                background
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

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                background.brighter()
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                background
                        );
                    }

                    @Override
                    public void mousePressed(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                background.darker()
                        );
                    }

                    @Override
                    public void mouseReleased(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                background
                        );
                    }
                }
        );

        return button;
    }

    // =========================================================
    // TRANSACTION ID RENDERER
    // =========================================================

    private static class TransactionIdRenderer
            extends JPanel
            implements TableCellRenderer {

        private static final long serialVersionUID =
                1L;

        private String value =
                "";

        private boolean selected;

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            this.value =
                    value == null
                            ? "-"
                            : value.toString();

            this.selected =
                    isSelected;

            return this;
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
                    selected
                            ? new Color(
                            237,
                            228,
                            246
                    )
                            : WHITE
            );

            g.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );

            Font font =
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            10
                    );

            g.setFont(
                    font
            );

            FontMetricsWrapper metrics =
                    new FontMetricsWrapper(
                            g.getFontMetrics()
                    );

            int badgeWidth =
                    Math.max(
                            60,
                            metrics.stringWidth(
                                    value
                            ) + 26
                    );

            int badgeHeight =
                    28;

            int x =
                    (
                            getWidth()
                                    - badgeWidth
                    ) / 2;

            int y =
                    (
                            getHeight()
                                    - badgeHeight
                    ) / 2;

            g.setColor(
                    BLACK
            );

            g.fillRoundRect(
                    x,
                    y,
                    badgeWidth,
                    badgeHeight,
                    12,
                    12
            );

            g.setColor(
                    new Color(
                            70,
                            70,
                            70
                    )
            );

            g.drawRoundRect(
                    x,
                    y,
                    badgeWidth - 1,
                    badgeHeight - 1,
                    12,
                    12
            );

            g.setColor(
                    WHITE
            );

            int textWidth =
                    metrics.stringWidth(
                            value
                    );

            int textX =
                    x
                            + (
                            badgeWidth
                                    - textWidth
                    ) / 2;

            int textY =
                    y
                            + 18;

            g.drawString(
                    value,
                    textX,
                    textY
            );

            g.dispose();
        }
    }

    // =========================================================
    // PLAYER RENDERER
    // =========================================================

    private static class PlayerRenderer
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

            setHorizontalAlignment(
                    SwingConstants.LEFT
            );

            setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            12
                    )
            );

            setForeground(
                    DARK_TEXT
            );

            setBorder(
                    BorderFactory.createEmptyBorder(
                            0,
                            10,
                            0,
                            5
                    )
            );

            setBackground(
                    isSelected
                            ? new Color(
                            237,
                            228,
                            246
                    )
                            : row % 2 == 0
                            ? WHITE
                            : new Color(
                            250,
                            248,
                            252
                    )
            );

            return this;
        }
    }

    // =========================================================
    // TRANSACTION TYPE RENDERER
    // =========================================================

    private static class TransactionTypeRenderer
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

            String text =
                    value == null
                            ? "-"
                            : value.toString();

            JLabel label =
                    new JLabel(
                            text.toUpperCase(
                                    Locale.ENGLISH
                            ),
                            SwingConstants.CENTER
                    );

            label.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            10
                    )
            );

            label.setOpaque(
                    true
            );

            label.setBorder(
                    BorderFactory.createEmptyBorder(
                            5,
                            8,
                            5,
                            8
                    )
            );

            String upper =
                    text.toUpperCase(
                            Locale.ENGLISH
                    );

            if (
                    upper.contains("DEPOSIT")
                    || upper.contains("CREDIT")
                    || upper.contains("ADD")
            ) {

                label.setForeground(
                        GREEN
                );

                label.setBackground(
                        GREEN_LIGHT
                );

            } else if (
                    upper.contains("WITHDRAW")
                    || upper.contains("DEBIT")
                    || upper.contains("REMOVE")
            ) {

                label.setForeground(
                        RED
                );

                label.setBackground(
                        RED_LIGHT
                );

            } else {

                label.setForeground(
                        BLUE
                );

                label.setBackground(
                        BLUE_LIGHT
                );
            }

            return label;
        }
    }

    // =========================================================
    // CURRENCY RENDERER
    // =========================================================

    private static class CurrencyRenderer
            extends DefaultTableCellRenderer {

        private static final long serialVersionUID =
                1L;

        private final int alignment;

        CurrencyRenderer(
                int alignment
        ) {

            this.alignment =
                    alignment;
        }

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
                    alignment
            );

            setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            12
                    )
            );

            setForeground(
                    DARK_TEXT
            );

            setBorder(
                    BorderFactory.createEmptyBorder(
                            0,
                            8,
                            0,
                            12
                    )
            );

            setBackground(
                    isSelected
                            ? new Color(
                            237,
                            228,
                            246
                    )
                            : row % 2 == 0
                            ? WHITE
                            : new Color(
                            250,
                            248,
                            252
                    )
            );

            return this;
        }
    }

    // =========================================================
    // DESCRIPTION RENDERER
    // =========================================================

    private static class DescriptionRenderer
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

            setHorizontalAlignment(
                    SwingConstants.LEFT
            );

            setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            12
                    )
            );

            setForeground(
                    GREY_TEXT
            );

            setBorder(
                    BorderFactory.createEmptyBorder(
                            0,
                            10,
                            0,
                            10
                    )
            );

            setBackground(
                    isSelected
                            ? new Color(
                            237,
                            228,
                            246
                    )
                            : row % 2 == 0
                            ? WHITE
                            : new Color(
                            250,
                            248,
                            252
                    )
            );

            return this;
        }
    }

    // =========================================================
    // DATE RENDERER
    // =========================================================

    private static class DateRenderer
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

            setForeground(
                    GREY_TEXT
            );

            setBorder(
                    BorderFactory.createEmptyBorder(
                            0,
                            5,
                            0,
                            5
                    )
            );

            setBackground(
                    isSelected
                            ? new Color(
                            237,
                            228,
                            246
                    )
                            : row % 2 == 0
                            ? WHITE
                            : new Color(
                            250,
                            248,
                            252
                    )
            );

            return this;
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

            setOpaque(
                    false
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

            setOpaque(
                    false
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
    // ROUND ICON
    // =========================================================

    private static class RoundIcon
            extends JPanel {

        private static final long serialVersionUID =
                1L;

        private final Color background;

        private final Color foreground;

        private final int size;

        private final String text;

        RoundIcon(
                Color background,
                Color foreground,
                int size,
                String text
        ) {

            this.background =
                    background;

            this.foreground =
                    foreground;

            this.size =
                    size;

            this.text =
                    text;

            setOpaque(
                    false
            );

            setPreferredSize(
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

            g.setColor(
                    background
            );

            g.fillRoundRect(
                    0,
                    0,
                    size - 1,
                    size - 1,
                    18,
                    18
            );

            g.setColor(
                    foreground
            );

            g.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            size / 4
                    )
            );

            FontMetricsWrapper metrics =
                    new FontMetricsWrapper(
                            g.getFontMetrics()
                    );

            int textWidth =
                    metrics.stringWidth(
                            text
                    );

            int x =
                    (
                            size
                                    - textWidth
                    ) / 2;

            int y =
                    (
                            size
                                    - metrics.getHeight()
                    ) / 2
                            + metrics.getAscent();

            g.drawString(
                    text,
                    x,
                    y
            );

            g.dispose();
        }
    }

    // =========================================================
    // SIMPLE FONT METRICS WRAPPER
    // =========================================================

    private static class FontMetricsWrapper {

        private final java.awt.FontMetrics metrics;

        FontMetricsWrapper(
                java.awt.FontMetrics metrics
        ) {

            this.metrics =
                    metrics;
        }

        int stringWidth(
                String text
        ) {

            return metrics.stringWidth(
                    text
            );
        }

        int getHeight() {

            return metrics.getHeight();
        }

        int getAscent() {

            return metrics.getAscent();
        }
    }

    // =========================================================
    // CUSTOM LINE BORDER
    // =========================================================

    private static class LineBorderCustom
            extends javax.swing.border.LineBorder {

        private static final long serialVersionUID =
                1L;

        LineBorderCustom(
                Color color,
                int thickness
        ) {

            super(
                    color,
                    thickness,
                    true
            );
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

                        javax.swing.UIManager
                                .setLookAndFeel(
                                        javax.swing.UIManager
                                                .getSystemLookAndFeelClassName()
                                );

                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                    try {

                        WalletTransactionUi ui =
                                new WalletTransactionUi();

                        ui.setVisible(
                                true
                        );

                    } catch (Exception e) {

                        e.printStackTrace();

                        JOptionPane.showMessageDialog(
                                null,
                                "Unable to start Wallet Transaction UI.\n\n"
                                        + e.getMessage(),
                                "Startup Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
        );
    }
}