package mongoose.backend.controls.masterslave;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import mongoose.client.presentationmodel.*;
import webfx.framework.client.ui.controls.ControlFactoryMixin;
import webfx.fxkit.extra.controls.displaydata.datagrid.DataGrid;
import webfx.fxkit.util.properties.Properties;

public final class MasterTableView implements UiBuilder {

    private final DataGrid masterTable = new DataGrid();
    private CheckBox masterLimitCheckBox;

    private MasterTableView(HasMasterDisplayResultProperty pm, ControlFactoryMixin mixin) {
        masterTable.displayResultProperty().bind(pm.masterDisplayResultProperty());
        if (pm instanceof HasMasterDisplaySelectionProperty)
            ((HasMasterDisplaySelectionProperty) pm).masterDisplaySelectionProperty().bindBidirectional(masterTable.displaySelectionProperty());
        if (pm instanceof HasLimitProperty) {
            masterLimitCheckBox = mixin.newCheckBox("LimitTo100");
            masterLimitCheckBox.setSelected(true);
            Properties.runNowAndOnPropertiesChange(() -> ((HasLimitProperty) pm).limitProperty().setValue(masterLimitCheckBox.isSelected() ? 30 : -1), masterLimitCheckBox.selectedProperty());
            masterTable.fullHeightProperty().bind(masterLimitCheckBox.selectedProperty());
        }
    }

    @Override
    public Node buildUi() {
        if (masterLimitCheckBox == null)
            return masterTable;
        BorderPane.setAlignment(masterTable, Pos.TOP_CENTER);
        return new BorderPane(masterTable, null, null, masterLimitCheckBox, null);
    }

    public static MasterTableView createAndBind(HasMasterDisplayResultProperty pm, ControlFactoryMixin mixin) {
        return new MasterTableView(pm, mixin);
    }
}
