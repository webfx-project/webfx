package naga.core.spi.toolkit.swing.nodes;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import naga.core.spi.toolkit.GuiNode;
import naga.core.spi.toolkit.nodes.Parent;
import naga.core.spi.toolkit.swing.SwingNode;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
class SwingParent<P extends Container> extends SwingNode<P> implements Parent<P, Component> {

    SwingParent(P parent) {
        super(parent);
        children.addListener(this::onChanged);
    }

    private final ObservableList<GuiNode<Component>> children = FXCollections.observableArrayList();

    @Override
    public ObservableList<GuiNode<Component>> getChildren() {
        return children;
    }

    private void onChanged(ListChangeListener.Change<? extends GuiNode<Component>> change) {
        node.removeAll();
        for (GuiNode<Component> child : children)
            node.add(prepareChildComponent(child.unwrapToNativeNode()));
    }

    protected Component prepareChildComponent(Component child) {
        return child;
    }

}