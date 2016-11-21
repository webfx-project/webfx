package naga.toolkit.drawing.shapes;

import naga.toolkit.drawing.shapes.impl.GroupImpl;

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
