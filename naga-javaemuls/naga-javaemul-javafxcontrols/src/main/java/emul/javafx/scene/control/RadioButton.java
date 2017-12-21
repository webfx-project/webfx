package emul.javafx.scene.control;

import emul.com.sun.javafx.scene.control.skin.RadioButtonSkin;
import emul.javafx.scene.Node;

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

    @Override
    protected Skin<?> createDefaultSkin() {
        return new RadioButtonSkin(this);
    }

    // We continue to use the target toolkit layout measurable even if there is a skin
    @Override
    public boolean shouldUseLayoutMeasurable() {
        return true;
    }
}
