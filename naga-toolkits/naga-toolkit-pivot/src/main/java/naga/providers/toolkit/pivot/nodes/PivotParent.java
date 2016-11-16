package naga.providers.toolkit.pivot.nodes;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.Parent;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Container;


/**
 * @author Bruno Salmon
 */
public class PivotParent<P extends Container> extends PivotNode<P> implements Parent {

    public PivotParent(P parent) {
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
            node.add(prepareChildComponent(child.unwrapToNativeNode()));
    }

    protected Component prepareChildComponent(Component child) {
        return child;
    }

}