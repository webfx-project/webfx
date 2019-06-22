package mongoose.backend.controls.masterslave;

import javafx.scene.Node;
import mongoose.client.presentationmodel.HasSlaveDisplayResultProperty;
import webfx.fxkit.extra.controls.displaydata.datagrid.DataGrid;

public class SlaveTableView implements UiBuilder {

    private final DataGrid slaveTable = new DataGrid();

    SlaveTableView(HasSlaveDisplayResultProperty pm) {
        slaveTable.displayResultProperty().bind(pm.slaveDisplayResultProperty());
    }

    @Override
    public Node buildUi() {
        return slaveTable;
    }

    public static SlaveTableView createAndBind(HasSlaveDisplayResultProperty pm) {
        return new SlaveTableView(pm);
    }
}
