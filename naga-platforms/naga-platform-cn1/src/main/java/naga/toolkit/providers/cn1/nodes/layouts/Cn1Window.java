package naga.toolkit.providers.cn1.nodes.layouts;

import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.layouts.Window;

/**
 * @author Bruno Salmon
 */
public class Cn1Window implements Window<Component> {

    private final Form form;

    public Cn1Window() {
        this(new Form(new BorderLayout()));
    }

    public Cn1Window(Form form) {
        this.form = form;
        nodeProperty.addListener((observable, oldValue, newValue) -> setWindowContent(newValue.unwrapToNativeNode()));
        titleProperty().addListener((observable, oldValue, newValue) -> form.setTitle(newValue));
    }

    private void setWindowContent(Component rootComponent) {
        form.removeAll();
        form.add(BorderLayout.CENTER, rootComponent);
        form.show();
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
