package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.layoutmeasurable;

import javafx.geometry.Bounds;

/**
 * @author Bruno Salmon
 */
public final class HtmlLayoutCache {

    private Bounds cachedLayoutBounds;

    private double negWidthSize = -1;
    private double posWidthValue = -1;
    private double posWidthSize = -1;

    private double negHeightSize = -1;
    private double posHeightValue = -1;
    private double posHeightSize = -1;

    public Bounds getCachedLayoutBounds() {
        return cachedLayoutBounds;
    }

    public void setCachedLayoutBounds(Bounds cachedLayoutBounds) {
        this.cachedLayoutBounds = cachedLayoutBounds;
    }

    double getCachedSize(double value, boolean width) {
        if (width) {
            if (value < 0)
                return negWidthSize;
            if (value == posWidthValue)
                return posWidthSize;
        } else {
            if (value < 0)
                return negHeightSize;
            if (value == posHeightValue)
                return posHeightSize;
        }
        return -1;
    }

    void setCachedSize(double value, boolean width, double size) {
        if (width) {
            if (value < 0)
                negWidthSize = size;
            else {
                posWidthValue = value;
                posWidthSize = size;
            }
        } else {
            if (value < 0)
                negHeightSize = size;
            else {
                posHeightValue = value;
                posHeightSize = size;
            }
        }
    }

    void clearCache() {
        negWidthSize = posWidthSize = negHeightSize = posHeightSize = -1;
        cachedLayoutBounds = null;
    }
}
