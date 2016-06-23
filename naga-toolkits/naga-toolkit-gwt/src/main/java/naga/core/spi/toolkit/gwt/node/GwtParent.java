package naga.core.spi.toolkit.gwt.node;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import naga.core.spi.toolkit.node.GuiNode;
import naga.core.spi.toolkit.node.Parent;

/**
 * @author Bruno Salmon
 */
public class GwtParent<P extends UIObject> extends GwtNode<P> implements Parent<P, Widget> {

    private HandlerRegistration attachHandlerRegistration;

    public GwtParent(P node) {
        super(node);
        children.addListener(this::onChanged);
        if (node instanceof Widget) {
            Widget widget = (Widget) node;
            attachHandlerRegistration = widget.addAttachHandler(this::onAttached);
        }
    }

    private final ObservableList<GuiNode<Widget>> children = FXCollections.observableArrayList();
    @Override
    public ObservableList<GuiNode<Widget>> getChildren() {
        return children;
    }

    protected void onAttached(AttachEvent event) {
        attachHandlerRegistration.removeHandler();
        attachHandlerRegistration = null;
        onChanged(null);
    }

    private void onChanged(ListChangeListener.Change<? extends GuiNode<Widget>> change) {
        HasWidgets childContainer = attachHandlerRegistration != null ? null : getGwtChildContainer();
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
