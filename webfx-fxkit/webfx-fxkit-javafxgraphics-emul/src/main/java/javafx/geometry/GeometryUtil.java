package javafx.geometry;

import com.sun.javafx.geom.Point2D;

/**
 * @author Bruno Salmon
 */
public final class GeometryUtil {

    /**
     * Computes the length of the vector represented by {@code (x, y).
     *
     * @param x the X magnitude of the vector
     * @param y the Y magnitude of the vector
     * @return the length of the vector.
     */
    public static double distance(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Computes the distance between point 1 and point 2.
     *
     * @param x1 the x coordinate of point 1
     * @param y1 the y coordinate of point 1
     * @param x2 the x coordinate of point 2
     * @param y2 the y coordinate of point 2
     * @return the distance between point 1 and point 2.
     */
    public static double distance(double x1, double y1, double x2, double y2) {
        return distance(x2 - x1, y2 - y1);
    }

    /**
     * Computes the distance between point 1 and point 2.
     *
     * @param point1 x and y coordinate of point 1
     * @param x2 the x coordinate of point 2
     * @param y2 the y coordinate of point 2
     * @return the distance between point 1 and point 2.
     */
    public static double distance(Point2D point1, double x2, double y2) {
        return distance(point1.x, point1.y, x2, y2);
    }

    /**
     * Computes the distance between point 1 and point 2.
     *
     * @param point1 x and y coordinate of point 1
     * @param point2 x and y coordinate of point 2
     * @return the distance between point 1 and point 2.
     */
    public static double distance(Point2D point1, Point2D point2) {
        return distance(point1, point2.x, point2.y);
    }

    /**
     * Computes the angle (in radian) between the vector and the X axis
     * @param x the X magnitude of the vector
     * @param y the Y magnitude of the vector
     * @return the angle (in radian) between the vector and the X axis
     */
    public static double angleRadian(double x, double y) {
        return Math.atan2(y, x);
    }

    /**
     * Computes the angle (in degrees) between the vector and the X axis
     * @param x the X magnitude of the vector
     * @param y the Y magnitude of the vector
     * @return the angle (in degrees) between the vector and the X axis
     */
    public static double angle(double x, double y) {
        return Math.toDegrees(angleRadian(x, y));
    }

    /**
     * Computes the angle (in degrees) between the vector and the X axis
     * @param x the X magnitude of the vector
     * @param y the Y magnitude of the vector
     * @return the angle (in degrees) between the vector and the X axis
     */
    public static double angle(double pivotX, double pivotY, double x, double y) {
        return angle(x - pivotX, y - pivotY);
    }

    public static Point2D pivot(double pivotX, double pivotY, double angleDeg, double distance) {
        return pivotRadian(pivotX, pivotY, Math.toRadians(angleDeg), distance);
    }

    public static Point2D pivotRadian(double pivotX, double pivotY, double angleRad, double distance) {
        return new Point2D((float) (pivotX + distance * Math.cos(angleRad)), (float) (pivotY + distance * Math.sin(angleRad)));
    }

    public static Point2D rotate(double pivotX, double pivotY, double x, double y, double angleDeg) {
        if (angleDeg != 0d) {
            double distance = distance(pivotX, pivotY, x, y);
            if (distance != 0d)
                return pivot(pivotX, pivotY, angle(pivotX, pivotY, x, y) + angleDeg, distance);
        }
        return new Point2D((float) x, (float) y);
    }
}