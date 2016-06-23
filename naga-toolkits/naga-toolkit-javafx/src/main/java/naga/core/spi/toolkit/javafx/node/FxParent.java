package naga.core.spi.toolkit.javafx.node;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import naga.core.spi.toolkit.node.GuiNode;
import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.node.Parent;

import java.lang.reflect.Field;

/**
 * @author Bruno Salmon
 */
public class FxParent<P extends javafx.scene.Parent> extends FxNode<P> implements Parent<P, Node> {

    protected final ObservableList<GuiNode<Node>> children;

    public FxParent(P parent) {
        super(parent);
        ObservableList<Node> fxChildren = null;
        try {
            Field childrenField = javafx.scene.Parent.class.getDeclaredField("children");
            childrenField.setAccessible(true);
            fxChildren = (ObservableList<Node>) childrenField.get(parent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        children = Toolkit.get().wrapNativeObservableList(fxChildren);
    }

    @Override
    public ObservableList<GuiNode<Node>> getChildren() {
        return children;
    }
}
