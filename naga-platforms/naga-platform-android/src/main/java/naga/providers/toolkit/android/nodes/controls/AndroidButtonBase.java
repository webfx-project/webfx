package naga.providers.toolkit.android.nodes.controls;

import android.widget.Button;
import android.widget.CheckBox;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.spi.nodes.controls.SelectableButton;
import naga.providers.toolkit.android.nodes.AndroidNode;

/**
 * @author Bruno Salmon
 */
public class AndroidButtonBase<N extends Button> extends AndroidNode<N> implements SelectableButton {

    public AndroidButtonBase(N button) {
        super(button);
        textProperty.setValue(button.getText().toString());
        textProperty.addListener((observable, oldValue, newValue) -> button.setText(newValue));

        if (button instanceof CheckBox) {
            CheckBox checkBox = (CheckBox) button;
            button.setOnClickListener(view -> selectedProperty.setValue(checkBox.isChecked()));
            selectedProperty.addListener((observable, oldValue, newValue) -> checkBox.setChecked(newValue));
        } else {
            button.setOnClickListener(view -> selectedProperty.setValue(button.isSelected()));
            selectedProperty.addListener((observable, oldValue, newValue) -> button.setSelected(newValue));
        }
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
