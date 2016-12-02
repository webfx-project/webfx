package naga.toolkit.fx.scene.layout;

import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.Parent;
import naga.toolkit.fx.scene.layout.impl.RegionImpl;
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
