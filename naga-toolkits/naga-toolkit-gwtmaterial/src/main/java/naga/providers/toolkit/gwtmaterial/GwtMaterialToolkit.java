package naga.providers.toolkit.gwtmaterial;

import com.google.gwt.user.cellview.client.DataGrid;
import gwt.material.design.client.ui.MaterialCheckBox;
import naga.providers.toolkit.gwt.GwtToolkit;
import naga.providers.toolkit.gwtmaterial.nodes.controls.GwtMaterialCheckBox;
import naga.providers.toolkit.gwtmaterial.nodes.controls.GwtMaterialSearchBox;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.Table;
import naga.providers.toolkit.gwtmaterial.nodes.controls.GwtMaterialTable;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.ToggleSwitch;

/**
 * @author Bruno Salmon
 */
public class GwtMaterialToolkit extends GwtToolkit {

    public GwtMaterialToolkit() {
        registerNodeFactoryAndWrapper(Table.class, GwtMaterialTable::new, DataGrid.class, GwtMaterialTable::new);
        registerNodeFactoryAndWrapper(CheckBox.class, GwtMaterialCheckBox::new, MaterialCheckBox.class, GwtMaterialCheckBox::new);
        registerNodeFactory(ToggleSwitch.class, GwtMaterialCheckBox::new);
        registerNodeFactory(SearchBox.class, GwtMaterialSearchBox::new);
    }
}
