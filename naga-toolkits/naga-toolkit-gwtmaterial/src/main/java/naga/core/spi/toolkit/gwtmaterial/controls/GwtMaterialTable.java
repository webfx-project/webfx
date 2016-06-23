package naga.core.spi.toolkit.gwtmaterial.controls;

import com.google.gwt.user.cellview.client.DataGrid;
import naga.core.spi.toolkit.gwt.controls.GwtTable;

/**
 * @author Bruno Salmon
 */
public class GwtMaterialTable extends GwtTable {

    public GwtMaterialTable() {
    }

    public GwtMaterialTable(DataGrid<Integer> node) {
        super(node);
        node.addStyleName("bordered");
    }
}
