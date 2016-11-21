package naga.toolkit.drawing.shapes;

import naga.toolkit.drawing.shapes.impl.GroupImpl;

/**
 * @author Bruno Salmon
 */
public interface Group extends DrawableParent {

    static Group create() {
        return new GroupImpl();
    }

    static Group create(Drawable... drawables) {
        return new GroupImpl(drawables);
    }

}
