package com.sairam.game;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

public class PdfReportExporter {

    private static final float PAGE_WIDTH = PDRectangle.A4.getWidth();
    private static final float PAGE_HEIGHT = PDRectangle.A4.getHeight();

    private static final float MARGIN = 35;
    private static final float HEADER_HEIGHT = 65;
    private static final float FOOTER_HEIGHT = 25;
    private static final float TABLE_HEADER_HEIGHT = 24;
    private static final float ROW_HEIGHT = 20;

    private static final float TITLE_FONT_SIZE = 18;
    private static final float SUBTITLE_FONT_SIZE = 9;
    private static final float NORMAL_FONT_SIZE = 8;
    private static final float SMALL_FONT_SIZE = 7;

    private static final DateTimeFormatter FILE_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private PdfReportExporter() {
    }

    // =========================================================
    // EXPORT REPORT
    // =========================================================

    public static File exportReport(
            String title,
            String reportType,
            Object[] summary,
            String[] columns,
            List<Object[]> rows) throws IOException {

        if (title == null || title.trim().isEmpty()) {
            title = "NUMBER COLOR GAME REPORT";
        }

        if (reportType == null || reportType.trim().isEmpty()) {
            reportType = "OVERALL";
        }

        if (summary == null) {
            summary = new Object[] {
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0
            };
        }

        if (columns == null || columns.length == 0) {
            throw new IOException("No report columns available.");
        }

        if (rows == null) {
            throw new IOException("No report data available.");
        }

        File outputFile = createOutputFile(reportType);

        File parent = outputFile.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (PDDocument document = new PDDocument()) {

            int pageNumber = 1;

            PDPage page = createPage();

            document.addPage(page);

            PDPageContentStream content =
                    new PDPageContentStream(
                            document,
                            page
                    );

            drawHeader(
                    content,
                    title,
                    reportType
            );

            float currentY =
                    PAGE_HEIGHT
                            - MARGIN
                            - HEADER_HEIGHT
                            - 15;

            currentY =
                    drawSummary(
                            content,
                            summary,
                            currentY
                    );

            currentY -= 15;

            currentY =
                    drawTableHeader(
                            content,
                            columns,
                            currentY
                    );

            for (Object[] row : rows) {

                if (currentY - ROW_HEIGHT
                        < MARGIN + FOOTER_HEIGHT) {

                    drawFooter(
                            content,
                            pageNumber
                    );

                    content.close();

                    pageNumber++;

                    page = createPage();

                    document.addPage(page);

                    content =
                            new PDPageContentStream(
                                    document,
                                    page
                            );

                    drawHeader(
                            content,
                            title,
                            reportType
                    );

                    currentY =
                            PAGE_HEIGHT
                                    - MARGIN
                                    - HEADER_HEIGHT
                                    - 15;

                    currentY =
                            drawTableHeader(
                                    content,
                                    columns,
                                    currentY
                            );
                }

                drawTableRow(
                        content,
                        columns,
                        row,
                        currentY
                );

                currentY -= ROW_HEIGHT;
            }

            drawFooter(
                    content,
                    pageNumber
            );

            content.close();

            document.save(outputFile);
        }

        System.out.println(
                "PDF report created successfully:"
        );

        System.out.println(
                outputFile.getAbsolutePath()
        );

        return outputFile;
    }

    // =========================================================
    // CREATE OUTPUT FILE
    // =========================================================

    private static File createOutputFile(
            String reportType) {

        String safeType =
                reportType
                        .trim()
                        .toLowerCase()
                        .replaceAll(
                                "[^a-z0-9]+",
                                "_"
                        );

        String timestamp =
                LocalDateTime.now()
                        .format(
                                FILE_DATE_FORMAT
                        );

        String fileName =
                "NumberColorGame_"
                        + safeType
                        + "_Report_"
                        + timestamp
                        + ".pdf";

        return new File(
                System.getProperty("user.home"),
                "Downloads"
                        + File.separator
                        + fileName
        );
    }

    // =========================================================
    // CREATE PAGE
    // =========================================================

    private static PDPage createPage() {

        return new PDPage(
                PDRectangle.A4
        );
    }

    // =========================================================
    // HEADER
    // =========================================================

