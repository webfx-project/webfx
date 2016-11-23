package naga.toolkit.drawing.shapes.impl;

import naga.toolkit.drawing.shapes.Bounds;

/**
 * @author Bruno Salmon
 */
public abstract class BoundsImpl implements Bounds {

    private final double minX;
    private final double minY;
    private final double width;
    private final double height;
    private final double maxX;
    private final double maxY;

    @Override
    public final double getMinX() { return minX; }

    @Override
    public final double getMinY() { return minY; }

    @Override
    public final double getWidth() { return width; }

    @Override
    public final double getHeight() { return height; }

    @Override
    public final double getMaxX() { return maxX; }

    @Override
    public final double getMaxY() { return maxY; }

    /**
     * Creates a new instance of {@code Bounds} class.
     * @param minX the X coordinate of the upper-left corner
     * @param minY the Y coordinate of the upper-left corner
     * @param width the width of the {@code Bounds}
     * @param height the height of the {@code Bounds}
     */
    BoundsImpl(double minX, double minY, double width, double height) {
        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
        this.maxX = minX + width;
        this.maxY = minY + height;
    }
}

