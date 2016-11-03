package naga.toolkit.transform;

import naga.toolkit.properties.markers.HasXProperty;
import naga.toolkit.properties.markers.HasYProperty;
import naga.toolkit.transform.impl.TranslateImpl;

/**
 * @author Bruno Salmon
 */
public interface Translate extends Transform,
        HasXProperty,
        HasYProperty {

    static Translate create() {
        return new TranslateImpl();
    }

    static Translate create(double x) {
        return new TranslateImpl(x);
    }

    static Translate create(double x, double y) {
        return new TranslateImpl(x, y);
    }
}
