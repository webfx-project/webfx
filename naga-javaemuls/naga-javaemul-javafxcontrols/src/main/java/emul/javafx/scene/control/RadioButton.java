package emul.javafx.scene.control;

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
        return null;
    }
}
