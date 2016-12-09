package naga.toolkit.fx.scene.control;

import naga.toolkit.fx.scene.control.impl.ButtonImpl;

/**
 * @author Bruno Salmon
 */
public interface Button extends ButtonBase {

    static Button create() {
        return new ButtonImpl();
    }

    static Button create(String text) {
        return new ButtonImpl(text);
    }
}
