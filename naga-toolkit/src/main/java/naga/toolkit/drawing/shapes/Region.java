package naga.toolkit.drawing.shapes;

import naga.toolkit.drawing.shapes.impl.RegionImpl;
import naga.toolkit.properties.markers.HasInsetsProperty;
import naga.toolkit.properties.markers.HasSnapToPixelProperty;

/**
 * @author Bruno Salmon
 */
public interface Region extends Parent,
        PreferenceResizableNode,
        HasInsetsProperty,
        HasSnapToPixelProperty {

    static Region create() {
        return new RegionImpl();
    }

    static Region create(Node... nodes) {
        return new RegionImpl(nodes);
    }

}
