package naga.providers.toolkit.android.nodes.layouts;

import android.app.Activity;
import android.view.View;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.layouts.Window;

/**
 * @author Bruno Salmon
 */
public class AndroidWindow implements Window<View> {

    private final Activity activity;

    public AndroidWindow(Activity activity) {
        this.activity = activity;
        nodeProperty.addListener((observable, oldValue, newValue) -> setWindowContent(newValue.unwrapToNativeNode()));
        titleProperty().addListener((observable, oldValue, newValue) -> activity.setTitle(newValue));
    }

    private void setWindowContent(View rootComponent) {
        activity.setContentView(rootComponent);
    }

    private final Property<GuiNode<View>> nodeProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<View>> nodeProperty() {
        return nodeProperty;
    }

    private final Property<String> titleProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> titleProperty() {
        return titleProperty;
    }
}
