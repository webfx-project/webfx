package naga.core.spi.toolkit.gwtmaterial.controls;

import gwt.material.design.client.ui.MaterialCheckBox;
import naga.core.spi.toolkit.gwt.controls.GwtSelectableButton;
import naga.core.spi.toolkit.controls.CheckBox;
import naga.core.spi.toolkit.controls.ToggleSwitch;

/**
 * @author Bruno Salmon
 */
public class GwtMaterialCheckBox extends GwtSelectableButton<MaterialCheckBox> implements CheckBox<MaterialCheckBox>, ToggleSwitch<MaterialCheckBox> {

    public GwtMaterialCheckBox() {
        this(new MaterialCheckBox());
    }

    public GwtMaterialCheckBox(MaterialCheckBox button) {
        super(button);
    }
}
