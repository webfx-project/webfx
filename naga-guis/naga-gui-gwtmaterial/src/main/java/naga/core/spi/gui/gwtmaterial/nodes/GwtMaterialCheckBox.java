package naga.core.spi.gui.gwtmaterial.nodes;

import gwt.material.design.client.ui.MaterialCheckBox;
import naga.core.spi.gui.nodes.CheckBox;
import naga.core.spi.gui.nodes.ToggleSwitch;

/**
 * @author Bruno Salmon
 */
public class GwtMaterialCheckBox extends GwtButtonBase<MaterialCheckBox> implements CheckBox<MaterialCheckBox>, ToggleSwitch<MaterialCheckBox> {

    public GwtMaterialCheckBox() {
        this(new MaterialCheckBox());
    }

    public GwtMaterialCheckBox(MaterialCheckBox button) {
        super(button);
    }
}
