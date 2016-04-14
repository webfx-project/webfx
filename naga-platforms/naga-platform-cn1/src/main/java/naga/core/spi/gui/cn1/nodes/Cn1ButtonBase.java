package naga.core.spi.gui.cn1.nodes;

import com.codename1.ui.Button;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.gui.cn1.Cn1Node;
import naga.core.spi.gui.nodes.ButtonBase;

/**
 * @author Bruno Salmon
 */
public class Cn1ButtonBase<N extends Button> extends Cn1Node<N> implements ButtonBase<N> {

    public Cn1ButtonBase(N button) {
        super(button);
        textProperty.setValue(button.getText());
        textProperty.addListener((observable, oldValue, newValue) -> button.setText(newValue));
        selectedProperty.setValue(button.isSelected());
        button.addActionListener(event -> selectedProperty.setValue(button.isSelected()));
        //selectedProperty.addListener((observable, oldValue, newValue) -> button.setSelected(newValue));
    }

    private final Property<Boolean> selectedProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Boolean> selectedProperty() {
        return selectedProperty;
    }

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }
}
