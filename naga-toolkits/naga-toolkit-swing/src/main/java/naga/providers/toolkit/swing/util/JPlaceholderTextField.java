package naga.providers.toolkit.swing.util;

import naga.commons.util.Strings;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class JPlaceholderTextField extends JTextField {

    private String placeholder;

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (Strings.isEmpty(getText()) && Strings.isNotEmpty(placeholder)) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getDisabledTextColor());
            g2d.drawString(placeholder, getInsets().left, g.getFontMetrics().getMaxAscent() + getInsets().top);
        }
    }
}
