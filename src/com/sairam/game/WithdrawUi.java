package com.sairam.game;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class WithdrawUi extends JFrame {

    private static final long serialVersionUID = 1L;

    // =========================================================
    // SETTINGS
    // =========================================================

    private static final int MIN_WITHDRAW = 10;
    private static final int MAX_WITHDRAW = 1_000_000;

    // =========================================================
    // COLORS
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

    private static final Color ORANGE =
            new Color(220, 125, 35);

    private static final Color ORANGE_LIGHT =
            new Color(255, 244, 228);

    private static final Color BLUE =
            new Color(55, 105, 190);

    private static final Color BLUE_LIGHT =
            new Color(235, 242, 252);

    // =========================================================
    // FIELDS
    // =========================================================

    private final String playerName;

    private final WithdrawDao withdrawDao;

    private JLabel balanceLabel;

    private JLabel balanceStatusLabel;

    private JTextField amountField;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public WithdrawUi(String playerName) {

        if (
                playerName == null
                || playerName.trim().isEmpty()
        ) {

            throw new IllegalArgumentException(
                    "Player name cannot be empty."
            );
        }

        this.playerName =
                playerName.trim();

        this.withdrawDao =
                new WithdrawDao();

        initializeUI();

        loadBalance();
    }

    // =========================================================
    // INITIALIZE UI
    // =========================================================

    private void initializeUI() {

        setTitle(
                "Lucky King - Withdraw Credits"
        );

        setSize(
                920,
                720
        );

        setMinimumSize(
                new Dimension(
                        850,
                        650
                )
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setResizable(false);

        JPanel root =
                new JPanel(
                        new BorderLayout()
                );

        root.setBackground(
                LIGHT_BG
        );

        root.add(
                createHeader(),
                BorderLayout.NORTH
        );

        root.add(
                createCenterContent(),
                BorderLayout.CENTER
        );

        root.add(
                createFooter(),
                BorderLayout.SOUTH
        );

        setContentPane(root);

        getRootPane()
                .setDefaultButton(
                        null
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

        header.setPreferredSize(
                new Dimension(
                        900,
                        110
                )
        );

        header.setLayout(
                new BorderLayout()
        );

        header.setBorder(
                new EmptyBorder(
                        18,
                        25,
                        18,
                        25
                )
        );

        // -----------------------------------------------------
        // LEFT
        // -----------------------------------------------------

        JPanel left =
                new JPanel(
                        new BorderLayout(
                                15,
                                0
                        )
                );

        left.setOpaque(false);

        RoundIcon logo =
                new RoundIcon(
                        GOLD,
                        BLACK,
                        62,
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
                                1,
                                0,
                                4
                        )
                );

        titlePanel.setOpaque(false);

        JLabel title =
                new JLabel(
                        "WITHDRAW CREDITS"
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

        JLabel subtitle =
                new JLabel(
                        "Lucky King • Secure Credit Withdrawal"
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
                                1,
                                0,
                                5
                        )
                );

        right.setOpaque(false);

        JLabel playerTitle =
                new JLabel(
                        "PLAYER",
                        SwingConstants.RIGHT
                );

        playerTitle.setForeground(
                GOLD_LIGHT
        );

        playerTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        JLabel player =
                new JLabel(
                        playerName,
                        SwingConstants.RIGHT
                );

        player.setForeground(
                WHITE
        );

        player.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        right.add(playerTitle);

        right.add(player);

        header.add(
                right,
                BorderLayout.EAST
        );

        return header;
    }

    // =========================================================
    // CENTER CONTENT
    // =========================================================

    private JPanel createCenterContent() {

        JPanel center =
                new JPanel();

        center.setBackground(
                LIGHT_BG
        );

        center.setLayout(
                new BoxLayout(
                        center,
                        BoxLayout.Y_AXIS
                )
        );

        center.setBorder(
                new EmptyBorder(
                        20,
                        45,
                        15,
                        45
                )
        );

        // =====================================================
        // BALANCE CARD
        // =====================================================

        RoundedPanel balanceCard =
                new RoundedPanel(
                        20,
                        CARD_BG
                );

        balanceCard.setLayout(
                new BorderLayout(
                        20,
                        0
                )
        );

        balanceCard.setBorder(
                new EmptyBorder(
                        18,
                        22,
                        18,
                        22
                )
        );

        balanceCard.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        115
                )
        );

        // -----------------------------------------------------
        // BALANCE ICON
        // -----------------------------------------------------

        RoundIcon balanceIcon =
                new RoundIcon(
                        BLACK,
                        WHITE,
                        58,
                        "C"
                );

        balanceCard.add(
                balanceIcon,
                BorderLayout.WEST
        );

        // -----------------------------------------------------
        // BALANCE TEXT
        // -----------------------------------------------------

        JPanel balanceText =
                new JPanel();

        balanceText.setOpaque(false);

        balanceText.setLayout(
                new BoxLayout(
                        balanceText,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel balanceTitle =
                new JLabel(
                        "CURRENT BALANCE"
                );

        balanceTitle.setForeground(
                GREY_TEXT
        );

        balanceTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        balanceLabel =
                new JLabel(
                        "Rs. 0"
                );

        balanceLabel.setForeground(
                BLACK
        );

        balanceLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        29
                )
        );

        balanceStatusLabel =
                new JLabel(
                        "Available for withdrawal"
                );

        balanceStatusLabel.setForeground(
                GREEN
        );

        balanceStatusLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        balanceText.add(
                balanceTitle
        );

        balanceText.add(
                Box.createVerticalStrut(4)
        );

        balanceText.add(
                balanceLabel
        );

        balanceText.add(
                Box.createVerticalStrut(2)
        );

        balanceText.add(
                balanceStatusLabel
        );

        balanceCard.add(
                balanceText,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // BALANCE BADGE
        // -----------------------------------------------------

        RoundedBadge availableBadge =
                new RoundedBadge(
                        "AVAILABLE",
                        GREEN,
                        GREEN_LIGHT
                );

        balanceCard.add(
                availableBadge,
                BorderLayout.EAST
        );

        center.add(
                balanceCard
        );

        center.add(
                Box.createVerticalStrut(15)
        );

        // =====================================================
        // WITHDRAW CARD
        // =====================================================

        RoundedPanel withdrawCard =
                new RoundedPanel(
                        20,
                        CARD_BG
                );

        withdrawCard.setLayout(
                new BoxLayout(
                        withdrawCard,
                        BoxLayout.Y_AXIS
                )
        );

        withdrawCard.setBorder(
                new EmptyBorder(
                        20,
                        25,
                        20,
                        25
                )
        );

        withdrawCard.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        335
                )
        );

        JLabel sectionTitle =
                new JLabel(
                        "CREATE WITHDRAWAL REQUEST"
                );

        sectionTitle.setForeground(
                DARK_TEXT
        );

        sectionTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        sectionTitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        withdrawCard.add(
                sectionTitle
        );

        withdrawCard.add(
                Box.createVerticalStrut(4)
        );

        JLabel sectionSubtitle =
                new JLabel(
                        "Enter the amount you want to withdraw."
                );

        sectionSubtitle.setForeground(
                GREY_TEXT
        );

        sectionSubtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        sectionSubtitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        withdrawCard.add(
                sectionSubtitle
        );

        withdrawCard.add(
                Box.createVerticalStrut(15)
        );

        // -----------------------------------------------------
        // AMOUNT LABEL
        // -----------------------------------------------------

        JLabel amountLabel =
                new JLabel(
                        "WITHDRAWAL AMOUNT"
                );

        amountLabel.setForeground(
                GREY_TEXT
        );

        amountLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        amountLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        withdrawCard.add(
                amountLabel
        );

        withdrawCard.add(
                Box.createVerticalStrut(7)
        );

        // -----------------------------------------------------
        // AMOUNT FIELD
        // -----------------------------------------------------

        JPanel amountWrapper =
                new JPanel(
                        new BorderLayout()
                );

        amountWrapper.setOpaque(false);

        amountWrapper.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        amountWrapper.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        52
                )
        );

        JLabel rsLabel =
                new JLabel(
                        "Rs."
                );

        rsLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        rsLabel.setForeground(
                WHITE
        );

        rsLabel.setBackground(
                BLACK
        );

        rsLabel.setOpaque(true);

        rsLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        rsLabel.setPreferredSize(
                new Dimension(
                        65,
                        50
                )
        );

        amountField =
                new JTextField();

        amountField.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        19
                )
        );

        amountField.setForeground(
                DARK_TEXT
        );

        amountField.setBackground(
                WHITE
        );

        amountField.setCaretColor(
                PURPLE
        );

        amountField.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                0,
                                14,
                                0,
                                14
                        )
                )
        );

        amountField.setPreferredSize(
                new Dimension(
                        500,
                        50
                )
        );

        amountWrapper.add(
                rsLabel,
                BorderLayout.WEST
        );

        amountWrapper.add(
                amountField,
                BorderLayout.CENTER
        );

        withdrawCard.add(
                amountWrapper
        );

        withdrawCard.add(
                Box.createVerticalStrut(8)
        );

        // -----------------------------------------------------
        // LIMITS
        // -----------------------------------------------------

        JLabel limitsLabel =
                new JLabel(
                        "Minimum: Rs. "
                                + String.format(
                                Locale.US,
                                "%,d",
                                MIN_WITHDRAW
                        )
                                + "     •     Maximum: Rs. "
                                + String.format(
                                Locale.US,
                                "%,d",
                                MAX_WITHDRAW
                        )
                );

        limitsLabel.setForeground(
                GREY_TEXT
        );

        limitsLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        limitsLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        withdrawCard.add(
                limitsLabel
        );

        withdrawCard.add(
                Box.createVerticalStrut(13)
        );

        // -----------------------------------------------------
        // INFO
        // -----------------------------------------------------

        RoundedInfoPanel infoPanel =
                new RoundedInfoPanel();

        infoPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        infoPanel.setLayout(
                new BorderLayout(
                        12,
                        0
                )
        );

        infoPanel.setBorder(
                new EmptyBorder(
                        10,
                        13,
                        10,
                        13
                )
        );

        JLabel infoIcon =
                new JLabel(
                        "i",
                        SwingConstants.CENTER
                );

        infoIcon.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        infoIcon.setForeground(
                BLUE
        );

        infoIcon.setBackground(
                BLUE_LIGHT
        );

        infoIcon.setOpaque(true);

        infoIcon.setPreferredSize(
                new Dimension(
                        28,
                        28
                )
        );

        JLabel infoText =
                new JLabel(
                        "<html>"
                                + "<b>Withdrawal process:</b> "
                                + "Submit request → Admin review → "
                                + "Approval → Credits deducted."
                                + "</html>"
                );

        infoText.setForeground(
                DARK_TEXT
        );

        infoText.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        infoPanel.add(
                infoIcon,
                BorderLayout.WEST
        );

        infoPanel.add(
                infoText,
                BorderLayout.CENTER
        );

        withdrawCard.add(
                infoPanel
        );

        withdrawCard.add(
                Box.createVerticalStrut(14)
        );

        // -----------------------------------------------------
        // REQUEST BUTTON
        // -----------------------------------------------------

        JButton requestButton =
                createLargeButton(
                        "REQUEST WITHDRAWAL",
                        PURPLE
                );

        requestButton.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        requestButton.addActionListener(
                e -> createWithdrawalRequest()
        );

        withdrawCard.add(
                requestButton
        );

        center.add(
                withdrawCard
        );

        center.add(
                Box.createVerticalStrut(14)
        );

        // =====================================================
        // HISTORY BUTTON
        // =====================================================

        JButton historyButton =
                createLargeButton(
                        "VIEW WITHDRAWAL HISTORY",
                        BLACK
                );

        historyButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        historyButton.addActionListener(
                e -> showWithdrawalHistory()
        );

        center.add(
                historyButton
        );

        return center;
    }

    // =========================================================
    // FOOTER
    // =========================================================

    private JPanel createFooter() {

        JPanel footer =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                8
                        )
                );

        footer.setBackground(
                LIGHT_BG
        );

        JButton backButton =
                createSmallButton(
                        "BACK TO GAME",
                        new Color(
                                100,
                                94,
                                110
                        )
                );

        backButton.addActionListener(
                e -> goBack()
        );

        footer.add(
                backButton
        );

        return footer;
    }

    // =========================================================
    // LOAD BALANCE
    // =========================================================

    private void loadBalance() {

        try {

            int balance =
                    withdrawDao.getPlayerCredits(
                            playerName
                    );

            balanceLabel.setText(
                    formatCurrency(balance)
            );

            balanceStatusLabel.setText(
                    "Available for withdrawal"
            );

            balanceStatusLabel.setForeground(
                    GREEN
            );

        } catch (SQLException e) {

            e.printStackTrace();

            balanceLabel.setText(
                    "Rs. --"
            );

            balanceStatusLabel.setText(
                    "Unable to load balance"
            );

            balanceStatusLabel.setForeground(
                    RED
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load your balance.\n\n"
                            + e.getMessage(),
                    "Balance Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception e) {

            e.printStackTrace();

            balanceLabel.setText(
                    "Rs. --"
            );

            balanceStatusLabel.setText(
                    "Balance unavailable"
            );

            balanceStatusLabel.setForeground(
                    RED
            );
        }
    }

    // =========================================================
    // CREATE WITHDRAWAL REQUEST
    // =========================================================

    private void createWithdrawalRequest() {

        String amountText =
                amountField
                        .getText()
                        .trim();

        if (amountText.isEmpty()) {

            showWarning(
                    "Please enter withdrawal amount.",
                    "Amount Required"
            );

            amountField.requestFocus();

            return;
        }

        int amount;

        try {

            amount =
                    Integer.parseInt(
                            amountText
                    );

        } catch (NumberFormatException e) {

            showWarning(
                    "Please enter a valid whole number.",
                    "Invalid Amount"
            );

            amountField.requestFocus();

            amountField.selectAll();

            return;
        }

        // =====================================================
        // MINIMUM
        // =====================================================

        if (amount < MIN_WITHDRAW) {

            showWarning(
                    "Minimum withdrawal amount is "
                            + formatCurrency(
                            MIN_WITHDRAW
                    )
                            + ".",
                    "Invalid Amount"
            );

            amountField.requestFocus();

            amountField.selectAll();

            return;
        }

        // =====================================================
        // MAXIMUM
        // =====================================================

        if (amount > MAX_WITHDRAW) {

            showWarning(
                    "Maximum withdrawal amount is "
                            + formatCurrency(
                            MAX_WITHDRAW
                    )
                            + ".",
                    "Invalid Amount"
            );

            amountField.requestFocus();

            amountField.selectAll();

            return;
        }

        // =====================================================
        // CHECK BALANCE
        // =====================================================

        int currentBalance;

        try {

            currentBalance =
                    withdrawDao.getPlayerCredits(
                            playerName
                    );

        } catch (SQLException e) {

            e.printStackTrace();

            showError(
                    "Unable to check current balance.\n\n"
                            + e.getMessage(),
                    "Database Error"
            );

            return;

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Unable to check current balance.\n\n"
                            + e.getMessage(),
                    "Error"
            );

            return;
        }

        // =====================================================
        // INSUFFICIENT BALANCE
        // =====================================================

        if (amount > currentBalance) {

            showWarning(
                    "Insufficient credits.\n\n"
                            + "Your balance: "
                            + formatCurrency(
                            currentBalance
                    )
                            + "\n"
                            + "Requested: "
                            + formatCurrency(
                            amount
                    ),
                    "Insufficient Balance"
            );

            return;
        }

        // =====================================================
        // CONFIRM
        // =====================================================

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "<html>"
                                + "<b>Create withdrawal request?</b><br><br>"
                                + "Player: "
                                + escapeHtml(playerName)
                                + "<br>"
                                + "Amount: "
                                + formatCurrency(amount)
                                + "<br>"
                                + "Current Balance: "
                                + formatCurrency(currentBalance)
                                + "<br><br>"
                                + "<font color='#666666'>"
                                + "Credits will be deducted only "
                                + "after admin approval."
                                + "</font>"
                                + "</html>",
                        "Confirm Withdrawal",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (
                confirmation
                        != JOptionPane.YES_OPTION
        ) {

            return;
        }

        // =====================================================
        // CREATE REQUEST
        // =====================================================

        try {

            int requestId =
                    withdrawDao.createWithdrawRequest(
                            playerName,
                            amount
                    );

            JOptionPane.showMessageDialog(
                    this,
                    "<html>"
                            + "<b>Withdrawal request created successfully!</b>"
                            + "<br><br>"
                            + "Request ID: #"
                            + requestId
                            + "<br>"
                            + "Amount: "
                            + formatCurrency(amount)
                            + "<br>"
                            + "Status: PENDING"
                            + "<br><br>"
                            + "<font color='#666666'>"
                            + "Please wait for admin approval."
                            + "</font>"
                            + "</html>",
                    "Request Submitted",
                    JOptionPane.INFORMATION_MESSAGE
            );

            amountField.setText("");

            loadBalance();

        } catch (SQLException e) {

            e.printStackTrace();

            showError(
                    "Unable to create withdrawal request.\n\n"
                            + e.getMessage(),
                    "Withdrawal Error"
            );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Unexpected error occurred.\n\n"
                            + e.getMessage(),
                    "Error"
            );
        }
    }

    // =========================================================
    // SHOW WITHDRAWAL HISTORY
    // =========================================================

    private void showWithdrawalHistory() {

        try {

            List<Object[]> requests =
                    withdrawDao.getPlayerWithdrawRequests(
                            playerName
                    );

            if (
                    requests == null
                    || requests.isEmpty()
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "<html>"
                                + "<b>No withdrawal requests yet.</b>"
                                + "<br><br>"
                                + "Your submitted withdrawal requests "
                                + "will appear here."
                                + "</html>",
                        "Withdrawal History",
                        JOptionPane.INFORMATION_MESSAGE
                );

                return;
            }

            String[] columns = {

                    "REQUEST ID",
                    "AMOUNT",
                    "STATUS",
                    "CREATED AT",
                    "APPROVED AT"
            };

            DefaultTableModel model =
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

            SimpleDateFormat dateFormat =
                    new SimpleDateFormat(
                            "dd MMM yyyy  hh:mm a",
                            Locale.ENGLISH
                    );

            for (
                    Object[] request :
                    requests
            ) {

                Object requestId =
                        request.length > 0
                                ? request[0]
                                : "";

                Object amount =
                        request.length > 1
                                ? request[1]
                                : "";

                Object status =
                        request.length > 2
                                ? request[2]
                                : "";

                Object createdAt =
                        request.length > 3
                                ? request[3]
                                : "";

                Object approvedAt =
                        request.length > 4
                                ? request[4]
                                : "";

                String createdText =
                        formatTimestamp(
                                createdAt,
                                dateFormat
                        );

                String approvedText =
                        formatTimestamp(
                                approvedAt,
                                dateFormat
                        );

                model.addRow(
                        new Object[]{

                                requestId,

                                formatAmountObject(
                                        amount
                                ),

                                status,

                                createdText,

                                approvedText
                        }
                );
            }

            JTable table =
                    new JTable(model);

            styleHistoryTable(
                    table
            );

            JScrollPane scrollPane =
                    new JScrollPane(
                            table
                    );

            scrollPane.setPreferredSize(
                    new Dimension(
                            800,
                            380
                    )
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

            // -------------------------------------------------
            // HISTORY DIALOG
            // -------------------------------------------------

            JPanel panel =
                    new JPanel(
                            new BorderLayout(
                                    0,
                                    12
                            )
                    );

            panel.setBackground(
                    WHITE
            );

            panel.setBorder(
                    new EmptyBorder(
                            10,
                            10,
                            10,
                            10
                    )
            );

            JPanel titlePanel =
                    new JPanel(
                            new BorderLayout()
                    );

            titlePanel.setOpaque(false);

            JLabel title =
                    new JLabel(
                            "WITHDRAWAL HISTORY"
                    );

            title.setForeground(
                    DARK_TEXT
            );

            title.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            20
                    )
            );

            JLabel subtitle =
                    new JLabel(
                            "Player: " + playerName
                    );

            subtitle.setForeground(
                    GREY_TEXT
            );

            subtitle.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            12
                    )
            );

            titlePanel.add(
                    title,
                    BorderLayout.WEST
            );

            titlePanel.add(
                    subtitle,
                    BorderLayout.EAST
            );

            panel.add(
                    titlePanel,
                    BorderLayout.NORTH
            );

            panel.add(
                    scrollPane,
                    BorderLayout.CENTER
            );

            JOptionPane.showMessageDialog(
                    this,
                    panel,
                    "Lucky King - Withdrawal History",
                    JOptionPane.PLAIN_MESSAGE
            );

        } catch (SQLException e) {

            e.printStackTrace();

            showError(
                    "Unable to load withdrawal history.\n\n"
                            + e.getMessage(),
                    "Database Error"
            );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Unable to load withdrawal history.\n\n"
                            + e.getMessage(),
                    "Error"
            );
        }
    }

    // =========================================================
    // STYLE HISTORY TABLE
    // =========================================================

    private void styleHistoryTable(
            JTable table
    ) {

        table.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        table.setForeground(
                DARK_TEXT
        );

        table.setBackground(
                WHITE
        );

        table.setRowHeight(
                44
        );

        table.setGridColor(
                new Color(
                        235,
                        230,
                        238
                )
        );

        table.setShowGrid(true);

        table.setIntercellSpacing(
                new Dimension(
                        1,
                        1
                )
        );

        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        table.setSelectionBackground(
                new Color(
                        237,
                        228,
                        246
                )
        );

        table.setSelectionForeground(
                DARK_TEXT
        );

        // -----------------------------------------------------
        // HEADER
        // -----------------------------------------------------

        table.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                43
                        )
                );

        table.getTableHeader()
                .setBackground(
                        PURPLE_DARK
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
                                11
                        )
                );

        table.getTableHeader()
                .setReorderingAllowed(false);

        // -----------------------------------------------------
        // COLUMN WIDTHS
        // -----------------------------------------------------

        int[] widths = {

                105,
                145,
                130,
                190,
                190
        };

        for (
                int i = 0;
                i < widths.length;
                i++
        ) {

            table.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(
                            widths[i]
                    );
        }

        // -----------------------------------------------------
        // REQUEST ID
        // -----------------------------------------------------

        table.getColumnModel()
                .getColumn(0)
                .setCellRenderer(
                        new RequestIdRenderer()
                );

        // -----------------------------------------------------
        // AMOUNT
        // -----------------------------------------------------

        table.getColumnModel()
                .getColumn(1)
                .setCellRenderer(
                        new AmountRenderer()
                );

        // -----------------------------------------------------
        // STATUS
        // -----------------------------------------------------

        table.getColumnModel()
                .getColumn(2)
                .setCellRenderer(
                        new HistoryStatusRenderer()
                );

        // -----------------------------------------------------
        // DATE COLUMNS
        // -----------------------------------------------------

        table.getColumnModel()
                .getColumn(3)
                .setCellRenderer(
                        new HistoryTextRenderer()
                );

        table.getColumnModel()
                .getColumn(4)
                .setCellRenderer(
                        new HistoryTextRenderer()
                );
    }

    // =========================================================
    // FORMAT AMOUNT OBJECT
    // =========================================================

    private String formatAmountObject(
            Object amount
    ) {

        if (amount == null) {

            return "Rs. 0";
        }

        if (
                amount instanceof Number
        ) {

            return formatCurrency(
                    ((Number) amount)
                            .intValue()
            );
        }

        try {

            return formatCurrency(
                    Integer.parseInt(
                            amount.toString()
                    )
            );

        } catch (Exception e) {

            return "Rs. "
                    + amount;
        }
    }

    // =========================================================
    // FORMAT CURRENCY
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
    // FORMAT TIMESTAMP
    // =========================================================

    private String formatTimestamp(
            Object value,
            SimpleDateFormat format
    ) {

        if (value == null) {

            return "-";
        }

        if (
                value instanceof Timestamp
        ) {

            return format.format(
                    (Timestamp) value
            );
        }

        return value.toString();
    }

    // =========================================================
    // BACK TO GAME
    // =========================================================

    private void goBack() {

        dispose();

        SwingUtilities.invokeLater(
                () -> {

                    try {

                        GameUi gameUi =
                                new GameUi(
                                        playerName
                                );

                        gameUi.setVisible(true);

                    } catch (Exception e) {

                        e.printStackTrace();

                        JOptionPane.showMessageDialog(
                                null,
                                "Unable to open Game UI.\n\n"
                                        + e.getMessage(),
                                "Game UI Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
        );
    }

    // =========================================================
    // WARNING MESSAGE
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

    // =========================================================
    // ERROR MESSAGE
    // =========================================================

    private void showError(
            String message,
            String title
    ) {

        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }

    // =========================================================
    // HTML ESCAPE
    // =========================================================

    private String escapeHtml(
            String value
    ) {

        if (value == null) {

            return "";
        }

        return value
                .replace(
                        "&",
                        "&amp;"
                )
                .replace(
                        "<",
                        "&lt;"
                )
                .replace(
                        ">",
                        "&gt;"
                )
                .replace(
                        "\"",
                        "&quot;"
                );
    }

    // =========================================================
    // LARGE BUTTON
    // =========================================================

    private JButton createLargeButton(
            String text,
            Color background
    ) {

        JButton button =
                new JButton(text);

        button.setPreferredSize(
                new Dimension(
                        300,
                        44
                )
        );

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        44
                )
        );

        button.setMinimumSize(
                new Dimension(
                        200,
                        44
                )
        );

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
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
    // SMALL BUTTON
    // =========================================================

    private JButton createSmallButton(
            String text,
            Color background
    ) {

        JButton button =
                new JButton(text);

        button.setPreferredSize(
                new Dimension(
                        180,
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
                }
        );

        return button;
    }

    // =========================================================
    // REQUEST ID RENDERER
    // =========================================================

    private static class RequestIdRenderer
            extends JPanel
            implements TableCellRenderer {

        private static final long serialVersionUID = 1L;

        private String value = "";

        private boolean selected;

        RequestIdRenderer() {

            setOpaque(false);

            setLayout(
                    new GridBagLayout()
            );
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

            this.value =
                    value == null
                            ? ""
                            : String.valueOf(value);

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

            if (selected) {

                g.setColor(
                        new Color(
                                237,
                                228,
                                246
                        )
                );

            } else {

                g.setColor(
                        WHITE
                );
            }

            g.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );

            int badgeWidth =
                    Math.max(
                            58,
                            g.getFontMetrics(
                                    new Font(
                                            "Segoe UI",
                                            Font.BOLD,
                                            11
                                    )
                            ).stringWidth(value)
                                    + 25
                    );

            int badgeHeight = 28;

            int x =
                    (getWidth()
                            - badgeWidth)
                            / 2;

            int y =
                    (getHeight()
                            - badgeHeight)
                            / 2;

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
                            65,
                            65,
                            65
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

            Font font =
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            11
                    );

            g.setFont(font);

            FontMetrics fm =
                    g.getFontMetrics();

            int textWidth =
                    fm.stringWidth(value);

            int textX =
                    x
                            + (
                            badgeWidth
                                    - textWidth
                    )
                            / 2;

            int textY =
                    y
                            + (
                            badgeHeight
                                    - fm.getHeight()
                    )
                            / 2
                            + fm.getAscent();

            g.drawString(
                    value,
                    textX,
                    textY
            );

            g.dispose();
        }
    }

    // =========================================================
    // AMOUNT RENDERER
    // =========================================================

    private static class AmountRenderer
            extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

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

            setText(
                    value == null
                            ? "Rs. 0"
                            : value.toString()
            );

            setHorizontalAlignment(
                    SwingConstants.RIGHT
            );

            setForeground(
                    DARK_TEXT
            );

            setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            12
                    )
            );

            setBorder(
                    new EmptyBorder(
                            0,
                            8,
                            0,
                            12
                    )
            );

            if (!isSelected) {

                setBackground(
                        row % 2 == 0
                                ? WHITE
                                : new Color(
                                250,
                                248,
                                252
                        )
                );
            }

            return this;
        }
    }

    // =========================================================
    // STATUS RENDERER
    // =========================================================

    private static class HistoryStatusRenderer
            extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

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
                            status.toUpperCase(
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

            label.setOpaque(true);

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
                            "APPROVED"
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
                            "REJECTED"
                    )
                    ||
                    status.equalsIgnoreCase(
                            "DENIED"
                    )
            ) {

                label.setForeground(
                        RED
                );

                label.setBackground(
                        RED_LIGHT
                );

            } else if (
                    status.equalsIgnoreCase(
                            "PENDING"
                    )
            ) {

                label.setForeground(
                        ORANGE
                );

                label.setBackground(
                        ORANGE_LIGHT
                );

            } else {

                label.setForeground(
                        GREY_TEXT
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
    // HISTORY TEXT RENDERER
    // =========================================================

    private static class HistoryTextRenderer
            extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

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

            setForeground(
                    GREY_TEXT
            );

            setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            11
                    )
            );

            setBorder(
                    new EmptyBorder(
                            0,
                            5,
                            0,
                            5
                    )
            );

            if (!isSelected) {

                setBackground(
                        row % 2 == 0
                                ? WHITE
                                : new Color(
                                250,
                                248,
                                252
                        )
                );
            }

            return this;
        }
    }

    // =========================================================
    // ROUNDED BADGE
    // =========================================================

    private static class RoundedBadge
            extends JPanel {

        private static final long serialVersionUID = 1L;

        private final String text;

        private final Color foreground;

        private final Color background;

        RoundedBadge(
                String text,
                Color foreground,
                Color background
        ) {

            this.text =
                    text;

            this.foreground =
                    foreground;

            this.background =
                    background;

            setOpaque(false);

            setPreferredSize(
                    new Dimension(
                            100,
                            32
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
                    3,
                    getWidth() - 1,
                    getHeight() - 7,
                    15,
                    15
            );

            g.setColor(
                    foreground
            );

            g.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            9
                    )
            );

            FontMetrics fm =
                    g.getFontMetrics();

            int textWidth =
                    fm.stringWidth(text);

            g.drawString(
                    text,
                    (getWidth()
                            - textWidth)
                            / 2,
                    23
            );

            g.dispose();
        }
    }

    // =========================================================
    // ROUNDED INFO PANEL
    // =========================================================

    private static class RoundedInfoPanel
            extends JPanel {

        private static final long serialVersionUID = 1L;

        RoundedInfoPanel() {

            setOpaque(false);

            setPreferredSize(
                    new Dimension(
                            700,
                            48
                    )
            );

            setMaximumSize(
                    new Dimension(
                            Integer.MAX_VALUE,
                            48
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
                    BLUE_LIGHT
            );

            g.fillRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    12,
                    12
            );

            g.setColor(
                    new Color(
                            210,
                            220,
                            240
                    )
            );

            g.drawRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    12,
                    12
            );

            g.dispose();

            super.paintComponent(
                    graphics
            );
        }
    }

    // =========================================================
    // ROUNDED PANEL
    // =========================================================

    private static class RoundedPanel
            extends JPanel {

        private static final long serialVersionUID = 1L;

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
    // ROUND ICON
    // =========================================================

    private static class RoundIcon
            extends JPanel {

        private static final long serialVersionUID = 1L;

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

            setOpaque(false);

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
                    17,
                    17
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

            FontMetrics fm =
                    g.getFontMetrics();

            int textWidth =
                    fm.stringWidth(text);

            int x =
                    (size - textWidth)
                            / 2;

            int y =
                    (size
                            - fm.getHeight())
                            / 2
                            + fm.getAscent();

            g.drawString(
                    text,
                    x,
                    y
            );

            g.dispose();
        }
    }

    // =========================================================
    // GRADIENT PANEL
    // =========================================================

    private static class GradientPanel
            extends JPanel {

        private static final long serialVersionUID = 1L;

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

                    try {

                        WithdrawUi withdrawUi =
                                new WithdrawUi(
                                        "Sairam"
                                );

                        withdrawUi.setVisible(
                                true
                        );

                    } catch (Exception e) {

                        e.printStackTrace();

                        JOptionPane.showMessageDialog(
                                null,
                                "Unable to start Withdraw UI.\n\n"
                                        + e.getMessage(),
                                "Startup Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
        );
    }
}