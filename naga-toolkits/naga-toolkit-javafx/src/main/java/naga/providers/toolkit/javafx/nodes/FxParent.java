package naga.providers.toolkit.javafx.nodes;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.Parent;

import java.lang.reflect.Field;

/**
 * @author Bruno Salmon
 */
public class FxParent<P extends javafx.scene.Parent> extends FxNode<P> implements Parent {

    protected final ObservableList<GuiNode> children;

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
    public ObservableList<GuiNode> getChildren() {
        return children;
    }
}
