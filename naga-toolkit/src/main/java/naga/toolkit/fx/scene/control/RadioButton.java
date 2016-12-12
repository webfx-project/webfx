package naga.toolkit.fx.scene.control;

import naga.toolkit.fx.scene.control.impl.RadioButtonImpl;

/**
 * @author Bruno Salmon
 */
public interface RadioButton extends ToggleButton {

    static RadioButton create() {
        return new RadioButtonImpl();
    }
}
