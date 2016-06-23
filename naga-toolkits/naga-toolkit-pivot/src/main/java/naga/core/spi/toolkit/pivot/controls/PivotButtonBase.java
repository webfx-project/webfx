package naga.core.spi.toolkit.pivot.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.toolkit.controls.SelectableButton;
import naga.core.spi.toolkit.pivot.node.PivotNode;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.content.ButtonData;

/**
 * @author Bruno Salmon
 */
public class PivotButtonBase<N extends Button> extends PivotNode<N> implements SelectableButton<N> {

    public PivotButtonBase(N button) {
        super(button);
        ButtonData buttonData = new ButtonData();
        textProperty.setValue(buttonData.getText());
        textProperty.addListener((observable, oldValue, newValue) -> buttonData.setText(newValue) );
        selectedProperty.setValue(button.isSelected());
        button.getButtonPressListeners().add(button1 -> selectedProperty.setValue(button1.isSelected()));
        //selectedProperty.addListener((observable, oldValue, newValue) -> button.setSelected(newValue));
        button.setButtonData(buttonData);
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
