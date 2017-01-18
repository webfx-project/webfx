package emul.javafx.scene.layout;

import emul.javafx.scene.Node;
import emul.javafx.scene.Parent;

/**
 * @author Bruno Salmon
 */
public class Pane extends Region {

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

    /**
     * Creates a Pane layout.
     */
    public Pane() {
        super();
    }

    /**
     * Creates a Pane layout.
     * @param children The initial set of children for this pane.
     * @since JavaFX 8.0
     */
    public Pane(Node... children) {
        super();
        getChildren().addAll(children);
    }

}
