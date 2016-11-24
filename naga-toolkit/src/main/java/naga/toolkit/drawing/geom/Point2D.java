package naga.toolkit.drawing.geom;

/**
 * A 2D geometric point that usually represents the x, y coordinates.
 * It can also represent a relative magnitude vector's x, y magnitudes.
 */
public interface Point2D {

    /**
     * The x coordinate.
     * @return the x coordinate
     */
    double getX();

    /**
     * The y coordinate.
     * @return the y coordinate
     */
    double getY();

    static Point2D create(double x, double y) {
        return new Point2DImpl(x, y);
    }

}