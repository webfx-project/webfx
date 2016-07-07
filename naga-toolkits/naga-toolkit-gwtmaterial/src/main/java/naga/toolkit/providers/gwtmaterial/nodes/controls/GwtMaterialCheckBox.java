package naga.toolkit.providers.gwtmaterial.nodes.controls;

import gwt.material.design.client.ui.MaterialCheckBox;
import naga.toolkit.providers.gwt.nodes.controls.GwtSelectableButton;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.ToggleSwitch;

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