    private static void drawHeader(
            PDPageContentStream content,
            String title,
            String reportType)
            throws IOException {

        // Header background
        content.setNonStrokingColor(
                new Color(35, 45, 60)
        );

        content.addRect(
                0,
                PAGE_HEIGHT - HEADER_HEIGHT,
                PAGE_WIDTH,
                HEADER_HEIGHT
        );

        content.fill();

        // Main title
        PDType1Font boldFont =
                new PDType1Font(
                        Standard14Fonts.FontName.HELVETICA_BOLD
                );

        content.beginText();

        content.setNonStrokingColor(
                Color.WHITE
        );

        content.setFont(
                boldFont,
                TITLE_FONT_SIZE
        );

        content.newLineAtOffset(
                MARGIN,
                PAGE_HEIGHT - 30
        );

        content.showText(
                safePdfText(title)
        );

        content.endText();

        // Subtitle
        PDType1Font normalFont =
                new PDType1Font(
                        Standard14Fonts.FontName.HELVETICA
                );

        content.beginText();

        content.setNonStrokingColor(
                new Color(215, 220, 225)
        );

        content.setFont(
                normalFont,
                SUBTITLE_FONT_SIZE
        );

        content.newLineAtOffset(
                MARGIN,
                PAGE_HEIGHT - 47
        );

        content.showText(
                "Number Color Game - Management Report"
        );

        content.endText();

        // Report type
        String safeReportType =
                safePdfText(reportType);

        float reportTypeWidth =
                getTextWidth(
                        safeReportType,
                        normalFont,
                        SUBTITLE_FONT_SIZE
                );

        content.beginText();

        content.setNonStrokingColor(
                new Color(215, 220, 225)
        );

        content.setFont(
                normalFont,
                SUBTITLE_FONT_SIZE
        );

        content.newLineAtOffset(
                PAGE_WIDTH
                        - MARGIN
                        - reportTypeWidth,
                PAGE_HEIGHT - 30
        );

        content.showText(
                safeReportType
        );

        content.endText();

        // Generated date
        String generatedText =
                "Generated: "
                        + LocalDateTime.now()
                                .format(
                                        DISPLAY_DATE_FORMAT
                                );

        float generatedWidth =
                getTextWidth(
                        generatedText,
                        normalFont,
                        SMALL_FONT_SIZE
                );

        content.beginText();

        content.setNonStrokingColor(
                new Color(215, 220, 225)
        );

        content.setFont(
                normalFont,
                SMALL_FONT_SIZE
        );

        content.newLineAtOffset(
                PAGE_WIDTH
                        - MARGIN
                        - generatedWidth,
                PAGE_HEIGHT - 47
        );

        content.showText(
                generatedText
        );

        content.endText();
    }

    // =========================================================
    // SUMMARY
    // =========================================================

    private static float drawSummary(
            PDPageContentStream content,
            Object[] summary,
            float startY)
            throws IOException {

        float cardWidth =
                (
                        PAGE_WIDTH
                                - (2 * MARGIN)
                                - (6 * 6)
                ) / 7;

        float cardHeight = 55;

        String[] labels = {
                "TOTAL GAMES",
                "WINS",
                "LOSSES",
                "TOTAL BET",
                "TOTAL PAYOUT",
                "PROFIT / LOSS",
                "WIN %"
        };

        PDType1Font boldFont =
                new PDType1Font(
                        Standard14Fonts.FontName.HELVETICA_BOLD
                );

        for (int i = 0; i < 7; i++) {

            float x =
                    MARGIN
                            + i * (cardWidth + 6);

            // Card background
            content.setNonStrokingColor(
                    new Color(245, 247, 250)
            );

            content.addRect(
                    x,
                    startY - cardHeight,
                    cardWidth,
                    cardHeight
            );

            content.fill();

            // Card border
            content.setStrokingColor(
                    new Color(220, 224, 228)
            );

            content.addRect(
                    x,
                    startY - cardHeight,
                    cardWidth,
                    cardHeight
            );

            content.stroke();

            // Label
            drawCenteredText(
                    content,
                    labels[i],
                    x,
                    startY - 17,
                    cardWidth,
                    boldFont,
                    6.5f,
                    new Color(90, 100, 115)
            );

            String value =
                    i < summary.length
                            ? formatValue(
                                    summary[i],
                                    i == 6
                            )
                            : "0";

            Color valueColor =
                    new Color(45, 55, 70);

            if (i == 5) {

                double profit =
                        toDouble(
                                summary[i]
                        );

                if (profit > 0) {

                    valueColor =
                            new Color(
                                    45,
                                    130,
                                    85
                            );

                } else if (profit < 0) {

                    valueColor =
                            new Color(
                                    180,
                                    65,
                                    65
                            );
                }
            }

            drawCenteredText(
                    content,
                    value,
                    x,
                    startY - 42,
                    cardWidth,
                    boldFont,
                    10,
                    valueColor
            );
        }

        return startY - cardHeight;
    }

    // =========================================================
    // TABLE HEADER
    // =========================================================

