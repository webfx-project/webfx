package naga.toolkit.fx.scene.transform;

import naga.toolkit.fx.geom.Point2D;

/**
 * @author Bruno Salmon
 */
public interface Transform {

    /**
     * Transforms the specified point by this transform.
     * This method can be used only for 2D transforms.
     * @param x the X coordinate of the point
     * @param y the Y coordinate of the point
     * @return the transformed point
     */
    Point2D transform(double x, double y);

    /**
     * Transforms the specified point by this transform.
     * This method can be used only for 2D transforms.
     * @param point the point to be transformed
     * @return the transformed point
     * @throws NullPointerException if the specified {@code point} is null
     */
    default Point2D transform(Point2D point) {
        return transform(point.getX(), point.getY());
    }

    /**
     * Returns the inverse transform of this transform.
     * @return the inverse transform
     */
    Transform createInverse();

    default Point2D inverseTransform(double x, double y) {
        return createInverse().transform(x, y); // not optimized default method (implementation should cache inverse)
    }

    default Point2D inverseTransform(Point2D point) /*throws NonInvertibleTransformException*/ {
        return inverseTransform(point.getX(), point.getY());
    }

    Affine toAffine();
}
