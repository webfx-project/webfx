package naga.providers.toolkit.pivot.nodes.controls;

import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.ToggleSwitch;
import org.apache.pivot.wtk.Checkbox;


/**
 * @author Bruno Salmon
 */
public class PivotCheckBox extends PivotButtonBase<org.apache.pivot.wtk.Checkbox> implements CheckBox<Checkbox>, ToggleSwitch<org.apache.pivot.wtk.Checkbox> {

    public PivotCheckBox() {
        this(new org.apache.pivot.wtk.Checkbox());
    }

    public PivotCheckBox(org.apache.pivot.wtk.Checkbox button) {
        super(button);
    }
}

