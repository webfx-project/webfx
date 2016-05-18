package naga.core.spi.gui.gwt.nodes;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import naga.core.spi.gui.GuiNode;
import naga.core.spi.gui.gwt.GwtNode;
import naga.core.spi.gui.nodes.Parent;

/**
 * @author Bruno Salmon
 */
class GwtParent<P extends UIObject> extends GwtNode<P> implements Parent<P, Widget> {

    GwtParent(P node) {
        super(node);
        children.addListener(this::onChanged);
    }

    private final ObservableList<GuiNode<Widget>> children = FXCollections.observableArrayList();
    @Override
    public ObservableList<GuiNode<Widget>> getChildren() {
        return children;
    }

    private void onChanged(ListChangeListener.Change<? extends GuiNode<Widget>> change) {
        HasWidgets childContainer = getGwtChildContainer();
        if (childContainer != null) {
            childContainer.clear();
            //Platform.log("Adding " + children.size() + " children to childContainer");
            for (GuiNode<Widget> child : children)
                childContainer.add(prepareChildWidget(child.unwrapToNativeNode()));
        }
    }

    protected HasWidgets getGwtChildContainer() {
        return node instanceof HasWidgets ? (HasWidgets) node : null;
    }

    protected Widget prepareChildWidget(Widget child) {
        return child;
    }

}
