package naga.core.spi.toolkit.swing.nodes;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.toolkit.nodes.SelectableButton;
import naga.core.spi.toolkit.swing.SwingNode;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingButtonBase<N extends AbstractButton> extends SwingNode<N> implements SelectableButton<N> {

    public SwingButtonBase(N button) {
        super(button);
        textProperty.setValue(button.getText());
        textProperty.addListener((observable, oldValue, newValue) -> button.setText(newValue));
        selectedProperty.setValue(button.isSelected());
        button.addChangeListener(event -> selectedProperty.setValue(button.isSelected()));
        selectedProperty.addListener((observable, oldValue, newValue) -> button.setSelected(newValue));
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
