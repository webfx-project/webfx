package naga.toolkit.drawing.paint.impl;

import naga.toolkit.drawing.paint.Color;

/**
 * @author Bruno Salmon
 */
public class ColorImpl implements Color {

    private double red;
    private double green;
    private double blue;
    private double opacity = 1;

    public ColorImpl(double red, double green, double blue, double opacity) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.opacity = opacity;
    }

    @Override
    public double getRed() {
        return red;
    }

    @Override
    public double getGreen() {
        return green;
    }

    @Override
    public double getBlue() {
        return blue;
    }

    @Override
    public double getOpacity() {
        return opacity;
    }
}
