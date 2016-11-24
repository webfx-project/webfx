package naga.toolkit.drawing.scene;

import naga.toolkit.drawing.scene.impl.GroupImpl;

/**
 * @author Bruno Salmon
 */
public interface Group extends Parent {

    static Group create() {
        return new GroupImpl();
    }

    static Group create(Node... nodes) {
        return new GroupImpl(nodes);
    }

}
