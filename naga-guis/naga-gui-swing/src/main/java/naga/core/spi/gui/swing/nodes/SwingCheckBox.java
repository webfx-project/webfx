package naga.core.spi.gui.swing.nodes;

import naga.core.spi.gui.nodes.CheckBox;
import naga.core.spi.gui.nodes.ToggleSwitch;

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

