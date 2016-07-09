package naga.providers.toolkit.swing.nodes.controls;

import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.ToggleSwitch;

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

