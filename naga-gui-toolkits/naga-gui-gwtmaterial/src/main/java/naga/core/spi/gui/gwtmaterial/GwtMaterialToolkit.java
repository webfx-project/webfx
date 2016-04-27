package naga.core.spi.gui.gwtmaterial;

import naga.core.spi.gui.gwt.GwtToolkit;
import naga.core.spi.gui.gwtmaterial.nodes.GwtMaterialCheckBox;
import naga.core.spi.gui.gwtmaterial.nodes.GwtMaterialSearchBox;
import naga.core.spi.gui.gwtmaterial.nodes.GwtMaterialTable;
import naga.core.spi.gui.nodes.CheckBox;
import naga.core.spi.gui.nodes.SearchBox;
import naga.core.spi.gui.nodes.Table;
import naga.core.spi.gui.nodes.ToggleSwitch;

/**
 * @author Bruno Salmon
 */
public class GwtMaterialToolkit extends GwtToolkit {

    public GwtMaterialToolkit() {
        registerNodeFactory(Table.class, GwtMaterialTable::new);
        registerNodeFactory(CheckBox.class, GwtMaterialCheckBox::new);
        registerNodeFactory(ToggleSwitch.class, GwtMaterialCheckBox::new);
        registerNodeFactory(SearchBox.class, GwtMaterialSearchBox::new);
    }
}