    private static float drawTableHeader(
            PDPageContentStream content,
            String[] columns,
            float y)
            throws IOException {

        float tableWidth =
                PAGE_WIDTH
                        - (2 * MARGIN);

        float[] widths =
                calculateColumnWidths(
                        columns
                );

        float x = MARGIN;

        // Header background
        content.setNonStrokingColor(
                new Color(55, 70, 90)
        );

        content.addRect(
                MARGIN,
                y - TABLE_HEADER_HEIGHT,
                tableWidth,
                TABLE_HEADER_HEIGHT
        );

        content.fill();

        PDType1Font boldFont =
                new PDType1Font(
                        Standard14Fonts.FontName.HELVETICA_BOLD
                );

        for (int i = 0; i < columns.length; i++) {

            float columnWidth =
                    widths[i];

            content.setStrokingColor(
                    new Color(220, 224, 228)
            );

            content.addRect(
                    x,
                    y - TABLE_HEADER_HEIGHT,
                    columnWidth,
                    TABLE_HEADER_HEIGHT
            );

            content.stroke();

            String header =
                    columns[i] == null
                            ? ""
                            : columns[i];

            drawCenteredText(
                    content,
                    header,
                    x,
                    y - 15,
                    columnWidth,
                    boldFont,
                    6.5f,
                    Color.WHITE
            );

            x += columnWidth;
        }

        return y - TABLE_HEADER_HEIGHT;
    }

    // =========================================================
    // TABLE ROW
    // =========================================================

    private static void drawTableRow(
            PDPageContentStream content,
            String[] columns,
            Object[] row,
            float y)
            throws IOException {

        float tableWidth =
                PAGE_WIDTH
                        - (2 * MARGIN);

        float[] widths =
                calculateColumnWidths(
                        columns
                );

        float x = MARGIN;

        // Row background
        content.setNonStrokingColor(
                Color.WHITE
        );

        content.addRect(
                MARGIN,
                y - ROW_HEIGHT,
                tableWidth,
                ROW_HEIGHT
        );

        content.fill();

        for (int i = 0; i < columns.length; i++) {

            float columnWidth =
                    widths[i];

            String value = "";

            if (row != null
                    && i < row.length
                    && row[i] != null) {

                value =
                        formatValue(
                                row[i],
                                false
                        );
            }

            // Cell border
            content.setStrokingColor(
                    new Color(225, 228, 232)
            );

            content.addRect(
                    x,
                    y - ROW_HEIGHT,
                    columnWidth,
                    ROW_HEIGHT
            );

            content.stroke();

            Color textColor =
                    new Color(45, 55, 70);

            if (columns[i] != null
                    && columns[i].equalsIgnoreCase(
                            "PROFIT / LOSS"
                    )) {

                double valueNumber =
                        toDouble(
                                row != null
                                        && i < row.length
                                        ? row[i]
                                        : null
                        );

                if (valueNumber > 0) {

                    textColor =
                            new Color(
                                    45,
                                    130,
                                    85
                            );

                } else if (valueNumber < 0) {

                    textColor =
                            new Color(
                                    180,
                                    65,
                                    65
                            );
                }
            }

            drawCellText(
                    content,
                    value,
                    x,
                    y,
                    columnWidth,
                    textColor
            );

            x += columnWidth;
        }
    }

    // =========================================================
    // CELL TEXT
    // =========================================================

    private static void drawCellText(
            PDPageContentStream content,
            String text,
            float x,
            float y,
            float width,
            Color textColor)
            throws IOException {

        if (text == null) {
            text = "";
        }

        text =
                safePdfText(text);

        PDType1Font font =
                new PDType1Font(
                        Standard14Fonts.FontName.HELVETICA
                );

        float fontSize =
                NORMAL_FONT_SIZE;

        float maxWidth =
                width - 6;

        String displayText =
                text;

        while (displayText.length() > 0
                && getTextWidth(
                        displayText,
                        font,
                        fontSize
                ) > maxWidth) {

            displayText =
                    displayText.substring(
                            0,
                            displayText.length() - 1
                    );
        }

        if (!displayText.equals(text)
                && displayText.length() > 3) {

            displayText =
                    displayText.substring(
                            0,
                            Math.max(
                                    0,
                                    displayText.length() - 3
                            )
                    )
                    + "...";
        }

        float textWidth =
                getTextWidth(
                        displayText,
                        font,
                        fontSize
                );

        float textX =
                x
                        + (width - textWidth)
                        / 2;

        float textY =
                y - 13;

        content.beginText();

        content.setNonStrokingColor(
                textColor
        );

        content.setFont(
                font,
                fontSize
        );

        content.newLineAtOffset(
                textX,
                textY
        );

        content.showText(
                displayText
        );

        content.endText();
    }

    // =========================================================
    // CENTERED TEXT
    // =========================================================

