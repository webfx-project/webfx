package naga.providers.toolkit.gwt.nodes.controls;

import com.google.gwt.user.client.ui.Label;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.gwt.nodes.GwtNode;
import naga.toolkit.spi.nodes.controls.TextView;

/**
 * @author Bruno Salmon
 */
public class GwtTextView extends GwtNode<Label> implements TextView<Label> {

    public GwtTextView() {
        this(new Label());
    }

    public GwtTextView(Label label) {
        super(label);
        textProperty.addListener((observable, oldValue, newValue) -> label.setText(newValue));
    }

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }

}
