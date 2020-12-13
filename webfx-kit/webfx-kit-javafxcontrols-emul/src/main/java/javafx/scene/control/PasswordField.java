package javafx.scene.control;

import dev.webfx.kit.registry.javafxcontrols.JavaFxControlsRegistry;

/**
 * @author Bruno Salmon
 */
public class PasswordField extends TextField {

    public PasswordField() {
    }

    public PasswordField(String text) {
        super(text);
    }

    static {
        JavaFxControlsRegistry.registerPasswordField();
    }
}
