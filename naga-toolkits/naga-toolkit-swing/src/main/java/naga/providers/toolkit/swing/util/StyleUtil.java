package naga.providers.toolkit.swing.util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class StyleUtil {
    public static final Color tableGridColor = new Color(187, 187, 187);
    private final static Border cellPaddingBorder = BorderFactory.createEmptyBorder(9, 5, 5, 5);
    private final static Border cellHeaderBorder = new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, tableGridColor), cellPaddingBorder);
    private final static Color fontColor = new Color(0, 0, 0, (int) (0.7 * 255));
    private static final float fontSize = 16f;
    private static final String fontStartPath = "/mongoose/client/java/fonts/Roboto";
    private static final Font boldItalicFont = SwingImageStore.getFont(fontStartPath + "-BoldItalic.ttf").deriveFont(fontSize);
    private static final Font italicFont = SwingImageStore.getFont(fontStartPath + "-Italic.ttf").deriveFont(fontSize);
    private static final Font boldFont = SwingImageStore.getFont(fontStartPath + "-Bold.ttf").deriveFont(fontSize);
    private static final Font font = SwingImageStore.getFont(fontStartPath + "-Regular.ttf").deriveFont(fontSize);

    public static void styleCellComponent(Component cellComponent, String rowStyle, boolean header, String textAlign, boolean selected) {
        JComponent jComponent = cellComponent instanceof JComponent ? (JComponent) cellComponent : null;
        if (jComponent != null)
            jComponent.setBorder(header ? cellHeaderBorder : cellPaddingBorder);
        cellComponent.setForeground(fontColor);
        cellComponent.setBackground(getRowColor(rowStyle));
        boolean bold = header || isRowBold(rowStyle);
        boolean italic = !header && isRowItalic(rowStyle);
        cellComponent.setFont(getFont(bold, italic));
        if (cellComponent instanceof JLabel) {
            JLabel jLabel = (JLabel) cellComponent;
            if (header)
                textAlign = "center";
            int alignment = "right".equals(textAlign) ? SwingConstants.RIGHT : "center".equals(textAlign) ? SwingConstants.CENTER : SwingConstants.LEFT;
            jLabel.setHorizontalAlignment(alignment);
            if (jLabel instanceof JGradientLabel)
                ((JGradientLabel) jLabel).setSelected(selected);
        }
    }

    public static Font getFont(boolean bold, boolean italic) {
        if (!bold && !italic) return font;
        if (bold && !italic) return boldFont;
        if (!bold) return italicFont;
        return boldItalicFont;
    }

    private static boolean isRowBold(String rowStyle) {
        return rowStyle != null && (rowStyle.contains("kmc") || rowStyle.contains("unread"));
    }

    private static boolean isRowItalic(String rowStyle) {
        return rowStyle != null && (rowStyle.contains("specialRate") || rowStyle.contains("branch"));
    }

    private static Color getRowColor(String rowStyle) {
        Color backgroundColor = Color.white;
        if (rowStyle != null) {
            if (rowStyle.contains("abandoned"))
                backgroundColor = Color.decode("#E0CDCD");
            if (rowStyle.contains("absent"))
                backgroundColor = Color.decode("#FF8080");
            if (rowStyle.contains("cancelled"))
                backgroundColor = Color.decode("#E0A0CD");
            if (rowStyle.contains("arrived"))
                backgroundColor = Color.decode("#09EF00");
            if (rowStyle.contains("arrivedWithBalance"))
                backgroundColor = Color.decode("#01afef");
            if (rowStyle.contains("flagged"))
                backgroundColor = Color.decode("#FF8800");
            if (rowStyle.contains("changed"))
                backgroundColor = Color.decode("#A8A8FF");
            if (rowStyle.contains("passReady"))
                backgroundColor = Color.decode("#5DC8CD");
            if (rowStyle.contains("confirmed"))
                backgroundColor = Color.decode("#A8A8FF");
            if (rowStyle.contains("paidInFull"))
                backgroundColor = Color.decode("#CCFF00");
            if (rowStyle.contains("paidEnough"))
                backgroundColor = Color.decode("#FAFD00");
            if (rowStyle.contains("overPaid"))
                backgroundColor = Color.decode("#FF9955");
            if (rowStyle.contains("noDeposit"))
                backgroundColor = Color.decode("#FFFFFF");
            if (rowStyle.contains("willPay"))
                backgroundColor = Color.decode("#E0E0E0");
            if (rowStyle.contains("paidUnderMinimal"))
                backgroundColor = Color.decode("#D3BC5F");
            if (rowStyle.contains("pending"))
                backgroundColor = Color.decode("#E0CDCD");
            if (rowStyle.contains("successful"))
                backgroundColor = Color.decode("#CCFF00");
            if (rowStyle.contains("failed"))
                backgroundColor = Color.decode("#FF8080");
            if (rowStyle.contains("kmc"))
                backgroundColor = Color.decode("#dfabfc");
            if (rowStyle.contains("kbc"))
                backgroundColor = Color.decode("#fdfe96");
            if (rowStyle.contains("branch"))
                backgroundColor = Color.decode("#cef7ff");
            if (rowStyle.contains("closed"))
                backgroundColor = Color.decode("#E0CDCD");
        }
        return backgroundColor;
    }
}
