package naga.providers.toolkit.swing.nodes.controls;

import naga.toolkit.spi.nodes.controls.RadioButton;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingRadioButton extends SwingButtonBase<JRadioButton> implements RadioButton<JRadioButton> {

    public SwingRadioButton() {
        this(new JRadioButton());
    }

    public SwingRadioButton(JRadioButton button) {
        super(button);
    }
}

