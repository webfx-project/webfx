package naga.toolkit.drawing.shapes;

import naga.toolkit.drawing.shapes.impl.BoundingBoxImpl;

/**
 * A rectangular bounding box which is used to describe the bounds of a node
 * or other scene graph object.
 */
public interface BoundingBox extends Bounds {

    /**
     * Creates a new instance of 2D {@code BoundingBox}.
     * @param minX the X coordinate of the upper-left corner
     * @param minY the Y coordinate of the upper-left corner
     * @param width the width of the {@code BoundingBoxImpl}
     * @param height the height of the {@code BoundingBoxImpl}
     */
    static BoundingBox create(double minX, double minY, double width, double height) {
        return new BoundingBoxImpl(minX, minY, width, height);
    }
}
