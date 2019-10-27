package webfx.extras.visual.impl;

import webfx.extras.visual.VisualStyle;

/**
 * @author Bruno Salmon
 */
public final class VisualStyleImpl implements VisualStyle {

    private final Double prefWidth;
    private final String textAlign;

    public VisualStyleImpl() {
        this(null, null);
    }

    public VisualStyleImpl(Double prefWidth, String textAlign) {
        this.prefWidth = prefWidth;
        this.textAlign = textAlign;
    }

    public Double getPrefWidth() {
        return prefWidth;
    }

    public String getTextAlign() {
        return textAlign;
    }
}
