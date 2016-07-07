package naga.toolkit.providers.pivot.nodes.controls;

import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.ToggleSwitch;


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

