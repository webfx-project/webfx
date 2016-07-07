package naga.toolkit.providers.gwtmaterial.nodes.controls;

import com.google.gwt.user.cellview.client.DataGrid;
import naga.toolkit.providers.gwt.nodes.controls.GwtTable;

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
