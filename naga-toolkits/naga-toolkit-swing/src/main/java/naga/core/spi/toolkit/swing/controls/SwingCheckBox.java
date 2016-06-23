package naga.core.spi.toolkit.swing.controls;

import naga.core.spi.toolkit.controls.CheckBox;
import naga.core.spi.toolkit.controls.ToggleSwitch;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingCheckBox extends SwingButtonBase<JCheckBox> implements CheckBox<JCheckBox>, ToggleSwitch<JCheckBox> {

    public SwingCheckBox() {
        this(new JCheckBox());
    }

    public SwingCheckBox(JCheckBox button) {
        super(button);
    }
}

