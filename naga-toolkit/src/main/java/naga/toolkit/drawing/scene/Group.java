package naga.toolkit.drawing.scene;

import naga.toolkit.drawing.scene.impl.GroupImpl;
import naga.toolkit.properties.markers.HasAutoSizeChildrenProperty;

/**
 * @author Bruno Salmon
 */
public interface Group extends Parent,
        HasAutoSizeChildrenProperty {

    static Group create() {
        return new GroupImpl();
    }

    static Group create(Node... nodes) {
        return new GroupImpl(nodes);
    }

}
