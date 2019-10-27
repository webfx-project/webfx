package mongoose.backend.controls.masterslave;

import javafx.scene.Node;
import mongoose.client.presentationmodel.HasSlaveVisualResultProperty;
import webfx.extras.visual.controls.grid.VisualGrid;

public class SlaveTableView implements UiBuilder {

    private final VisualGrid slaveTable = new VisualGrid();

    SlaveTableView(HasSlaveVisualResultProperty pm) {
        slaveTable.visualResultProperty().bind(pm.slaveVisualResultProperty());
    }

    @Override
    public Node buildUi() {
        return slaveTable;
    }

    public static SlaveTableView createAndBind(HasSlaveVisualResultProperty pm) {
        return new SlaveTableView(pm);
    }
}
