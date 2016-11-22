package naga.toolkit.drawing.shapes;

/**
 * The base class for objects that are used to describe the bounds of a node or
 * other scene graph object. One interesting characteristic of a Bounds object
 * is that it may have a negative width, height, or depth. A negative value
 * for any of these indicates that the Bounds are "empty".
 *
 * @since JavaFX 2.0
 */
public interface Bounds {
    /**
     * The x coordinate of the upper-left corner of this {@code Bounds}.
     */
    double getMinX();

    /**
     * The y coordinate of the upper-left corner of this {@code Bounds}.
     */
    double getMinY();

    /**
     * The width of this {@code Bounds}.
     */
    double getWidth();

    /**
     * The height of this {@code Bounds}.
     */
    double getHeight();

    /**
     * The x coordinate of the lower-right corner of this {@code Bounds}.
     */
    double getMaxX();

    /**
     * The y coordinate of the lower-right corner of this {@code Bounds}.
     */
    double getMaxY();

    /**
     * Indicates whether any of the dimensions(width, height or depth) of this bounds
     * is less than zero.
     * @return true if any of the dimensions(width, height or depth) of this bounds
     * is less than zero.
     */
    boolean isEmpty();

    /**
     * Tests if the specified point is inside the boundary of {@code Bounds}.
     *
     * @param p the specified point to be tested
     * @return true if the specified point is inside the boundary of this
     * {@code Bounds}; false otherwise.
     */
    boolean contains(Point2D p);

    /**
     * Tests if the specified {@code (x, y)} coordinates are inside the boundary
     * of {@code Bounds}.
     *
     * @param x the specified x coordinate to be tested
     * @param y the specified y coordinate to be tested
     * @return true if the specified {@code (x, y)} coordinates are inside the
     * boundary of this {@code Bounds}; false otherwise.
     */
    boolean contains(double x, double y);


    /**
     * Tests if the interior of this {@code Bounds} entirely contains the
     * specified Bounds, {@code b}.
     *
     * @param b The specified Bounds
     * @return true if the specified Bounds, {@code b}, is inside the
     * boundary of this {@code Bounds}; false otherwise.
     */
    boolean contains(Bounds b);

    /**
     * Tests if the interior of this {@code Bounds} entirely contains the
     * specified rectangular area.
     *
     * @param x the x coordinate of the upper-left corner of the specified
     * rectangular area
     * @param y the y coordinate of the upper-left corner of the specified
     * rectangular area
     * @param w the width of the specified rectangular area
     * @param h the height of the specified rectangular area
     * @return true if the interior of this {@code Bounds} entirely contains
     * the specified rectangular area; false otherwise.
     */
    boolean contains(double x, double y, double w, double h);

    /**
     * Tests if the interior of this {@code Bounds} intersects the interior
     * of a specified Bounds, {@code b}.
     *
     * @param b The specified Bounds
     * @return true if the interior of this {@code Bounds} and the interior
     * of the specified Bounds, {@code b}, intersect.
     */
    boolean intersects(Bounds b);

    /**
     * Tests if the interior of this {@code Bounds} intersects the interior
     * of a specified rectangular area.
     *
     * @param x the x coordinate of the upper-left corner of the specified
     * rectangular area
     * @param y the y coordinate of the upper-left corner of the specified
     * rectangular area
     * @param w the width of the specified rectangular area
     * @param h the height of the specified rectangular area
     * @return true if the interior of this {@code Bounds} and the interior
     * of the rectangular area intersect.
     */
    boolean intersects(double x, double y, double w, double h);

}
