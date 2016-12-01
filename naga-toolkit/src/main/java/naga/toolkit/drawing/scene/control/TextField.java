package naga.toolkit.drawing.scene.control;

import naga.toolkit.drawing.scene.control.impl.TextFieldImpl;

/**
 * @author Bruno Salmon
 */
public interface TextField extends TextInputControl {

    static TextField create() {
        return new TextFieldImpl();
    }
}
