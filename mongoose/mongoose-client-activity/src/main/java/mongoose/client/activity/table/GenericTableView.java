package mongoose.client.activity.table;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import mongoose.client.activity.themes.Theme;
import webfx.framework.client.ui.controls.button.ButtonFactoryMixin;
import webfx.framework.client.ui.layouts.SceneUtil;
import webfx.fxkit.extra.controls.displaydata.datagrid.DataGrid;

public class GenericTableView implements ButtonFactoryMixin {

    protected TextField searchBox;
    protected DataGrid table;
    protected CheckBox limitCheckBox;
    protected BorderPane borderPane;
    private final MasterSlaveView masterSlaveView;

    public GenericTableView(Node slaveView) {
        this(new MasterSlaveView());
        masterSlaveView.setSlaveView(slaveView);
    }

    public GenericTableView(MasterSlaveView masterSlaveView) {
        this.masterSlaveView = masterSlaveView;
    }

    public MasterSlaveView getMasterSlaveView() {
        return masterSlaveView;
    }

    public Node buildUi() {
        initUi();
        return masterSlaveView.getSplitPane();
    }

    protected void initUi() {
        searchBox = newTextFieldWithPrompt("GenericSearchPlaceholder");
        table = new DataGrid();
        BorderPane.setAlignment(table, Pos.TOP_CENTER);
        limitCheckBox = newCheckBox("LimitTo100");
        limitCheckBox.setSelected(true);
        limitCheckBox.textFillProperty().bind(Theme.mainTextFillProperty());
        table.fullHeightProperty().bind(limitCheckBox.selectedProperty());
        borderPane = new BorderPane(table, searchBox, null, limitCheckBox, null);
        masterSlaveView.setMasterView(borderPane);
    }

    public void onResume() {
        SceneUtil.autoFocusIfEnabled(searchBox);
    }

    public TextField getSearchBox() {
        return searchBox;
    }

    public DataGrid getTable() {
        return table;
    }

    public CheckBox getLimitCheckBox() {
        return limitCheckBox;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

}
