package naga.toolkit.fx.scene.transform;

import naga.toolkit.properties.markers.HasAngleProperty;
import naga.toolkit.properties.markers.HasPivotXProperty;
import naga.toolkit.properties.markers.HasPivotYProperty;
import naga.toolkit.fx.scene.transform.impl.RotateImpl;

/**
 * @author Bruno Salmon
 */
public interface Rotate extends Transform,
        HasAngleProperty,
        HasPivotXProperty,
        HasPivotYProperty {

    static Rotate create() {
        return new RotateImpl();
    }
}
