package naga.toolkit.fx.scene.control;

import naga.toolkit.fx.scene.control.impl.TextFieldImpl;

/**
 * @author Bruno Salmon
 */
public interface TextField extends TextInputControl {

    static TextField create() {
        return new TextFieldImpl();
    }
}
