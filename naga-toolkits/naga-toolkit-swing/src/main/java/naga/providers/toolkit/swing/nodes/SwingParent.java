package naga.providers.toolkit.swing.nodes;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.Parent;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingParent<P extends Container> extends SwingNode<P> implements Parent {

    public SwingParent(P parent) {
        super(parent);
        children.addListener(this::onChanged);
    }

    private final ObservableList<GuiNode> children = FXCollections.observableArrayList();

    @Override
    public ObservableList<GuiNode> getChildren() {
        return children;
    }

    private void onChanged(ListChangeListener.Change<? extends GuiNode> change) {
        node.removeAll();
        for (GuiNode child : children)
            addChild(prepareChildComponent(child.unwrapToNativeNode()));
    }

    protected Component prepareChildComponent(Component child) {
        return child;
    }

    protected void addChild(Component child) {
        node.add(child);
    }

}