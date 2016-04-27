package naga.core.spi.gui.gwtmaterial.nodes;

import com.google.gwt.user.cellview.client.DataGrid;
import naga.core.spi.gui.gwt.nodes.GwtTable;

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
