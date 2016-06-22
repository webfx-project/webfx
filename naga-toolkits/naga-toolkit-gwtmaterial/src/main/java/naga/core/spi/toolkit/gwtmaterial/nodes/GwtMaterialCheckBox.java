package naga.core.spi.toolkit.gwtmaterial.nodes;

import gwt.material.design.client.ui.MaterialCheckBox;
import naga.core.spi.toolkit.gwt.nodes.GwtSelectableButton;
import naga.core.spi.toolkit.nodes.CheckBox;
import naga.core.spi.toolkit.nodes.ToggleSwitch;

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
