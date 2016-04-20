package naga.core.spi.gui.pivot.nodes;

import naga.core.spi.gui.nodes.CheckBox;
import naga.core.spi.gui.nodes.ToggleSwitch;


/**
 * @author Bruno Salmon
 */
public class PivotCheckBox extends PivotButtonBase<org.apache.pivot.wtk.Checkbox> implements CheckBox<org.apache.pivot.wtk.Checkbox>, ToggleSwitch<org.apache.pivot.wtk.Checkbox> {

    public PivotCheckBox() {
        this(new org.apache.pivot.wtk.Checkbox());
    }

    public PivotCheckBox(org.apache.pivot.wtk.Checkbox button) {
        super(button);
    }
}

