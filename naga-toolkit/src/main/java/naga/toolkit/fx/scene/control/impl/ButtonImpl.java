package naga.toolkit.fx.scene.control.impl;

import naga.toolkit.fx.scene.control.Button;

/**
 * @author Bruno Salmon
 */
public class ButtonImpl extends ButtonBaseImpl implements Button {

    public ButtonImpl() {
    }

    public ButtonImpl(String text) {
        setText(text);
    }
}
