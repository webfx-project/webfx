package naga.core.spi.toolkit.gwt.nodes;

import com.google.gwt.user.client.ui.ButtonBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.toolkit.gwt.GwtNode;

/**
 * @author Bruno Salmon
 */
public class GwtButtonBase<N extends ButtonBase> extends GwtNode<N> implements naga.core.spi.toolkit.nodes.ButtonBase<N> {

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
