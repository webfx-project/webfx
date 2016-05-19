package naga.core.spi.toolkit.gwt.nodes;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.toolkit.GuiNode;
import naga.core.spi.toolkit.nodes.Window;

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
        nodeProperty.addListener((observable, oldValue, newValue) -> setWindowContent(newValue.unwrapToNativeNode()));
        titleProperty().addListener((observable, oldValue, newValue) -> rootLayoutPanel.setTitle(newValue));
    }

    private void setWindowContent(Widget rootComponent) {
        while (rootLayoutPanel.getWidgetCount() != 0)
            rootLayoutPanel.remove(0);
        rootLayoutPanel.add(rootComponent);
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
