package naga.toolkit.providers.gwt.nodes.layouts;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.layouts.Window;

/**
 * @author Bruno Salmon
 */
public class GwtWindow implements Window<Widget> {

    private final RootLayoutPanel rootLayoutPanel;

    public GwtWindow() {
        this(RootLayoutPanel.get());
    }

    public GwtWindow(RootLayoutPanel rootLayoutPanel) {
        this.rootLayoutPanel = rootLayoutPanel;
        nodeProperty.addListener((observable, oldValue, newValue) -> { if (newValue != null) setWindowContent(newValue.unwrapToNativeNode()); });
        titleProperty().addListener((observable, oldValue, newValue) -> rootLayoutPanel.setTitle(newValue));
    }

    private void setWindowContent(Widget rootComponent) {
        while (rootLayoutPanel.getWidgetCount() != 0)
            rootLayoutPanel.remove(0);
        //Platform.log("Setting window root " + rootComponent);
        rootLayoutPanel.add(rootComponent);
        //Platform.log("Ok");
    }

    private final Property<GuiNode<Widget>> nodeProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<Widget>> nodeProperty() {
        return nodeProperty;
    }

    private final Property<String> titleProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> titleProperty() {
        return titleProperty;
    }
}
