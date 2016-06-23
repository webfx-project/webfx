package naga.core.spi.toolkit.gwtmaterial;

import com.google.gwt.user.cellview.client.DataGrid;
import gwt.material.design.client.ui.MaterialCheckBox;
import naga.core.spi.toolkit.gwt.GwtToolkit;
import naga.core.spi.toolkit.gwtmaterial.controls.GwtMaterialCheckBox;
import naga.core.spi.toolkit.gwtmaterial.controls.GwtMaterialSearchBox;
import naga.core.spi.toolkit.gwtmaterial.controls.GwtMaterialTable;
import naga.core.spi.toolkit.controls.CheckBox;
import naga.core.spi.toolkit.controls.SearchBox;
import naga.core.spi.toolkit.controls.Table;
import naga.core.spi.toolkit.controls.ToggleSwitch;

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
