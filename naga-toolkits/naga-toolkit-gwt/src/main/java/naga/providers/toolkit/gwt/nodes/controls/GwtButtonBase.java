package naga.providers.toolkit.gwt.nodes.controls;

import com.google.gwt.user.client.ui.ButtonBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.gwt.nodes.GwtNode;

/**
 * @author Bruno Salmon
 */
public class GwtButtonBase<N extends ButtonBase> extends GwtNode<N> implements naga.toolkit.spi.nodes.controls.ButtonBase {

    public GwtButtonBase(N button) {
        super(button);
        textProperty.setValue(button.getText());
        textProperty.addListener((observable, oldValue, newValue) -> button.setText(newValue));
    }

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }
}
