package naga.toolkit.drawing.layout;

import naga.toolkit.drawing.shapes.Node;
import naga.toolkit.drawing.shapes.Parent;
import naga.toolkit.drawing.layout.impl.RegionImpl;
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
