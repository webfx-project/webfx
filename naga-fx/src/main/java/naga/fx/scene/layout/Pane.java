package naga.fx.scene.layout;

import naga.fx.scene.Node;
import naga.fx.scene.Parent;

/**
 * @author Bruno Salmon
 */
public abstract class Pane extends Region {

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
