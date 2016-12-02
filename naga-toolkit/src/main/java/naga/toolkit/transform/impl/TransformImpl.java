package naga.toolkit.transform.impl;

import javafx.beans.property.Property;
import naga.toolkit.fx.geom.Point2D;
import naga.toolkit.transform.Transform;
import naga.toolkit.util.Properties;

/**
 * @author Bruno Salmon
 */
abstract class TransformImpl implements Transform {

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

    private void clearInverseCacheOnPropertyChange(Property... properties) {
        Properties.runOnPropertiesChange(property -> clearInverseCacheNow(), properties);
    }

    @Override
    public Point2D inverseTransform(double x, double y)  {
        return getInverseCache().transform(x, y);
    }
}
