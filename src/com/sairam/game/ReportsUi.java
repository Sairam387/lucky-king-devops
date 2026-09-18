package com.sairam.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
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
import javax.swing.table.DefaultTableModel;

public class ReportsUi extends JFrame {

    private static final long serialVersionUID = 1L;

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color HEADER_COLOR =
            new Color(35, 45, 60);

    private static final Color CARD_COLOR =
            new Color(245, 247, 250);

    private static final Color PRIMARY_COLOR =
            new Color(45, 95, 150);

    private static final Color SUCCESS_COLOR =
            new Color(45, 130, 85);

    private static final Color DANGER_COLOR =
            new Color(180, 65, 65);

    private static final Color EXPORT_COLOR =
            new Color(70, 95, 130);

    private static final Color WARNING_COLOR =
            new Color(180, 130, 45);

    private static final Color TEXT_COLOR =
            new Color(45, 55, 70);

    // =========================================================
    // UI COMPONENTS
    // =========================================================

    private JComboBox<String> reportTypeCombo;

    private JTextField playerField;
    private JTextField startDateField;
    private JTextField endDateField;

    private JButton generateButton;
    private JButton refreshButton;
    private JButton clearButton;

    private JButton exportCsvButton;
    private JButton exportExcelButton;
    private JButton exportPdfButton;

    private JTable reportTable;
    private DefaultTableModel tableModel;

    private JLabel reportTitleLabel;

    private JLabel totalGamesValue;
    private JLabel totalWinsValue;
    private JLabel totalLossesValue;
    private JLabel totalBetValue;
    private JLabel totalPayoutValue;
    private JLabel profitLossValue;
    private JLabel winPercentageValue;

    // =========================================================
    // DAO
    // =========================================================

    private final ReportsDao reportsDao;

    // =========================================================
    // CURRENT REPORT DATA
    // =========================================================

    private Object[] currentSummary;
    private String[] currentColumns;
    private List<Object[]> currentRows;

    // =========================================================
    // DATE FORMAT
    // =========================================================

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ReportsUi() {

        reportsDao = new ReportsDao();

        initializeFrame();

        createUi();

        try {

            loadOverallReport();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load overall report.\n\n"
                            + e.getMessage(),
                    "Report Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // FRAME
    // =========================================================

    private void initializeFrame() {

        setTitle(
                "Number Color Game - Reports"
        );

        setSize(
                1400,
                820
        );

        setMinimumSize(
                new Dimension(
                        1150,
                        700
                )
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );
    }

    // =========================================================
    // CREATE UI
    // =========================================================

    private void createUi() {

        getContentPane().setBackground(
                Color.WHITE
        );

        setLayout(
                new BorderLayout(
                        10,
                        10
                )
        );

        add(
                createHeader(),
                BorderLayout.NORTH
        );

        add(
                createCenterPanel(),
                BorderLayout.CENTER
        );

        add(
                createBottomPanel(),
                BorderLayout.SOUTH
        );
    }

    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeader() {

        JPanel headerPanel =
                new JPanel(
                        new BorderLayout()
                );

        headerPanel.setBackground(
                HEADER_COLOR
        );

        headerPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        20,
                        15,
                        20
                )
        );

        reportTitleLabel =
                new JLabel(
                        "REPORTS & ANALYTICS"
                );

        reportTitleLabel.setForeground(
                Color.WHITE
        );

        reportTitleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        JLabel subtitle =
                new JLabel(
                        "Number Color Game - Management Reports"
                );

        subtitle.setForeground(
                new Color(
                        215,
                        220,
                        225
                )
        );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        JPanel titlePanel =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        titlePanel.setOpaque(false);

        titlePanel.add(
                reportTitleLabel
        );

        titlePanel.add(
                subtitle
        );

        headerPanel.add(
                titlePanel,
                BorderLayout.WEST
        );

        return headerPanel;
    }

    // =========================================================
    // CENTER PANEL
    // =========================================================

