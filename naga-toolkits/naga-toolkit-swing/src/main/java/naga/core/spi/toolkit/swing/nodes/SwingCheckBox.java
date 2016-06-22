package naga.core.spi.toolkit.swing.nodes;

import naga.core.spi.toolkit.nodes.CheckBox;
import naga.core.spi.toolkit.nodes.ToggleSwitch;

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

