package com.sairam.game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

public class DepositUi extends JFrame {

    private static final long serialVersionUID = 1L;

    // =========================================================
    // CONFIGURATION
    // =========================================================

    /*
     * QR CODE
     *
     * Put your QR image here:
     *
     * src/com/sairam/game/qr_code.jpeg
     *
     * The application first tries the classpath resource,
     * then the local project path.
     */
    private static final String QR_CODE_RESOURCE =
    	
            "/com/sairam/game/qr_code.jpeg.jpeg";

    private static final String QR_CODE_PATH =
             "/com/sairam/game/qr_code.jpeg.jpeg\";";

    /*
     * Replace this with your actual WhatsApp number.
     *
     * Example:
     * 919876543210
     *
     * Do NOT use:
     * +91
     * spaces
     * hyphens
     */
    private static final String WHATSAPP_NUMBER =
            "916304913846";

    private static final int MIN_DEPOSIT = 10;
    private static final int MAX_DEPOSIT = 1000000;

    // QR display size
    private static final int QR_PANEL_SIZE = 280;
    private static final int QR_IMAGE_MAX_SIZE = 255;

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color DARK =
            new Color(35, 31, 55);

    private static final Color PURPLE =
            new Color(108, 72, 180);

    private static final Color PURPLE_DARK =
            new Color(74, 45, 130);

    private static final Color GREEN =
            new Color(32, 140, 82);

    private static final Color LIGHT_BG =
            new Color(245, 247, 250);

    private static final Color BORDER =
            new Color(220, 220, 225);

    // =========================================================
    // OBJECTS
    // =========================================================

    private final String playerName;

    private final PlayerDao playerDao;

    private final DepositDao depositDao;

    // =========================================================
    // UI COMPONENTS
    // =========================================================

    private JLabel balanceLabel;

    private JTextField amountField;

    private JLabel qrLabel;

    private JLabel statusLabel;

    private JButton sendWhatsAppButton;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public DepositUi(String playerName) {

        if (playerName == null || playerName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Player name cannot be empty."
            );
        }

        this.playerName = playerName.trim();

        playerDao = new PlayerDao();

        depositDao = new DepositDao();

        setTitle(
                "Lucky King - Deposit - " + this.playerName
        );

        setSize(
                600,
                800
        );

        setMinimumSize(
                new Dimension(
                        550,
                        720
                )
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);

        createUI();

