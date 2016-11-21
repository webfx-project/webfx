package naga.toolkit.drawing.shapes.impl;

import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.shapes.Group;

/**
 * @author Bruno Salmon
 */
public class GroupImpl extends DrawableParentImpl implements Group {

    public GroupImpl() {
    }

    public GroupImpl(Drawable... drawables) {
        super(drawables);
    }
}