    private static void drawCenteredText(
            PDPageContentStream content,
            String text,
            float x,
            float y,
            float width,
            PDType1Font font,
            float fontSize,
            Color textColor)
            throws IOException {

        if (text == null) {
            text = "";
        }

        text =
                safePdfText(text);

        float textWidth =
                getTextWidth(
                        text,
                        font,
                        fontSize
                );

        float textX =
                x
                        + (width - textWidth)
                        / 2;

        content.beginText();

        content.setNonStrokingColor(
                textColor
        );

        content.setFont(
                font,
                fontSize
        );

        content.newLineAtOffset(
                textX,
                y
        );

        content.showText(
                text
        );

        content.endText();
    }

    // =========================================================
    // FOOTER
    // =========================================================

    private static void drawFooter(
            PDPageContentStream content,
            int pageNumber)
            throws IOException {

        float y = 20;

        // Footer line
        content.setStrokingColor(
                new Color(210, 215, 220)
        );

        content.moveTo(
                MARGIN,
                y + 10
        );

        content.lineTo(
                PAGE_WIDTH - MARGIN,
                y + 10
        );

        content.stroke();

        PDType1Font font =
                new PDType1Font(
                        Standard14Fonts.FontName.HELVETICA
                );

        String leftText =
                "Number Color Game";

        String rightText =
                "Page " + pageNumber;

        content.beginText();

        content.setNonStrokingColor(
                new Color(100, 105, 110)
        );

        content.setFont(
                font,
                SMALL_FONT_SIZE
        );

        content.newLineAtOffset(
                MARGIN,
                y
        );

        content.showText(
                leftText
        );

        content.endText();

        float rightWidth =
                getTextWidth(
                        rightText,
                        font,
                        SMALL_FONT_SIZE
                );

        content.beginText();

        content.setNonStrokingColor(
                new Color(100, 105, 110)
        );

        content.setFont(
                font,
                SMALL_FONT_SIZE
        );

        content.newLineAtOffset(
                PAGE_WIDTH
                        - MARGIN
                        - rightWidth,
                y
        );

        content.showText(
                rightText
        );

        content.endText();
    }

    // =========================================================
    // COLUMN WIDTHS
    // =========================================================

    private static float[] calculateColumnWidths(
            String[] columns) {

        int count =
                columns.length;

        float totalWidth =
                PAGE_WIDTH
                        - (2 * MARGIN);

        float[] widths =
                new float[count];

        if (count == 8) {

            widths[0] = 75;
            widths[1] = 90;
            widths[2] = 105;
            widths[3] = 105;
            widths[4] = 85;
            widths[5] = 95;
            widths[6] = 90;

            widths[7] =
                    totalWidth - 645;

            return widths;
        }

        float equalWidth =
                totalWidth / count;

        for (int i = 0; i < count; i++) {

            widths[i] =
                    equalWidth;
        }

        return widths;
    }

    // =========================================================
    // TEXT WIDTH
    // =========================================================

    private static float getTextWidth(
            String text,
            PDType1Font font,
            float fontSize)
            throws IOException {

        if (text == null
                || text.isEmpty()) {

            return 0;
        }

        return font.getStringWidth(
                safePdfText(text)
        ) / 1000
                * fontSize;
    }

    // =========================================================
    // FORMAT VALUE
    // =========================================================

    private static String formatValue(
            Object value,
            boolean percentage) {

        if (value == null) {
            return "";
        }

        if (percentage) {

            double number =
                    toDouble(value);

            return String.format(
                    "%.2f%%",
                    number
            );
        }

        if (value instanceof Double
                || value instanceof Float) {

            double number =
                    ((Number) value)
                            .doubleValue();

            if (number
                    == Math.rint(number)) {

                return String.valueOf(
                        (long) number
                );
            }

            return String.format(
                    "%.2f",
                    number
            );
        }

        return String.valueOf(
                value
        );
    }

    // =========================================================
    // DOUBLE
    // =========================================================

    private static double toDouble(
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
                    String.valueOf(
                            value
                    )
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
    // SAFE PDF TEXT
    // =========================================================

    private static String safePdfText(
            String text) {

        if (text == null) {
            return "";
        }

        StringBuilder result =
                new StringBuilder();

        for (char c : text.toCharArray()) {

            if (c >= 32 && c <= 126) {

                result.append(c);

            } else {

                result.append('?');
            }
        }

        return result.toString();
    }

    // =========================================================
    // OPEN PDF
    // =========================================================

    public static void openPdf(
            File file) {

        if (file == null) {
            return;
        }

        if (!file.exists()) {
            return;
        }

        try {

            if (!Desktop.isDesktopSupported()) {

                System.out.println(
                        "Desktop is not supported."
                );

                return;
            }

            Desktop.getDesktop()
                    .open(file);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}