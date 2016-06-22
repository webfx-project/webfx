package naga.core.spi.toolkit.gwt.nodes;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import naga.core.spi.platform.Platform;
import naga.core.spi.toolkit.GuiNode;
import naga.core.spi.toolkit.gwt.GwtNode;
import naga.core.spi.toolkit.nodes.VPage;
import naga.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class GwtVPage extends GwtNode<DockLayoutPanel> implements VPage<DockLayoutPanel, Widget> {

    public GwtVPage() {
        this(new DockLayoutPanel(Style.Unit.PX));
    }

    public GwtVPage(DockLayoutPanel node) {
        super(node);
        ChangeListener<GuiNode<Widget>> onAnyNodePropertyChange = (observable, oldValue, newValue) -> populate();
        topProperty.addListener(onAnyNodePropertyChange);
        bottomProperty.addListener(onAnyNodePropertyChange);
        centerProperty.addListener(onAnyNodePropertyChange);
    }

    private boolean populating;

    private void populate() {
        if (!populating) {
            populating = true;
            if (!node.isAttached())
                node.addAttachHandler(event -> addNorth());
            else {
                node.clear();
                addNorth();
            }
        }
    }

    private void addNorth() {
        populating = false;
        GuiNode<Widget> topPropertyValue = topProperty.getValue();
        if (topPropertyValue != null) {
            Widget widget = topPropertyValue.unwrapToNativeNode();
            measureHeight(widget).setHandler(asyncResult -> {
                node.clear();
                node.addNorth(widget, asyncResult.result());
                addSouth();
            });
        } else
            addSouth();
    }

    private void addSouth() {
        GuiNode<Widget> bottomPropertyValue = bottomProperty.getValue();
        if (bottomPropertyValue != null) {
            Widget widget = bottomPropertyValue.unwrapToNativeNode();
            measureHeight(widget).setHandler(asyncResult -> {
                node.addSouth(widget, asyncResult.result());
                addCenter();
            });
        } else
            addCenter();
    }

    private void addCenter() {
        GuiNode<Widget> centerPropertyValue = centerProperty.getValue();
        if (centerPropertyValue != null) {
            Widget widget = centerPropertyValue.unwrapToNativeNode();
            widget.setWidth("100%");
            widget.setHeight("100%");
            node.add(widget);
        }
    }

    private static Future<Double> measureHeight(Widget widget) {
        Future<Double> future = Future.future();
        startMeasureHeight(widget, future);
        return future;
    }

    private static void startMeasureHeight(Widget widget, Future<Double> future) {
        Element clone = widget.getElement().cloneNode(true).cast();
        clone.setAttribute("style", "position: absolute; visibility: hidden;");
        Document.get().getBody().appendChild(clone);
        finishMeasureHeight(clone, future);
    }

    private static void finishMeasureHeight(Element clone, Future<Double> future) {
        int height = clone.getOffsetHeight();
        if (height > 0) {
            future.complete((double) height);
            clone.removeFromParent();
        } else
            Platform.scheduleDelay(100, () ->  finishMeasureHeight(clone, future));
    }

    private final Property<GuiNode<Widget>> topProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<Widget>> headerProperty() {
        return topProperty;
    }

    private final Property<GuiNode<Widget>> centerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<Widget>> centerProperty() {
        return centerProperty;
    }

    private final Property<GuiNode<Widget>> bottomProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<Widget>> footerProperty() {
        return bottomProperty;
    }
}
