package naga.providers.toolkit.pivot.nodes.layouts;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.layouts.Window;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.DesktopApplicationContext;

/**
 * @author Bruno Salmon
 */
public class PivotWindow implements Window<Component> {

    private final org.apache.pivot.wtk.Window pWindow;

    public PivotWindow() {
        this(new org.apache.pivot.wtk.Window());
    }

    public PivotWindow(org.apache.pivot.wtk.Window pWindow) {
        this.pWindow = pWindow;
        nodeProperty.addListener((observable, oldValue, newValue) -> setWindowContent(newValue.unwrapToNativeNode()));
        titleProperty().addListener((observable, oldValue, newValue) -> pWindow.setTitle(newValue));
    }

    private void setWindowContent(Component rootComponent) {
        boolean firstShown = pWindow.getContent() == null;
        if (firstShown) {
            DesktopApplicationContext.main(Application.Adapter.class, new String[]{});
            pWindow.setContent(rootComponent);
            pWindow.setMaximized(true);
            pWindow.open(DesktopApplicationContext.getDisplays().get(0));
        }
    }

    private final Property<GuiNode<Component>> nodeProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<Component>> nodeProperty() {
        return nodeProperty;
    }

    private final Property<String> titleProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> titleProperty() {
        return titleProperty;
    }
}
