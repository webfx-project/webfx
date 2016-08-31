package naga.providers.toolkit.swing.util;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class JGradientLabel extends JLabel {

    private Color topColor;
    private Color bottomColor;
    private boolean selected;

    {
        setOpaque(false);
    }

    public JGradientLabel() {
    }

    public JGradientLabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
    }

    public void setVerticalGradientColors(Color topColor, Color bottomColor) {
        this.topColor = topColor;
        this.bottomColor = bottomColor;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (topColor != null)
            GradientUtil.paintVerticalGradient(this, topColor, bottomColor, g);
        else
            GradientUtil.paintVerticalGradient(this, g, selected);
        super.paintComponent(g);
    }

}
