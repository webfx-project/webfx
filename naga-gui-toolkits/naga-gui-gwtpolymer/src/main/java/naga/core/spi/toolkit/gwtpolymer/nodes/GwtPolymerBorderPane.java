package naga.core.spi.toolkit.gwtpolymer.nodes;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.StyleElement;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.polymer.Polymer;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import naga.core.spi.toolkit.GuiNode;
import naga.core.spi.toolkit.gwt.GwtNode;
import naga.core.spi.toolkit.nodes.BorderPane;

/**
 * @author Bruno Salmon
 */
public class GwtPolymerBorderPane extends GwtNode<Panel> implements BorderPane<Panel, Widget> {

    public GwtPolymerBorderPane() {
        this(new HTMLPanel("div", ""));
    }

    public GwtPolymerBorderPane(Panel node) {
        super(node);
        node.getElement().addClassName("layout vertical");
        node.getElement().setAttribute("style", "100%");
        Polymer.importHref("iron-flex-layout/iron-flex-layout.html", o -> {
            Document doc = Document.get();
            StyleElement styleElement = doc.createStyleElement();
            styleElement.setAttribute("is", "custom-style");
            styleElement.setAttribute("include", "iron-flex iron-flex-alignment");
            doc.getHead().insertFirst(styleElement);
            return null;
        });
        ChangeListener<GuiNode<Widget>> onAnyNodePropertyChange = (observable, oldValue, newValue) -> updateNodesOnceAttached();
        topProperty.addListener(onAnyNodePropertyChange);
        bottomProperty.addListener(onAnyNodePropertyChange);
        centerProperty.addListener(onAnyNodePropertyChange);
        updateNodesOnceAttached();
    }

    private HandlerRegistration attachHandlerRegistration;

    private void updateNodesOnceAttached() {
        if (!node.isAttached() && attachHandlerRegistration == null)
            attachHandlerRegistration = node.addAttachHandler(event -> updateNodes());
        else
            updateNodes();
    }

    private void updateNodes() {
        if (attachHandlerRegistration != null) {
            attachHandlerRegistration.removeHandler();
            attachHandlerRegistration = null;
        }
        node.clear();
        GuiNode<Widget> topPropertyValue = topProperty.getValue();
        if (topPropertyValue != null)
            node.add(topPropertyValue.unwrapToNativeNode());
        GuiNode<Widget> centerPropertyValue = centerProperty.getValue();
        if (centerPropertyValue != null) {
            Widget widget = centerPropertyValue.unwrapToNativeNode();
            widget.setWidth("100%");
            widget.setHeight("100%");
            widget.addStyleName("flex");
            node.add(widget);
        }
        GuiNode<Widget> bottomPropertyValue = bottomProperty.getValue();
        if (bottomPropertyValue != null)
            node.add(bottomPropertyValue.unwrapToNativeNode());
    }

    private final Property<GuiNode<Widget>> topProperty = new SimpleObjectProperty<>();

    @Override
    public Property<GuiNode<Widget>> topProperty() {
        return topProperty;
    }

    private final Property<GuiNode<Widget>> centerProperty = new SimpleObjectProperty<>();

    @Override
    public Property<GuiNode<Widget>> centerProperty() {
        return centerProperty;
    }

    private final Property<GuiNode<Widget>> bottomProperty = new SimpleObjectProperty<>();

    @Override
    public Property<GuiNode<Widget>> bottomProperty() {
        return bottomProperty;
    }
}
