package naga.toolkit.drawing.layout.impl;

import naga.toolkit.drawing.shapes.Node;
import naga.toolkit.drawing.layout.Pane;
import naga.toolkit.drawing.shapes.Parent;

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