    private JPanel createCenterPanel() {

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        centerPanel.setBackground(
                Color.WHITE
        );

        centerPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        10,
                        0,
                        10
                )
        );

        centerPanel.add(
                createFilterPanel(),
                BorderLayout.NORTH
        );

        centerPanel.add(
                createSummaryPanel(),
                BorderLayout.CENTER
        );

        return centerPanel;
    }

    // =========================================================
    // FILTER PANEL
    // =========================================================

    private JPanel createFilterPanel() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                8,
                                8
                        )
                );

        panel.setBackground(
                new Color(
                        248,
                        249,
                        251
                )
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        220,
                                        224,
                                        228
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                8,
                                5,
                                8
                        )
                )
        );

        JLabel reportTypeLabel =
                new JLabel(
                        "Report Type:"
                );

        reportTypeLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        reportTypeCombo =
                new JComboBox<>(
                        new String[] {
                                "OVERALL",
                                "TODAY",
                                "PLAYER",
                                "DATE RANGE",
                                "DAILY",
                                "MONTHLY",
                                "PLAYER-WISE",
                                "GAME TYPE",
                                "COLOR"
                        }
                );

        reportTypeCombo.setPreferredSize(
                new Dimension(
                        150,
                        32
                )
        );

        reportTypeCombo.addActionListener(
                e -> updateFilterFields()
        );

        JLabel playerLabel =
                new JLabel(
                        "Player:"
                );

        playerField =
                new JTextField();

        playerField.setPreferredSize(
                new Dimension(
                        140,
                        32
                )
        );

        JLabel startDateLabel =
                new JLabel(
                        "Start Date:"
                );

        startDateField =
                new JTextField();

        startDateField.setPreferredSize(
                new Dimension(
                        110,
                        32
                )
        );

        JLabel endDateLabel =
                new JLabel(
                        "End Date:"
                );

        endDateField =
                new JTextField();

        endDateField.setPreferredSize(
                new Dimension(
                        110,
                        32
                )
        );

        generateButton =
                createButton(
                        "GENERATE",
                        PRIMARY_COLOR
                );

        generateButton.addActionListener(
                e -> generateReport()
        );

        refreshButton =
                createButton(
                        "REFRESH",
                        SUCCESS_COLOR
                );

        refreshButton.addActionListener(
                e -> generateReport()
        );

        clearButton =
                createButton(
                        "CLEAR",
                        DANGER_COLOR
                );

        clearButton.addActionListener(
                e -> clearFilters()
        );

        panel.add(reportTypeLabel);
        panel.add(reportTypeCombo);

        panel.add(playerLabel);
        panel.add(playerField);

        panel.add(startDateLabel);
        panel.add(startDateField);

        panel.add(endDateLabel);
        panel.add(endDateField);

        panel.add(generateButton);
        panel.add(refreshButton);
        panel.add(clearButton);

        updateFilterFields();

        return panel;
    }

    // =========================================================
    // SUMMARY PANEL
    // =========================================================

    private JPanel createSummaryPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.add(
                createSummaryCardsPanel(),
                BorderLayout.NORTH
        );

        panel.add(
                createTablePanel(),
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================================================
    // SUMMARY CARDS
    // =========================================================

    private JPanel createSummaryCardsPanel() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                1,
                                7,
                                8,
                                8
                        )
                );

        panel.setBackground(
                Color.WHITE
        );

        totalGamesValue =
                new JLabel(
                        "0",
                        SwingConstants.CENTER
                );

        totalWinsValue =
                new JLabel(
                        "0",
                        SwingConstants.CENTER
                );

        totalLossesValue =
                new JLabel(
                        "0",
                        SwingConstants.CENTER
                );

        totalBetValue =
                new JLabel(
                        "0",
                        SwingConstants.CENTER
                );

        totalPayoutValue =
                new JLabel(
                        "0",
                        SwingConstants.CENTER
                );

        profitLossValue =
                new JLabel(
                        "0",
                        SwingConstants.CENTER
                );

        winPercentageValue =
                new JLabel(
                        "0.00%",
                        SwingConstants.CENTER
                );

        panel.add(
                createSummaryCard(
                        "TOTAL GAMES",
                        totalGamesValue
                )
        );

        panel.add(
                createSummaryCard(
                        "WINS",
                        totalWinsValue
                )
        );

        panel.add(
                createSummaryCard(
                        "LOSSES",
                        totalLossesValue
                )
        );

        panel.add(
                createSummaryCard(
                        "TOTAL BET",
                        totalBetValue
                )
        );

        panel.add(
                createSummaryCard(
                        "TOTAL PAYOUT",
                        totalPayoutValue
                )
        );

        panel.add(
                createSummaryCard(
                        "PROFIT / LOSS",
                        profitLossValue
                )
        );

        panel.add(
                createSummaryCard(
                        "WIN %",
                        winPercentageValue
                )
        );

        return panel;
    }

    // =========================================================
    // SUMMARY CARD
    // =========================================================

    private JPanel createSummaryCard(
            String title,
            JLabel valueLabel) {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                5,
                                5
                        )
                );

        card.setBackground(
                CARD_COLOR
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        220,
                                        224,
                                        228
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                8,
                                8,
                                8,
                                8
                        )
                )
        );

        JLabel titleLabel =
                new JLabel(
                        title,
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        titleLabel.setForeground(
                new Color(
                        90,
                        100,
                        115
                )
        );

        valueLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        valueLabel.setForeground(
                TEXT_COLOR
        );

        card.add(
                titleLabel,
                BorderLayout.NORTH
        );

        card.add(
                valueLabel,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // TABLE PANEL
    // =========================================================

    private JPanel createTablePanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Report Data"
                )
        );

        tableModel =
                new DefaultTableModel() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        reportTable =
                new JTable(
                        tableModel
                );

        reportTable.setRowHeight(
                25
        );

        reportTable.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        reportTable.getTableHeader()
                .setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                11
                        )
                );

        reportTable.getTableHeader()
                .setBackground(
                        HEADER_COLOR
                );

        reportTable.getTableHeader()
                .setForeground(
                        Color.WHITE
                );

        reportTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        reportTable
                );

        scrollPane.setPreferredSize(
                new Dimension(
                        1000,
                        350
                )
        );

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================================================
    // BOTTOM PANEL
    // =========================================================

    private JPanel createBottomPanel() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                8
                        )
                );

        panel.setBackground(
                Color.WHITE
        );

        exportCsvButton =
                createButton(
                        "EXPORT CSV",
                        EXPORT_COLOR
                );

        exportCsvButton.addActionListener(
                e -> exportCsv()
        );

        exportExcelButton =
                createButton(
                        "EXPORT EXCEL",
                        SUCCESS_COLOR
                );

        exportExcelButton.addActionListener(
                e -> exportExcel()
        );

        exportPdfButton =
                createButton(
                        "EXPORT PDF",
                        WARNING_COLOR
                );

        exportPdfButton.addActionListener(
                e -> exportPdf()
        );

        panel.add(exportCsvButton);
        panel.add(exportExcelButton);
        panel.add(exportPdfButton);

        return panel;
    }

    // =========================================================
    // BUTTON
    // =========================================================

    private JButton createButton(
            String text,
            Color background) {

        JButton button =
                new JButton(
                        text
                );

        button.setPreferredSize(
                new Dimension(
                        125,
                        34
                )
        );

        button.setBackground(
                background
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

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        5,
                        10,
                        5,
                        10
                )
        );

        button.setMargin(
                new Insets(
                        5,
                        10,
                        5,
                        10
                )
        );

        return button;
    }

    // =========================================================
    // FILTER FIELD CONTROL
    // =========================================================

    private void updateFilterFields() {

        String type =
                getCurrentReportType();

        boolean playerRequired =
                "PLAYER".equals(type);

        boolean dateRequired =
                "DATE RANGE".equals(type);

        playerField.setEnabled(
                playerRequired
        );

        startDateField.setEnabled(
                dateRequired
        );

        endDateField.setEnabled(
                dateRequired
        );

        if (!playerRequired) {
            playerField.setText("");
        }

        if (!dateRequired) {
            startDateField.setText("");
            endDateField.setText("");
        }
    }

    // =========================================================
    // GENERATE REPORT
    // =========================================================

    private void generateReport() {

        String type =
                getCurrentReportType();

        try {

            switch (type) {

                case "OVERALL":
                    loadOverallReport();
                    break;

                case "TODAY":
                    loadTodayReport();
                    break;

                case "PLAYER":
                    loadPlayerReport();
                    break;

                case "DATE RANGE":
                    loadDateRangeReport();
                    break;

                case "DAILY":
                    loadDailyReport();
                    break;

                case "MONTHLY":
                    loadMonthlyReport();
                    break;

                case "PLAYER-WISE":
                    loadPlayerWiseReport();
                    break;

                case "GAME TYPE":
                    loadGameTypeReport();
                    break;

                case "COLOR":
                    loadColorReport();
                    break;

                default:
                    loadOverallReport();
                    break;
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to generate report.\n\n"
                            + e.getMessage(),
                    "Report Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // OVERALL
    // =========================================================

    private void loadOverallReport()
            throws Exception {

        Object[] report =
                reportsDao.getOverallReport();

        currentSummary =
                report;

        currentColumns =
                new String[] {
                        "REPORT",
                        "TOTAL GAMES",
                        "TOTAL WINS",
                        "TOTAL LOSSES",
                        "TOTAL BET",
                        "TOTAL PAYOUT",
                        "PROFIT / LOSS",
                        "WIN %"
                };

        currentRows =
                new ArrayList<>();

        currentRows.add(
                new Object[] {
                        "OVERALL",
                        report[0],
                        report[1],
                        report[2],
                        report[3],
                        report[4],
                        report[5],
                        formatPercentage(report[6])
                }
        );

        setTableData(
                currentColumns,
                currentRows
        );

        updateSummary(report);

        reportTitleLabel.setText(
                "OVERALL REPORT"
        );
    }

    // =========================================================
    // TODAY
    // =========================================================

    private void loadTodayReport()
            throws Exception {

        Object[] report =
                reportsDao.getTodayReport();

        currentSummary =
                report;

        currentColumns =
                new String[] {
                        "REPORT",
                        "TOTAL GAMES",
                        "TOTAL WINS",
                        "TOTAL LOSSES",
                        "TOTAL BET",
                        "TOTAL PAYOUT",
                        "PROFIT / LOSS",
                        "WIN %"
                };

        currentRows =
                new ArrayList<>();

        currentRows.add(
                new Object[] {
                        "TODAY",
                        report[0],
                        report[1],
                        report[2],
                        report[3],
                        report[4],
                        report[5],
                        formatPercentage(report[6])
                }
        );

        setTableData(
                currentColumns,
                currentRows
        );

        updateSummary(report);

        reportTitleLabel.setText(
                "TODAY'S REPORT"
        );
    }

    // =========================================================
    // PLAYER
    // =========================================================

    private void loadPlayerReport()
            throws Exception {

        String player =
                playerField.getText().trim();

        if (player.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a player name.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Object[] report =
                reportsDao.getPlayerReport(
                        player
                );

        currentSummary =
                report;

        currentColumns =
                new String[] {
                        "REPORT",
                        "TOTAL GAMES",
                        "TOTAL WINS",
                        "TOTAL LOSSES",
                        "TOTAL BET",
                        "TOTAL PAYOUT",
                        "PROFIT / LOSS",
                        "WIN %"
                };

        currentRows =
                new ArrayList<>();

        currentRows.add(
                new Object[] {
                        player,
                        report[0],
                        report[1],
                        report[2],
                        report[3],
                        report[4],
                        report[5],
                        formatPercentage(report[6])
                }
        );

        setTableData(
                currentColumns,
                currentRows
        );

        updateSummary(report);

        reportTitleLabel.setText(
                "PLAYER REPORT - " + player
        );
    }

    // =========================================================
    // DATE RANGE
    // =========================================================

    private void loadDateRangeReport()
            throws Exception {

        String startText =
                startDateField.getText().trim();

        String endText =
                endDateField.getText().trim();

        if (startText.isEmpty()
                || endText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter both start date and end date.\n\n"
                            + "Format: yyyy-MM-dd",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        LocalDate startDate;
        LocalDate endDate;

        try {

            startDate =
                    LocalDate.parse(
                            startText,
                            DATE_FORMAT
                    );

            endDate =
                    LocalDate.parse(
                            endText,
                            DATE_FORMAT
                    );

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid date format.\n\n"
                            + "Use: yyyy-MM-dd\n"
                            + "Example: 2026-09-07",
                    "Invalid Date",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (endDate.isBefore(startDate)) {

            JOptionPane.showMessageDialog(
                    this,
                    "End date cannot be before start date.",
                    "Invalid Date Range",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Object[] report =
                reportsDao.getDateRangeReport(
                        startText,
                        endText
                );

        currentSummary =
                report;

        currentColumns =
                new String[] {
                        "REPORT",
                        "TOTAL GAMES",
                        "TOTAL WINS",
                        "TOTAL LOSSES",
                        "TOTAL BET",
                        "TOTAL PAYOUT",
                        "PROFIT / LOSS",
                        "WIN %"
                };

        currentRows =
                new ArrayList<>();

        currentRows.add(
                new Object[] {
                        startText + " to " + endText,
                        report[0],
                        report[1],
                        report[2],
                        report[3],
                        report[4],
                        report[5],
                        formatPercentage(report[6])
                }
        );

        setTableData(
                currentColumns,
                currentRows
        );

        updateSummary(report);

        reportTitleLabel.setText(
                "DATE RANGE REPORT"
        );
    }

    // =========================================================
    // DAILY
    // =========================================================

    private void loadDailyReport()
            throws Exception {

        currentColumns =
                new String[] {
                        "DATE",
                        "TOTAL GAMES",
                        "TOTAL WINS",
                        "TOTAL LOSSES",
                        "TOTAL BET",
                        "TOTAL PAYOUT",
                        "PROFIT / LOSS",
                        "WIN %"
                };

        List<Object[]> daoRows =
                reportsDao.getDailyReport();

        currentRows =
                formatDailyRows(
                        daoRows
                );

        setTableData(
                currentColumns,
                currentRows
        );

        updateSummaryFromRows(
                currentRows
        );

        reportTitleLabel.setText(
                "DAILY REPORT"
        );
    }

    // =========================================================
    // MONTHLY
    // =========================================================

    private void loadMonthlyReport()
            throws Exception {

        currentColumns =
                new String[] {
                        "MONTH",
                        "TOTAL GAMES",
                        "TOTAL WINS",
                        "TOTAL LOSSES",
                        "TOTAL BET",
                        "TOTAL PAYOUT",
                        "PROFIT / LOSS",
                        "WIN %"
                };

        List<Object[]> daoRows =
                reportsDao.getMonthlyReport();

        currentRows =
                formatMonthlyRows(
                        daoRows
                );

        setTableData(
                currentColumns,
                currentRows
        );

        updateSummaryFromRows(
                currentRows
        );

        reportTitleLabel.setText(
                "MONTHLY REPORT"
        );
    }

    // =========================================================
    // PLAYER-WISE
    // =========================================================

    private void loadPlayerWiseReport()
            throws Exception {

        currentColumns =
                new String[] {
                        "PLAYER",
                        "TOTAL GAMES",
                        "TOTAL WINS",
                        "TOTAL LOSSES",
                        "TOTAL BET",
                        "TOTAL PAYOUT",
                        "PROFIT / LOSS",
                        "WIN %"
                };

        List<Object[]> daoRows =
                reportsDao.getPlayerWiseReport();

        currentRows =
                formatPlayerWiseRows(
                        daoRows
                );

        setTableData(
                currentColumns,
                currentRows
        );

        updateSummaryFromRows(
                currentRows
        );

        reportTitleLabel.setText(
                "PLAYER-WISE REPORT"
        );
    }

    // =========================================================
    // GAME TYPE
    // =========================================================

    private void loadGameTypeReport()
            throws Exception {

        currentColumns =
                new String[] {
                        "GAME TYPE",
                        "TOTAL GAMES",
                        "TOTAL WINS",
                        "TOTAL LOSSES",
                        "TOTAL BET",
                        "TOTAL PAYOUT",
                        "PROFIT / LOSS",
                        "WIN %"
                };

        List<Object[]> daoRows =
                reportsDao.getGameTypeReport();

        currentRows =
                formatGameTypeRows(
                        daoRows
                );

        setTableData(
                currentColumns,
                currentRows
        );

        updateSummaryFromRows(
                currentRows
        );

        reportTitleLabel.setText(
                "GAME TYPE REPORT"
        );
    }

    // =========================================================
    // COLOR
    // =========================================================

    private void loadColorReport()
            throws Exception {

        currentColumns =
                new String[] {
                        "COLOR",
                        "TOTAL GAMES",
                        "TOTAL WINS",
                        "TOTAL LOSSES",
                        "TOTAL BET",
                        "TOTAL PAYOUT",
                        "PROFIT / LOSS",
                        "WIN %"
                };

        List<Object[]> daoRows =
                reportsDao.getColorReport();

        currentRows =
                formatColorRows(
                        daoRows
                );

        setTableData(
                currentColumns,
                currentRows
        );

        updateSummaryFromRows(
                currentRows
        );

        reportTitleLabel.setText(
                "COLOR REPORT"
        );
    }

    // =========================================================
    // FORMAT DAILY
    // =========================================================

    private List<Object[]> formatDailyRows(
            List<Object[]> rows) {

        List<Object[]> result =
                new ArrayList<>();

        if (rows == null) {
            return result;
        }

        for (Object[] row : rows) {

            if (row == null || row.length < 7) {
                continue;
            }

            double games =
                    toDouble(row[1]);

            double wins =
                    toDouble(row[2]);

            double percentage =
                    games == 0
                            ? 0
                            : (wins / games) * 100;

            result.add(
                    new Object[] {
                            formatDateValue(row[0]),
                            row[1],
                            row[2],
                            row[3],
                            row[4],
                            row[5],
                            row[6],
                            String.format(
                                    "%.2f%%",
                                    percentage
                            )
                    }
            );
        }

        return result;
    }

    // =========================================================
    // FORMAT MONTHLY
    // =========================================================

    private List<Object[]> formatMonthlyRows(
            List<Object[]> rows) {

        List<Object[]> result =
                new ArrayList<>();

        if (rows == null) {
            return result;
        }

        for (Object[] row : rows) {

            if (row == null || row.length < 7) {
                continue;
            }

            String monthText =
                    formatMonthValue(row[0]);

            double games =
                    toDouble(row[1]);

            double wins =
                    toDouble(row[2]);

            double percentage =
                    games == 0
                            ? 0
                            : (wins / games) * 100;

            result.add(
                    new Object[] {
                            monthText,
                            row[1],
                            row[2],
                            row[3],
                            row[4],
                            row[5],
                            row[6],
                            String.format(
                                    "%.2f%%",
                                    percentage
                            )
                    }
            );
        }

        return result;
    }

    // =========================================================
    // FORMAT PLAYER-WISE
    // =========================================================

    private List<Object[]> formatPlayerWiseRows(
            List<Object[]> rows) {

        List<Object[]> result =
                new ArrayList<>();

        if (rows == null) {
            return result;
        }

        for (Object[] row : rows) {

            if (row == null || row.length < 8) {
                continue;
            }

            result.add(
                    new Object[] {
                            row[0],
                            row[1],
                            row[2],
                            row[3],
                            row[4],
                            row[5],
                            row[6],
                            formatPercentage(row[7])
                    }
            );
        }

        return result;
    }

    // =========================================================
    // FORMAT GAME TYPE
    // =========================================================

    private List<Object[]> formatGameTypeRows(
            List<Object[]> rows) {

        List<Object[]> result =
                new ArrayList<>();

        if (rows == null) {
            return result;
        }

        for (Object[] row : rows) {

            if (row == null || row.length < 7) {
                continue;
            }

            double games =
                    toDouble(row[1]);

            double wins =
                    toDouble(row[2]);

            double percentage =
                    games == 0
                            ? 0
                            : (wins / games) * 100;

            result.add(
                    new Object[] {
                            row[0],
                            row[1],
                            row[2],
                            row[3],
                            row[4],
                            row[5],
                            row[6],
                            String.format(
                                    "%.2f%%",
                                    percentage
                            )
                    }
            );
        }

        return result;
    }

    // =========================================================
    // FORMAT COLOR
    // =========================================================

    private List<Object[]> formatColorRows(
            List<Object[]> rows) {

        List<Object[]> result =
                new ArrayList<>();

        if (rows == null) {
            return result;
        }

        for (Object[] row : rows) {

            if (row == null || row.length < 7) {
                continue;
            }

            double games =
                    toDouble(row[1]);

            double wins =
                    toDouble(row[2]);

            double percentage =
                    games == 0
                            ? 0
                            : (wins / games) * 100;

            result.add(
                    new Object[] {
                            row[0],
                            row[1],
                            row[2],
                            row[3],
                            row[4],
                            row[5],
                            row[6],
                            String.format(
                                    "%.2f%%",
                                    percentage
                            )
                    }
            );
        }

        return result;
    }

    // =========================================================
    // DATE VALUE
    // =========================================================

    private String formatDateValue(
            Object value) {

        if (value == null) {
            return "";
        }

        if (value instanceof LocalDate) {
            return ((LocalDate) value)
                    .format(DATE_FORMAT);
        }

        if (value instanceof Timestamp) {
            return ((Timestamp) value)
                    .toLocalDateTime()
                    .toLocalDate()
                    .format(DATE_FORMAT);
        }

        if (value instanceof java.sql.Date) {
            return ((java.sql.Date) value)
                    .toLocalDate()
                    .format(DATE_FORMAT);
        }

        String text =
                String.valueOf(value);

        if (text.length() >= 10) {
            String firstTen =
                    text.substring(0, 10);

            try {
                return LocalDate.parse(
                        firstTen
                ).format(DATE_FORMAT);

            } catch (Exception ignored) {
            }
        }

        return text;
    }

    // =========================================================
    // MONTH VALUE
    // =========================================================

    private String formatMonthValue(
            Object value) {

        if (value == null) {
            return "";
        }

        DateTimeFormatter monthFormatter =
                DateTimeFormatter.ofPattern(
                        "MMMM yyyy"
                );

        if (value instanceof LocalDate) {

            return ((LocalDate) value)
                    .format(monthFormatter);
        }

        if (value instanceof Timestamp) {

            return ((Timestamp) value)
                    .toLocalDateTime()
                    .toLocalDate()
                    .format(monthFormatter);
        }

        if (value instanceof java.sql.Date) {

            return ((java.sql.Date) value)
                    .toLocalDate()
                    .format(monthFormatter);
        }

        if (value instanceof LocalDateTime) {

            return ((LocalDateTime) value)
                    .toLocalDate()
                    .format(monthFormatter);
        }

        String text =
                String.valueOf(value);

        try {

            if (text.length() >= 10) {

                LocalDate date =
                        LocalDate.parse(
                                text.substring(0, 10)
                        );

                return date.format(
                        monthFormatter
                );
            }

        } catch (Exception ignored) {
        }

        return text;
    }

    // =========================================================
    // TABLE DATA
    // =========================================================

    private void setTableData(
            String[] columns,
            List<Object[]> rows) {

        tableModel.setColumnCount(0);

        tableModel.setRowCount(0);

        for (String column : columns) {

            tableModel.addColumn(
                    column
            );
        }

        if (rows != null) {

            for (Object[] row : rows) {

                tableModel.addRow(
                        row
                );
            }
        }

        reportTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );
    }

    // =========================================================
    // SUMMARY
    // =========================================================

    private void updateSummary(
            Object[] report) {

        if (report == null
                || report.length < 7) {

            clearSummary();

            return;
        }

        totalGamesValue.setText(
                String.valueOf(
                        report[0]
                )
        );

        totalWinsValue.setText(
                String.valueOf(
                        report[1]
                )
        );

        totalLossesValue.setText(
                String.valueOf(
                        report[2]
                )
        );

        totalBetValue.setText(
                formatAmount(
                        report[3]
                )
        );

        totalPayoutValue.setText(
                formatAmount(
                        report[4]
                )
        );

        profitLossValue.setText(
                formatAmount(
                        report[5]
                )
        );

        winPercentageValue.setText(
                formatPercentage(
                        report[6]
                )
        );

        updateProfitColor(
                report[5]
        );
    }

    // =========================================================
    // SUMMARY FROM ROWS
    // =========================================================

    private void updateSummaryFromRows(
            List<Object[]> rows) {

        int totalGames = 0;
        int totalWins = 0;
        int totalLosses = 0;

        double totalBet = 0;
        double totalPayout = 0;
        double profitLoss = 0;

        if (rows != null) {

            for (Object[] row : rows) {

                if (row == null || row.length < 7) {
                    continue;
                }

                totalGames +=
                        toInt(row[1]);

                totalWins +=
                        toInt(row[2]);

                totalLosses +=
                        toInt(row[3]);

                totalBet +=
                        toDouble(row[4]);

                totalPayout +=
                        toDouble(row[5]);

                profitLoss +=
                        toDouble(row[6]);
            }
        }

        double winPercentage =
                totalGames == 0
                        ? 0
                        : ((double) totalWins
                                / totalGames) * 100;

        currentSummary =
                new Object[] {
                        totalGames,
                        totalWins,
                        totalLosses,
                        totalBet,
                        totalPayout,
                        profitLoss,
                        winPercentage
                };

        updateSummary(
                currentSummary
        );
    }

    // =========================================================
    // CLEAR FILTERS
    // =========================================================

    private void clearFilters() {

        playerField.setText("");

        startDateField.setText("");

        endDateField.setText("");

        reportTypeCombo.setSelectedItem(
                "OVERALL"
        );

        clearSummary();

        tableModel.setRowCount(0);

        currentRows =
                new ArrayList<>();

        currentSummary =
                createEmptySummary();

        currentColumns =
                null;

        reportTitleLabel.setText(
                "REPORTS & ANALYTICS"
        );
    }

    // =========================================================
    // CLEAR SUMMARY
    // =========================================================

    private void clearSummary() {

        totalGamesValue.setText("0");

        totalWinsValue.setText("0");

        totalLossesValue.setText("0");

        totalBetValue.setText("0");

        totalPayoutValue.setText("0");

        profitLossValue.setText("0");

        winPercentageValue.setText("0.00%");

        profitLossValue.setForeground(
                TEXT_COLOR
        );
    }

    // =========================================================
    // EXPORT CSV
    // =========================================================

    private void exportCsv() {

        if (!hasReportData()) {
            return;
        }

        JFileChooser chooser =
                new JFileChooser();

        chooser.setDialogTitle(
                "Save Report as CSV"
        );

        chooser.setSelectedFile(
                new File(
                        createDefaultFileName(
                                "csv"
                        )
                )
        );

        int result =
                chooser.showSaveDialog(
                        this
                );

        if (result
                != JFileChooser.APPROVE_OPTION) {

            return;
        }

        File file =
                chooser.getSelectedFile();

        if (!file.getName()
                .toLowerCase()
                .endsWith(".csv")) {

            file =
                    new File(
                            file.getAbsolutePath()
                                    + ".csv"
                    );
        }

        try {

            writeCsvFile(file);

            JOptionPane.showMessageDialog(
                    this,
                    "CSV report exported successfully.\n\n"
                            + file.getAbsolutePath(),
                    "Export Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to export CSV.\n\n"
                            + e.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // WRITE CSV
    // =========================================================

    private void writeCsvFile(
            File file) throws IOException {

        try (
                BufferedWriter writer =
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        new FileOutputStream(file),
                                        StandardCharsets.UTF_8
                                )
                        )
        ) {

            writer.write(
                    csvEscape(
                            "Number Color Game Report"
                    )
            );

            writer.newLine();

            writer.write(
                    csvEscape(
                            "Report Type"
                    )
            );

            writer.write(",");

            writer.write(
                    csvEscape(
                            getCurrentReportType()
                    )
            );

            writer.newLine();

            writer.write(
                    csvEscape(
                            "Generated On"
                    )
            );

            writer.write(",");

            writer.write(
                    csvEscape(
                            LocalDate.now().toString()
                    )
            );

            writer.newLine();
            writer.newLine();

            writer.write("SUMMARY");
            writer.newLine();

            String[] summaryHeaders =
                    new String[] {
                            "TOTAL GAMES",
                            "TOTAL WINS",
                            "TOTAL LOSSES",
                            "TOTAL BET",
                            "TOTAL PAYOUT",
                            "PROFIT / LOSS",
                            "WIN %"
                    };

            for (int i = 0;
                    i < summaryHeaders.length;
                    i++) {

                if (i > 0) {
                    writer.write(",");
                }

                writer.write(
                        csvEscape(
                                summaryHeaders[i]
                        )
                );
            }

            writer.newLine();

            if (currentSummary != null) {

                for (int i = 0;
                        i < summaryHeaders.length;
                        i++) {

                    if (i > 0) {
                        writer.write(",");
                    }

                    String value =
                            i < currentSummary.length
                                    ? formatExportValue(
                                            currentSummary[i]
                                    )
                                    : "";

                    writer.write(
                            csvEscape(value)
                    );
                }
            }

            writer.newLine();
            writer.newLine();

            writer.write("REPORT DATA");
            writer.newLine();

            for (int i = 0;
                    i < currentColumns.length;
                    i++) {

                if (i > 0) {
                    writer.write(",");
                }

                writer.write(
                        csvEscape(
                                currentColumns[i]
                        )
                );
            }

            writer.newLine();

            if (currentRows != null) {

                for (Object[] row : currentRows) {

                    for (int i = 0;
                            i < currentColumns.length;
                            i++) {

                        if (i > 0) {
                            writer.write(",");
                        }

                        String value = "";

                        if (row != null
                                && i < row.length
                                && row[i] != null) {

                            value =
                                    formatExportValue(
                                            row[i]
                                    );
                        }

                        writer.write(
                                csvEscape(value)
                        );
                    }

                    writer.newLine();
                }
            }
        }
    }

    // =========================================================
    // CSV ESCAPE
    // =========================================================

    private String csvEscape(
            String value) {

        if (value == null) {
            return "";
        }

        String escaped =
                value.replace(
                        "\"",
                        "\"\""
                );

        if (escaped.contains(",")
                || escaped.contains("\"")
                || escaped.contains("\n")
                || escaped.contains("\r")) {

            return "\"" + escaped + "\"";
        }

        return escaped;
    }

    // =========================================================
    // EXPORT EXCEL
    // =========================================================

    private void exportExcel() {

        if (!hasReportData()) {
            return;
        }

        JFileChooser chooser =
                new JFileChooser();

        chooser.setDialogTitle(
                "Save Report as Excel"
        );

        chooser.setSelectedFile(
                new File(
                        createDefaultFileName(
                                "xlsx"
                        )
                )
        );

        int result =
                chooser.showSaveDialog(
                        this
                );

        if (result
                != JFileChooser.APPROVE_OPTION) {

            return;
        }

        File file =
                chooser.getSelectedFile();

        if (!file.getName()
                .toLowerCase()
                .endsWith(".xlsx")) {

            file =
                    new File(
                            file.getAbsolutePath()
                                    + ".xlsx"
                    );
        }

        try {

            createExcelFile(file);

            JOptionPane.showMessageDialog(
                    this,
                    "Excel report exported successfully.\n\n"
                            + file.getAbsolutePath(),
                    "Export Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to export Excel file.\n\n"
                            + e.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // CREATE EXCEL FILE
    // =========================================================

    private void createExcelFile(
            File file) throws IOException {

        try (
                FileOutputStream output =
                        new FileOutputStream(file);

                ZipOutputStream zip =
                        new ZipOutputStream(output)
        ) {

            addZipEntry(
                    zip,
                    "[Content_Types].xml",
                    createContentTypesXml()
            );

            addZipEntry(
                    zip,
                    "_rels/.rels",
                    createRootRelationshipsXml()
            );

            addZipEntry(
                    zip,
                    "xl/workbook.xml",
                    createWorkbookXml()
            );

            addZipEntry(
                    zip,
                    "xl/_rels/workbook.xml.rels",
                    createWorkbookRelationshipsXml()
            );

            addZipEntry(
                    zip,
                    "xl/styles.xml",
                    createStylesXml()
            );

            addZipEntry(
                    zip,
                    "xl/worksheets/sheet1.xml",
                    createWorksheetXml()
            );
        }
    }

    // =========================================================
    // ZIP ENTRY
    // =========================================================

    private void addZipEntry(
            ZipOutputStream zip,
            String name,
            String content)
            throws IOException {

        ZipEntry entry =
                new ZipEntry(name);

        zip.putNextEntry(entry);

        byte[] data =
                content.getBytes(
                        StandardCharsets.UTF_8
                );

        zip.write(data);

        zip.closeEntry();
    }

    // =========================================================
    // EXCEL CONTENT TYPES
    // =========================================================

    private String createContentTypesXml() {

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">"
                + "<Default Extension=\"rels\" "
                + "ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>"
                + "<Default Extension=\"xml\" "
                + "ContentType=\"application/xml\"/>"
                + "<Override PartName=\"/xl/workbook.xml\" "
                + "ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml\"/>"
                + "<Override PartName=\"/xl/worksheets/sheet1.xml\" "
                + "ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/>"
                + "<Override PartName=\"/xl/styles.xml\" "
                + "ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml\"/>"
                + "</Types>";
    }

    // =========================================================
    // ROOT RELATIONSHIPS
    // =========================================================

    private String createRootRelationshipsXml() {

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">"
                + "<Relationship Id=\"rId1\" "
                + "Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" "
                + "Target=\"xl/workbook.xml\"/>"
                + "</Relationships>";
    }

    // =========================================================
    // WORKBOOK
    // =========================================================

    private String createWorkbookXml() {

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<workbook "
                + "xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\" "
                + "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\">"
                + "<sheets>"
                + "<sheet name=\"Report\" sheetId=\"1\" r:id=\"rId1\"/>"
                + "</sheets>"
                + "</workbook>";
    }

    // =========================================================
    // WORKBOOK RELATIONSHIPS
    // =========================================================

    private String createWorkbookRelationshipsXml() {

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">"
                + "<Relationship Id=\"rId1\" "
                + "Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" "
                + "Target=\"worksheets/sheet1.xml\"/>"
                + "<Relationship Id=\"rId2\" "
                + "Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles\" "
                + "Target=\"styles.xml\"/>"
                + "</Relationships>";
    }

    // =========================================================
    // EXCEL STYLES
    // =========================================================

    private String createStylesXml() {

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<styleSheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">"

                + "<fonts count=\"2\">"

                + "<font>"
                + "<sz val=\"11\"/>"
                + "<name val=\"Calibri\"/>"
                + "</font>"

                + "<font>"
                + "<b/>"
                + "<sz val=\"11\"/>"
                + "<name val=\"Calibri\"/>"
                + "</font>"

                + "</fonts>"

                + "<fills count=\"3\">"

                + "<fill>"
                + "<patternFill patternType=\"none\"/>"
                + "</fill>"

                + "<fill>"
                + "<patternFill patternType=\"gray125\"/>"
                + "</fill>"

                + "<fill>"
                + "<patternFill patternType=\"solid\">"
                + "<fgColor rgb=\"FF37465A\"/>"
                + "<bgColor indexed=\"64\"/>"
                + "</patternFill>"
                + "</fill>"

                + "</fills>"

                + "<borders count=\"1\">"

                + "<border>"
                + "<left/>"
                + "<right/>"
                + "<top/>"
                + "<bottom/>"
                + "<diagonal/>"
                + "</border>"

                + "</borders>"

                + "<cellStyleXfs count=\"1\">"

                + "<xf numFmtId=\"0\" "
                + "fontId=\"0\" "
                + "fillId=\"0\" "
                + "borderId=\"0\"/>"

                + "</cellStyleXfs>"

                + "<cellXfs count=\"2\">"

                + "<xf numFmtId=\"0\" "
                + "fontId=\"0\" "
                + "fillId=\"0\" "
                + "borderId=\"0\"/>"

                + "<xf numFmtId=\"0\" "
                + "fontId=\"1\" "
                + "fillId=\"2\" "
                + "borderId=\"0\" "
                + "applyFont=\"1\" "
                + "applyFill=\"1\">"

                + "<alignment "
                + "horizontal=\"center\" "
                + "vertical=\"center\"/>"

                + "</xf>"

                + "</cellXfs>"

                + "</styleSheet>";
    }

    // =========================================================
    // EXCEL WORKSHEET
    // =========================================================

    private String createWorksheetXml() {

        StringBuilder xml =
                new StringBuilder();

        xml.append(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        );

        xml.append(
                "<worksheet "
                        + "xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">"
        );

        xml.append(
                "<sheetViews>"
                        + "<sheetView workbookViewId=\"0\"/>"
                        + "</sheetViews>"
        );

        xml.append(
                "<sheetFormatPr defaultRowHeight=\"20\"/>"
        );

        xml.append("<sheetData>");

        int rowNumber = 1;

        xml.append(
                createExcelRow(
                        rowNumber++,
                        new Object[] {
                                "NUMBER COLOR GAME REPORT"
                        },
                        false
                )
        );

        xml.append(
                createExcelRow(
                        rowNumber++,
                        new Object[] {
                                "Report Type",
                                getCurrentReportType()
                        },
                        false
                )
        );

        xml.append(
                createExcelRow(
                        rowNumber++,
                        new Object[] {
                                "Generated On",
                                LocalDate.now().toString()
                        },
                        false
                )
        );

        rowNumber++;

        xml.append(
                createExcelRow(
                        rowNumber++,
                        new Object[] {
                                "SUMMARY"
                        },
                        false
                )
        );

        xml.append(
                createExcelRow(
                        rowNumber++,
                        new Object[] {
                                "TOTAL GAMES",
                                "TOTAL WINS",
                                "TOTAL LOSSES",
                                "TOTAL BET",
                                "TOTAL PAYOUT",
                                "PROFIT / LOSS",
                                "WIN %"
                        },
                        true
                )
        );

        Object[] summaryRow =
                new Object[7];

        if (currentSummary != null) {

            for (int i = 0; i < 7; i++) {

                if (i < currentSummary.length) {

                    summaryRow[i] =
                            formatExportValue(
                                    currentSummary[i]
                            );
                }
            }
        }

        xml.append(
                createExcelRow(
                        rowNumber++,
                        summaryRow,
                        false
                )
        );

        rowNumber++;

        xml.append(
                createExcelRow(
                        rowNumber++,
                        new Object[] {
                                "REPORT DATA"
                        },
                        false
                )
        );

        Object[] headers =
                new Object[
                        currentColumns.length
                ];

        System.arraycopy(
                currentColumns,
                0,
                headers,
                0,
                currentColumns.length
        );

        xml.append(
                createExcelRow(
                        rowNumber++,
                        headers,
                        true
                )
        );

        if (currentRows != null) {

            for (Object[] row : currentRows) {

                xml.append(
                        createExcelRow(
                                rowNumber++,
                                row,
                                false
                        )
                );
            }
        }

        xml.append("</sheetData>");

        xml.append(
                "<autoFilter ref=\"A1:"
                        + getExcelColumnName(
                                Math.max(
                                        1,
                                        currentColumns.length
                                )
                        )
                        + rowNumber
                        + "\"/>"
        );

        xml.append("</worksheet>");

        return xml.toString();
    }

    // =========================================================
    // EXCEL ROW
    // =========================================================

    private String createExcelRow(
            int rowNumber,
            Object[] values,
            boolean header) {

        StringBuilder row =
                new StringBuilder();

        row.append(
                "<row r=\""
                        + rowNumber
                        + "\">"
        );

        if (values != null) {

            for (int i = 0;
                    i < values.length;
                    i++) {

                String value = "";

                if (values[i] != null) {

                    value =
                            formatExportValue(
                                    values[i]
                            );
                }

                String cellReference =
                        getExcelColumnName(
                                i + 1
                        )
                                + rowNumber;

                row.append(
                        "<c r=\""
                                + cellReference
                                + "\""
                );

                if (header) {

                    row.append(
                            " s=\"1\""
                    );
                }

                row.append(
                        " t=\"inlineStr\">"
                );

                row.append(
                        "<is><t>"
                );

                row.append(
                        xmlEscape(value)
                );

                row.append(
                        "</t></is></c>"
                );
            }
        }

        row.append(
                "</row>"
        );

        return row.toString();
    }

    // =========================================================
    // EXCEL COLUMN NAME
    // =========================================================

    private String getExcelColumnName(
            int columnNumber) {

        StringBuilder result =
                new StringBuilder();

        while (columnNumber > 0) {

            int remainder =
                    (columnNumber - 1)
                            % 26;

            result.insert(
                    0,
                    (char) (
                            'A'
                                    + remainder
                    )
            );

            columnNumber =
                    (columnNumber - 1)
                            / 26;
        }

        return result.toString();
    }

    // =========================================================
    // XML ESCAPE
    // =========================================================

    private String xmlEscape(
            String value) {

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
                )
                .replace(
                        "'",
                        "&apos;"
                );
    }

    // =========================================================
    // EXPORT PDF
    // =========================================================

    private void exportPdf() {

        if (!hasReportData()) {
            return;
        }

        try {

            String reportType =
                    getCurrentReportType();

            String title =
                    reportTitleLabel.getText();

            File file =
                    PdfReportExporter.exportReport(
                            title,
                            reportType,
                            currentSummary,
                            currentColumns,
                            currentRows
                    );

            int choice =
                    JOptionPane.showConfirmDialog(
                            this,
                            "PDF report exported successfully.\n\n"
                                    + file.getAbsolutePath()
                                    + "\n\n"
                                    + "Do you want to open the PDF?",
                            "PDF Export Successful",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE
                    );

            if (choice
                    == JOptionPane.YES_OPTION) {

                PdfReportExporter.openPdf(
                        file
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to export PDF.\n\n"
                            + e.getMessage(),
                    "PDF Export Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // CHECK REPORT DATA
    // =========================================================

    private boolean hasReportData() {

        if (currentColumns == null
                || currentColumns.length == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please generate a report first.",
                    "No Report",
                    JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        if (currentRows == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "There is no report data to export.",
                    "No Data",
                    JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        return true;
    }

    // =========================================================
    // DEFAULT FILE NAME
    // =========================================================

    private String createDefaultFileName(
            String extension) {

        String type =
                getCurrentReportType()
                        .toLowerCase()
                        .replace(
                                " ",
                                "_"
                        )
                        .replace(
                                "-",
                                "_"
                        );

        String date =
                LocalDate.now()
                        .format(
                                DateTimeFormatter.BASIC_ISO_DATE
                        );

        return "NumberColorGame_"
                + type
                + "_Report_"
                + date
                + "."
                + extension;
    }

    // =========================================================
    // CURRENT REPORT TYPE
    // =========================================================

    private String getCurrentReportType() {

        Object selected =
                reportTypeCombo.getSelectedItem();

        if (selected == null) {
            return "OVERALL";
        }

        return selected.toString();
    }

    // =========================================================
    // FORMAT PERCENTAGE
    // =========================================================

    private String formatPercentage(
            Object value) {

        if (value == null) {
            return "0.00%";
        }

        double number =
                toDouble(value);

        return String.format(
                "%.2f%%",
                number
        );
    }

    // =========================================================
    // FORMAT AMOUNT
    // =========================================================

    private String formatAmount(
            Object value) {

        if (value == null) {
            return "0";
        }

        double number =
                toDouble(value);

        if (number == Math.rint(number)) {

            return String.valueOf(
                    (long) number
            );
        }

        return String.format(
                "%.2f",
                number
        );
    }

    // =========================================================
    // EXPORT VALUE
    // =========================================================

    private String formatExportValue(
            Object value) {

        if (value == null) {
            return "";
        }

        if (value instanceof Number) {

            double number =
                    ((Number) value)
                            .doubleValue();

            if (number == Math.rint(number)) {

                return String.valueOf(
                        (long) number
                );
            }

            return String.format(
                    "%.2f",
                    number
            );
        }

        return String.valueOf(value);
    }

    // =========================================================
    // DOUBLE
    // =========================================================

    private double toDouble(
            Object value) {

        if (value == null) {
            return 0;
        }

        if (value instanceof Number) {

            return ((Number) value)
                    .doubleValue();
        }

        try {

            String text =
                    String.valueOf(value)
                            .replace(
                                    "%",
                                    ""
                            )
                            .trim();

            if (text.isEmpty()) {
                return 0;
            }

            return Double.parseDouble(
                    text
            );

        } catch (NumberFormatException e) {

            return 0;
        }
    }

    // =========================================================
    // INTEGER
    // =========================================================

    private int toInt(
            Object value) {

        return (int) Math.round(
                toDouble(value)
        );
    }

    // =========================================================
    // EMPTY SUMMARY
    // =========================================================

    private Object[] createEmptySummary() {

        return new Object[] {
                0,
                0,
                0,
                0,
                0,
                0,
                0
        };
    }

    // =========================================================
    // PROFIT COLOR
    // =========================================================

    private void updateProfitColor(
            Object value) {

        double profit =
                toDouble(value);

        if (profit > 0) {

            profitLossValue.setForeground(
                    SUCCESS_COLOR
            );

        } else if (profit < 0) {

            profitLossValue.setForeground(
                    DANGER_COLOR
            );

        } else {

            profitLossValue.setForeground(
                    TEXT_COLOR
            );
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args) {

        SwingUtilities.invokeLater(
                () -> {

                    ReportsUi reportsUi =
                            new ReportsUi();

                    reportsUi.setVisible(
                            true
                    );
                }
        );
    }
}