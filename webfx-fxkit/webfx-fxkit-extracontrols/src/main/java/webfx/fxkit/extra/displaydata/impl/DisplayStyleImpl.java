package webfx.fxkit.extra.displaydata.impl;

import webfx.fxkit.extra.displaydata.DisplayStyle;

/**
 * @author Bruno Salmon
 */
public final class DisplayStyleImpl implements DisplayStyle {

    private final Double prefWidth;
    private final String textAlign;

    public DisplayStyleImpl() {
        this(null, null);
    }

    public DisplayStyleImpl(Double prefWidth, String textAlign) {
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