        loadBalance();
    }

    // =========================================================
    // CREATE UI
    // =========================================================

    private void createUI() {

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(
                                15,
                                15
                        )
                );

        mainPanel.setBackground(
                LIGHT_BG
        );

        mainPanel.setBorder(
                new EmptyBorder(
                        20,
                        25,
                        20,
                        25
                )
        );

        // =====================================================
        // HEADER
        // =====================================================

        JPanel headerPanel =
                new JPanel(
                        new BorderLayout()
                );

        headerPanel.setOpaque(false);

        JLabel titleLabel =
                new JLabel(
                        "💰  DEPOSIT CREDITS"
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        titleLabel.setForeground(
                DARK
        );

        JLabel playerLabel =
                new JLabel(
                        "Player: " + playerName
                );

        playerLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        playerLabel.setForeground(
                Color.DARK_GRAY
        );

        headerPanel.add(
                titleLabel,
                BorderLayout.WEST
        );

        headerPanel.add(
                playerLabel,
                BorderLayout.EAST
        );

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        // =====================================================
        // CENTER PANEL
        // =====================================================

        JPanel centerPanel =
                new JPanel();

        centerPanel.setLayout(
                new BoxLayout(
                        centerPanel,
                        BoxLayout.Y_AXIS
                )
        );

        centerPanel.setBackground(
                Color.WHITE
        );

        centerPanel.setBorder(
                new EmptyBorder(
                        20,
                        25,
                        20,
                        25
                )
        );

        // =====================================================
        // CURRENT BALANCE
        // =====================================================

        JLabel balanceTitle =
                new JLabel(
                        "CURRENT BALANCE"
                );

        balanceTitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        balanceTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        balanceTitle.setForeground(
                Color.GRAY
        );

        centerPanel.add(
                balanceTitle
        );

        centerPanel.add(
                Box.createVerticalStrut(5)
        );

        balanceLabel =
                new JLabel(
                        "₹ 0"
                );

        balanceLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        balanceLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        balanceLabel.setForeground(
                PURPLE
        );

        centerPanel.add(
                balanceLabel
        );

        centerPanel.add(
                Box.createVerticalStrut(15)
        );

        // =====================================================
        // SCAN & PAY
        // =====================================================

        JLabel qrTitle =
                new JLabel(
                        "SCAN & PAY"
                );

        qrTitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        qrTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        qrTitle.setForeground(
                DARK
        );

        centerPanel.add(
                qrTitle
        );

        centerPanel.add(
                Box.createVerticalStrut(4)
        );

        JLabel phonePeLabel =
                new JLabel(
                        "Scan & Pay Using PhonePe App"
                );

        phonePeLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        phonePeLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        phonePeLabel.setForeground(
                Color.DARK_GRAY
        );

        centerPanel.add(
                phonePeLabel
        );

        centerPanel.add(
                Box.createVerticalStrut(10)
        );

        // =====================================================
        // QR CODE PANEL
        // =====================================================

        JPanel qrPanel =
                new JPanel(
                        new GridBagLayout()
                );

        qrPanel.setBackground(
                Color.WHITE
        );

        qrPanel.setPreferredSize(
                new Dimension(
                        QR_PANEL_SIZE,
                        QR_PANEL_SIZE
                )
        );

        qrPanel.setMinimumSize(
                new Dimension(
                        QR_PANEL_SIZE,
                        QR_PANEL_SIZE
                )
        );

        qrPanel.setMaximumSize(
                new Dimension(
                        QR_PANEL_SIZE,
                        QR_PANEL_SIZE
                )
        );

        qrPanel.setBorder(
                new LineBorder(
                        BORDER,
                        2
                )
        );

        qrLabel =
                new JLabel();

        qrLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        qrLabel.setVerticalAlignment(
                SwingConstants.CENTER
        );

        qrPanel.add(
                qrLabel
        );

        loadQrCode();

        centerPanel.add(
                qrPanel
        );

        centerPanel.add(
                Box.createVerticalStrut(8)
        );

        // =====================================================
        // PAYMENT NAME
        // =====================================================

        JLabel paymentName =
                new JLabel(
                        "PhonePe Payment"
                );

        paymentName.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        paymentName.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        paymentName.setForeground(
                PURPLE
        );

        centerPanel.add(
                paymentName
        );

        centerPanel.add(
                Box.createVerticalStrut(15)
        );

        // =====================================================
        // INSTRUCTIONS
        // =====================================================

        JLabel instructionLabel =
                new JLabel(
                        "<html><center>"
                                + "1. Scan the QR code and make payment.<br>"
                                + "2. Enter the same amount below.<br>"
                                + "3. Click SEND REQUEST ON WHATSAPP.<br>"
                                + "4. Admin will verify your payment.<br>"
                                + "5. Credits will be added after approval."
                                + "</center></html>"
                );

        instructionLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        instructionLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        instructionLabel.setForeground(
                Color.DARK_GRAY
        );

        centerPanel.add(
                instructionLabel
        );

        centerPanel.add(
                Box.createVerticalStrut(18)
        );

        // =====================================================
        // AMOUNT LABEL
        // =====================================================

        JLabel amountLabel =
                new JLabel(
                        "Deposit Amount"
                );

        amountLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        amountLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        amountLabel.setForeground(
                DARK
        );

        centerPanel.add(
                amountLabel
        );

        centerPanel.add(
                Box.createVerticalStrut(7)
        );

        // =====================================================
        // AMOUNT FIELD
        // =====================================================

        amountField =
                new JTextField();

        amountField.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        amountField.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        amountField.setPreferredSize(
                new Dimension(
                        300,
                        45
                )
        );

        amountField.setMinimumSize(
                new Dimension(
                        300,
                        45
                )
        );

        amountField.setMaximumSize(
                new Dimension(
                        300,
                        45
                )
        );

        amountField.setBorder(
                new LineBorder(
                        BORDER,
                        1
                )
        );

        centerPanel.add(
                amountField
        );

        centerPanel.add(
                Box.createVerticalStrut(5)
        );

        // =====================================================
        // RANGE
        // =====================================================

        JLabel rangeLabel =
                new JLabel(
                        "Minimum ₹"
                                + MIN_DEPOSIT
                                + "  |  Maximum ₹"
                                + MAX_DEPOSIT
                );

        rangeLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        rangeLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        rangeLabel.setForeground(
                Color.GRAY
        );

        centerPanel.add(
                rangeLabel
        );

        centerPanel.add(
                Box.createVerticalStrut(14)
        );

        // =====================================================
        // WHATSAPP BUTTON
        // =====================================================

        sendWhatsAppButton =
                new JButton(
                        "📱  SEND REQUEST ON WHATSAPP"
                );

        stylePrimaryButton(
                sendWhatsAppButton
        );

        sendWhatsAppButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        sendWhatsAppButton.addActionListener(
                e -> sendDepositRequest()
        );

        centerPanel.add(
                sendWhatsAppButton
        );

        centerPanel.add(
                Box.createVerticalStrut(10)
        );

        // =====================================================
        // VIEW REQUESTS
        // =====================================================

        JButton requestsButton =
                new JButton(
                        "VIEW MY DEPOSIT REQUESTS"
                );

        styleSecondaryButton(
                requestsButton
        );

        requestsButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        requestsButton.addActionListener(
                e -> showMyRequests()
        );

        centerPanel.add(
                requestsButton
        );

        centerPanel.add(
                Box.createVerticalStrut(8)
        );

        // =====================================================
        // STATUS
        // =====================================================

        statusLabel =
                new JLabel(
                        " "
                );

        statusLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        statusLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        statusLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        centerPanel.add(
                statusLabel
        );

        // =====================================================
        // SCROLL PANEL
        // =====================================================

        JScrollPane scrollPane =
                new JScrollPane(
                        centerPanel
                );

        scrollPane.setBorder(
                null
        );

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // =====================================================
        // FOOTER
        // =====================================================

        JLabel footer =
                new JLabel(
                        "Credits are added only after payment verification."
                );

        footer.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        footer.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        footer.setForeground(
                Color.GRAY
        );

        mainPanel.add(
                footer,
                BorderLayout.SOUTH
        );

        add(
                mainPanel
        );
    }

    // =========================================================
    // LOAD BALANCE
    // =========================================================

    private void loadBalance() {

        try {

            int credits =
                    playerDao.getCredits(
                            playerName
                    );

            balanceLabel.setText(
                    "₹ " + credits
            );

        } catch (SQLException e) {

            balanceLabel.setText(
                    "Unable to load"
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // LOAD QR CODE
    // =========================================================

    private void loadQrCode() {

        try {

            BufferedImage image = null;

            // =================================================
            // FIRST TRY: CLASSPATH RESOURCE
            // =================================================

            try (InputStream inputStream =
                         getClass().getResourceAsStream(
                                 QR_CODE_RESOURCE
                         )) {

                if (inputStream != null) {

                    image =
                            ImageIO.read(
                                    inputStream
                            );
                }
            }

            // =================================================
            // SECOND TRY: PROJECT FILE
            // =================================================

            if (image == null) {

                File file =
                        new File(
                                QR_CODE_PATH
                        );

                if (file.exists()) {

                    image =
                            ImageIO.read(
                                    file
                            );
                }
            }

            // =================================================
            // THIRD TRY: CURRENT WORKING DIRECTORY
            // =================================================

            if (image == null) {

                File file =
                        new File(
                                "qr_code.jpeg"
                        );

                if (file.exists()) {

                    image =
                            ImageIO.read(
                                    file
                            );
                }
            }

            // =================================================
            // QR NOT FOUND
            // =================================================

            if (image == null) {

                qrLabel.setIcon(
                        null
                );

                qrLabel.setText(
                        "<html><center>"
                                + "<b>QR CODE NOT FOUND</b><br><br>"
                                + "Please place your QR image at:<br><br>"
                                + "<b>src/com/sairam/game/qr_code.jpeg</b>"
                                + "</center></html>"
                );

                qrLabel.setFont(
                        new Font(
                                "Arial",
                                Font.PLAIN,
                                12
                        )
                );

                qrLabel.setForeground(
                        Color.RED
                );

                return;
            }

            // =================================================
            // VALIDATE IMAGE
            // =================================================

            if (image.getWidth() <= 0 ||
                    image.getHeight() <= 0) {

                throw new Exception(
                        "QR image has invalid dimensions."
                );
            }

            // =================================================
            // SCALE WITHOUT DISTORTION
            // =================================================

            double scaleX =
                    (double) QR_IMAGE_MAX_SIZE
                            / image.getWidth();

            double scaleY =
                    (double) QR_IMAGE_MAX_SIZE
                            / image.getHeight();

            double scale =
                    Math.min(
                            scaleX,
                            scaleY
                    );

            int newWidth =
                    Math.max(
                            1,
                            (int)
                                    Math.round(
                                            image.getWidth()
                                                    * scale
                                    )
                    );

            int newHeight =
                    Math.max(
                            1,
                            (int)
                                    Math.round(
                                            image.getHeight()
                                                    * scale
                                    )
                    );

            Image scaledImage =
                    image.getScaledInstance(
                            newWidth,
                            newHeight,
                            Image.SCALE_SMOOTH
                    );

            // =================================================
            // DISPLAY QR
            // =================================================

            qrLabel.setText(
                    ""
            );

            qrLabel.setForeground(
                    Color.BLACK
            );

            qrLabel.setIcon(
                    new ImageIcon(
                            scaledImage
                    )
            );

            qrLabel.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            qrLabel.setVerticalAlignment(
                    SwingConstants.CENTER
            );

        } catch (Exception e) {

            qrLabel.setIcon(
                    null
            );

            qrLabel.setText(
                    "<html><center>"
                            + "<b>UNABLE TO LOAD QR CODE</b><br><br>"
                            + e.getMessage()
                            + "</center></html>"
            );

            qrLabel.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            12
                    )
            );

            qrLabel.setForeground(
                    Color.RED
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // SEND DEPOSIT REQUEST
    // =========================================================

    private void sendDepositRequest() {

        String amountText =
                amountField.getText().trim();

        if (amountText.isEmpty()) {

            showError(
                    "Please enter the deposit amount."
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

            showError(
                    "Please enter a valid whole number."
            );

            amountField.requestFocus();

            return;
        }

        if (amount < MIN_DEPOSIT) {

            showError(
                    "Minimum deposit amount is ₹"
                            + MIN_DEPOSIT
                            + "."
            );

            amountField.requestFocus();

            return;
        }

        if (amount > MAX_DEPOSIT) {

            showError(
                    "Maximum deposit amount is ₹"
                            + MAX_DEPOSIT
                            + "."
            );

            amountField.requestFocus();

            return;
        }

        try {

            // =================================================
            // CREATE PENDING REQUEST
            // =================================================

            int requestId =
                    depositDao.createDepositRequest(
                            playerName,
                            amount
                    );

            // =================================================
            // WHATSAPP MESSAGE
            // =================================================

            String message =
                    "Hello Admin,\n\n"
                            + "I have made a deposit payment.\n\n"
                            + "Player Name: "
                            + playerName
                            + "\n"
                            + "Deposit Amount: ₹"
                            + amount
                            + "\n"
                            + "Deposit Request ID: #"
                            + requestId
                            + "\n\n"
                            + "Please verify my payment and add "
                            + "the credits to my Lucky King account.";

            openWhatsApp(
                    message
            );

            statusLabel.setForeground(
                    GREEN
            );

            statusLabel.setText(
                    "Request #"
                            + requestId
                            + " created - PENDING"
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Deposit request created successfully.\n\n"
                            + "Request ID: #"
                            + requestId
                            + "\n"
                            + "Amount: ₹"
                            + amount
                            + "\n\n"
                            + "Please send the WhatsApp message.\n"
                            + "Credits will be added after admin verification.",
                    "Deposit Request Created",
                    JOptionPane.INFORMATION_MESSAGE
            );

            amountField.setText(
                    ""
            );

        } catch (SQLException e) {

            e.printStackTrace();

            showError(
                    e.getMessage()
            );
        }
    }

    // =========================================================
    // OPEN WHATSAPP
    // =========================================================

    private void openWhatsApp(
            String message
    ) {

        try {

            String encodedMessage =
                    URLEncoder.encode(
                            message,
                            StandardCharsets.UTF_8
                    );

            String url =
                    "https://wa.me/"
                            + WHATSAPP_NUMBER
                            + "?text="
                            + encodedMessage;

            if (!Desktop.isDesktopSupported()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Desktop browser is not supported.\n\n"
                                + "Please send the deposit request manually.",
                        "WhatsApp",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            Desktop.getDesktop().browse(
                    new URI(url)
            );

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open WhatsApp.\n\n"
                            + "Please send the request manually.",
                    "WhatsApp Error",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // =========================================================
    // SHOW MY REQUESTS
    // =========================================================

    private void showMyRequests() {

        try {

            List<Object[]> requests =
                    depositDao.getPlayerDepositRequests(
                            playerName
                    );

            if (requests.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "You don't have any deposit requests.",
                        "Deposit Requests",
                        JOptionPane.INFORMATION_MESSAGE
                );

                return;
            }

            StringBuilder text =
                    new StringBuilder();

            text.append(
                    "YOUR DEPOSIT REQUESTS\n"
            );

            text.append(
                    "==============================\n\n"
            );

            for (Object[] row : requests) {

                int id =
                        ((Number) row[0]).intValue();

                int amount =
                        ((Number) row[1]).intValue();

                String status =
                        String.valueOf(
                                row[2]
                        );

                String createdAt =
                        String.valueOf(
                                row[3]
                        );

                text.append(
                        "Request #"
                )
                        .append(
                                id
                        )
                        .append(
                                "\n"
                        );

                text.append(
                        "Amount: ₹"
                )
                        .append(
                                amount
                        )
                        .append(
                                "\n"
                        );

                text.append(
                        "Status: "
                )
                        .append(
                                status
                        )
                        .append(
                                "\n"
                        );

                text.append(
                        "Created: "
                )
                        .append(
                                createdAt
                        )
                        .append(
                                "\n"
                        );

                text.append(
                        "------------------------------\n"
                );
            }

            JTextArea textArea =
                    new JTextArea(
                            text.toString()
                    );

            textArea.setEditable(
                    false
            );

            textArea.setFont(
                    new Font(
                            "Monospaced",
                            Font.PLAIN,
                            13
                    )
            );

            textArea.setMargin(
                    new Insets(
                            10,
                            10,
                            10,
                            10
                    )
            );

            JScrollPane scrollPane =
                    new JScrollPane(
                            textArea
                    );

            scrollPane.setPreferredSize(
                    new Dimension(
                            500,
                            400
                    )
            );

            JOptionPane.showMessageDialog(
                    this,
                    scrollPane,
                    "My Deposit Requests",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException e) {

            e.printStackTrace();

            showError(
                    "Unable to load deposit requests.\n\n"
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // PRIMARY BUTTON STYLE
    // =========================================================

    private void stylePrimaryButton(
            JButton button
    ) {

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        button.setForeground(
                Color.WHITE
        );

        button.setBackground(
                GREEN
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                new EmptyBorder(
                        12,
                        20,
                        12,
                        20
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setOpaque(
                true
        );
    }

    // =========================================================
    // SECONDARY BUTTON STYLE
    // =========================================================

    private void styleSecondaryButton(
            JButton button
    ) {

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(
                PURPLE_DARK
        );

        button.setBackground(
                Color.WHITE
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                new LineBorder(
                        PURPLE,
                        1
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    // =========================================================
    // ERROR
    // =========================================================

    private void showError(
            String message
    ) {

        statusLabel.setForeground(
                Color.RED
        );

        statusLabel.setText(
                "Error"
        );

        JOptionPane.showMessageDialog(
                this,
                message == null
                        ? "An unexpected error occurred."
                        : message,
                "Deposit Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // =========================================================
    // MAIN - TESTING ONLY
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    try {

                        DepositUi ui =
                                new DepositUi(
                                        "Sairam"
                                );

                        ui.setLocationRelativeTo(
                                null
                        );

                        ui.setVisible(
                                true
                        );

                    } catch (Exception e) {

                        e.printStackTrace();

                        JOptionPane.showMessageDialog(
                                null,
                                "Unable to start Deposit UI.\n\n"
                                        + e.getMessage(),
                                "Deposit UI Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
        );
    }
}
