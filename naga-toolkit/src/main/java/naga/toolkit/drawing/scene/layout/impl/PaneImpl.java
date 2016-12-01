package naga.toolkit.drawing.scene.layout.impl;

import naga.toolkit.drawing.scene.Node;
import naga.toolkit.drawing.scene.layout.Pane;
import naga.toolkit.drawing.scene.Parent;

/**
 * @author Bruno Salmon
 */
public class PaneImpl extends RegionImpl implements Pane {

    static void setConstraint(Node node, Object key, Object value) {
        if (value == null)
            node.getProperties().remove(key);
        else
            node.getProperties().put(key, value);
        Parent parent = node.getParent();
        if (parent != null)
            parent.requestLayout();
    }

    static Object getConstraint(Node node, Object key) {
        if (node.hasProperties()) {
            Object value = node.getProperties().get(key);
            if (value != null)
                return value;
        }
        return null;
    }

}
