package mongoose.client.activity.table;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import mongoose.client.activity.themes.Theme;
import webfx.framework.client.ui.controls.button.ButtonFactoryMixin;
import webfx.framework.client.ui.layouts.SceneUtil;
import webfx.framework.client.ui.util.background.BackgroundUtil;
import webfx.fxkit.extra.controls.displaydata.datagrid.DataGrid;

public class GenericTableView implements ButtonFactoryMixin {

    protected TextField searchBox;
    protected DataGrid table;
    protected CheckBox limitCheckBox;
    protected BorderPane borderPane;
    protected SplitPane splitPane;
    protected Region selectionRegion;

    public Node buildUi() {
        searchBox = newTextFieldWithPrompt("GenericSearchPlaceholder");
        table = new DataGrid();
        BorderPane.setAlignment(table, Pos.TOP_CENTER);
        limitCheckBox = newCheckBox("LimitTo100");
        limitCheckBox.setSelected(true);

        limitCheckBox.textFillProperty().bind(Theme.mainTextFillProperty());

        table.fullHeightProperty().bind(limitCheckBox.selectedProperty());

        borderPane = new BorderPane(table, searchBox, null, limitCheckBox, null);

        selectionRegion = new Region();
        selectionRegion.setBackground(BackgroundUtil.newBackground(Color.YELLOWGREEN));
        selectionRegion.setMaxHeight(0);

        splitPane = new SplitPane(borderPane, selectionRegion);
        splitPane.setOrientation(Orientation.VERTICAL);
        return splitPane;
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

    public SplitPane getSplitPane() {
        return splitPane;
    }

    public Region getSelectionRegion() {
        return selectionRegion;
    }
}
