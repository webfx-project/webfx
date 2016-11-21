package naga.toolkit.drawing.shapes.impl;

import naga.toolkit.drawing.shapes.Node;
import naga.toolkit.drawing.shapes.Group;

/**
 * @author Bruno Salmon
 */
public class GroupImpl extends ParentImpl implements Group {

    public GroupImpl() {
    }

    public GroupImpl(Node... nodes) {
        super(nodes);
    }
}
