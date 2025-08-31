package javafx.scene.control;

import dev.webfx.kit.registry.javafxcontrols.JavaFxControlsRegistry;
import javafx.scene.Node;
import javafx.scene.control.skin.RadioButtonSkin;

/**
 * @author Bruno Salmon
 */
public class RadioButton extends ToggleButton {

    public RadioButton() {
    }

    public RadioButton(String text) {
        super(text);
    }

    public RadioButton(String text, Node graphic) {
        super(text, graphic);
    }

    @Override
    protected void initialize() {
    }

    /**
     * Toggles the state of the radio button if and only if the RadioButton
     * has not already selected or is not part of a {@link ToggleGroup}.
     */
    @Override public void fire() {
        // we don't toggle from selected to not selected if part of a group
        if (getToggleGroup() == null || !isSelected()) {
            super.fire();
        }
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new RadioButtonSkin(this);
    }

    static {
        JavaFxControlsRegistry.registerRadioButton();
    }
}
