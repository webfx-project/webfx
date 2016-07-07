package naga.toolkit.providers.gwtmaterial;

import com.google.gwt.user.cellview.client.DataGrid;
import gwt.material.design.client.ui.MaterialCheckBox;
import naga.toolkit.providers.gwt.GwtToolkit;
import naga.toolkit.providers.gwtmaterial.nodes.controls.GwtMaterialCheckBox;
import naga.toolkit.providers.gwtmaterial.nodes.controls.GwtMaterialSearchBox;
import naga.toolkit.providers.gwtmaterial.nodes.controls.GwtMaterialTable;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.Table;
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
