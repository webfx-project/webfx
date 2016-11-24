package naga.toolkit.drawing.scene.impl;

import naga.toolkit.drawing.scene.Node;
import naga.toolkit.drawing.scene.Group;

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
