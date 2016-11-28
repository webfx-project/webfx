package naga.toolkit.drawing.scene.control;

import naga.toolkit.drawing.scene.control.impl.ButtonImpl;

/**
 * @author Bruno Salmon
 */
public interface Button extends ButtonBase {

    static Button create() {
        return new ButtonImpl();
    }
}
