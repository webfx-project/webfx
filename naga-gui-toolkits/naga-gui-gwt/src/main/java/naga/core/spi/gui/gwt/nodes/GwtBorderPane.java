package naga.core.spi.gui.gwt.nodes;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import naga.core.spi.gui.GuiNode;
import naga.core.spi.gui.gwt.GwtNode;
import naga.core.spi.gui.nodes.BorderPane;

/**
 * @author Bruno Salmon
 */
public class GwtBorderPane extends GwtNode<DockLayoutPanel> implements BorderPane<DockLayoutPanel, Widget> {

    public GwtBorderPane() {
        this(new DockLayoutPanel(Style.Unit.EM));
    }

    public GwtBorderPane(DockLayoutPanel node) {
        super(node);
        topProperty.addListener(this::onNodePropertyChange);
        bottomProperty.addListener(this::onNodePropertyChange);
        centerProperty.addListener(this::onNodePropertyChange);
    }

    private void onNodePropertyChange(ObservableValue<? extends GuiNode<Widget>> observable, GuiNode<Widget> oldValue, GuiNode<Widget> newValue) {
        node.clear();
        GuiNode<Widget> topPropertyValue = topProperty.getValue();
        if (topPropertyValue != null)
            node.addNorth(topPropertyValue.unwrapToNativeNode(), 5);
        GuiNode<Widget> bottomPropertyValue = bottomProperty.getValue();
        if (bottomPropertyValue != null)
            node.addSouth(bottomPropertyValue.unwrapToNativeNode(), 2.5);
        GuiNode<Widget> centerPropertyValue = centerProperty.getValue();
        if (centerPropertyValue != null) {
            Widget centerWidget = centerPropertyValue.unwrapToNativeNode();
            centerWidget.setWidth("100%");
            centerWidget.setHeight("100%");
            node.add(centerWidget);
        }
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
