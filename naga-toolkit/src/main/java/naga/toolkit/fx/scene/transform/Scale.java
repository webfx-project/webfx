package naga.toolkit.fx.scene.transform;

import naga.toolkit.properties.markers.HasXProperty;
import naga.toolkit.properties.markers.HasYProperty;
import naga.toolkit.fx.scene.transform.impl.ScaleImpl;

/**
 * @author Bruno Salmon
 */
public interface Scale extends Transform,
        HasXProperty,
        HasYProperty {

    static Scale create() {
        return new ScaleImpl();
    }

    static Scale create(double x, double y) {
        return new ScaleImpl(x, y);
    }
}
