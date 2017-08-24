package emul.javafx.scene.transform;

import emul.javafx.beans.property.Property;
import emul.com.sun.javafx.geom.Point2D;
import emul.javafx.beans.value.ObservableValue;
import naga.fx.properties.Properties;

/**
 * @author Bruno Salmon
 */
public abstract class Transform {

    /**
     * Transforms the specified point by this transform.
     * This method can be used only for 2D transforms.
     * @param x the X coordinate of the point
     * @param y the Y coordinate of the point
     * @return the transformed point
     */
    public abstract Point2D transform(double x, double y);

    /**
     * Transforms the specified point by this transform.
     * This method can be used only for 2D transforms.
     * @param point the point to be transformed
     * @return the transformed point
     * @throws NullPointerException if the specified {@code point} is null
     */
    public Point2D transform(Point2D point) {
        return transform(point.x, point.y);
    }

    /**
     * Returns the inverse transform of this transform.
     * @return the inverse transform
     */
    public abstract Transform createInverse();

    public Point2D inverseTransform(Point2D point) /*throws NonInvertibleTransformException*/ {
        return inverseTransform(point.x, point.y);
    }

    public abstract Affine toAffine();

    private Transform inverseCache;
    private boolean automaticClearInverseCacheSetup;

    private Transform getInverseCache() {
        if (inverseCache == null) {
            inverseCache = createInverse();
            if (!automaticClearInverseCacheSetup) {
                clearInverseCacheOnPropertyChange(propertiesInvalidatingCache());
                automaticClearInverseCacheSetup = true;
            }
        }
        return inverseCache;
    }

    private void clearInverseCacheNow() {
        inverseCache = null;
    }

    protected abstract Property[] propertiesInvalidatingCache();

    private void clearInverseCacheOnPropertyChange(ObservableValue... properties) {
        Properties.runOnPropertiesChange(property -> clearInverseCacheNow(), properties);
    }

    public Point2D inverseTransform(double x, double y)  {
        return getInverseCache().transform(x, y);
    }
}
