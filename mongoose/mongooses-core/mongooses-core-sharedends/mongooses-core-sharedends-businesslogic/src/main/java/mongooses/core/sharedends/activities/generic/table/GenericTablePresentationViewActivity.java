package mongooses.core.sharedends.activities.generic.table;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import mongooses.core.sharedends.logic.ui.theme.Theme;
import webfx.framework.activity.impl.elementals.presentation.view.impl.PresentationViewActivityImpl;
import webfx.framework.ui.controls.button.ButtonFactoryMixin;
import webfx.framework.ui.layouts.SceneUtil;
import webfx.fxkits.core.util.properties.Properties;
import webfx.fxkits.extra.control.DataGrid;

/**
 * @author Bruno Salmon
 */
public abstract class GenericTablePresentationViewActivity<PM extends GenericTablePresentationModel>
        extends PresentationViewActivityImpl<PM>
        implements ButtonFactoryMixin {

    protected TextField searchBox;
    protected DataGrid table;
    protected CheckBox limitCheckBox;

    @Override
    protected void createViewNodes(PM pm) {
        searchBox = newTextFieldWithPrompt("GenericSearchPlaceholder");
        table = new DataGrid();
        BorderPane.setAlignment(table, Pos.TOP_CENTER);
        limitCheckBox = newCheckBox("LimitTo100");

        limitCheckBox.textFillProperty().bind(Theme.mainTextFillProperty());

        // Initialization from the presentation model current state
        searchBox.setText(pm.searchTextProperty().getValue());
        limitCheckBox.setSelected(true);
        //searchBox.requestFocus();

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        pm.searchTextProperty().bind(searchBox.textProperty());
        //pm.limitProperty().bind(Bindings.when(limitCheckBox.selectedProperty()).then(table.heightProperty().divide(36)).otherwise(-1)); // not implemented in webfx-fxkits-emul-javafxbase
        Properties.runNowAndOnPropertiesChange(() -> pm.limitProperty().setValue(limitCheckBox.isSelected() ? table.getHeight() / 36 : -1), limitCheckBox.selectedProperty(), table.heightProperty());
        table.fullHeightProperty().bind(limitCheckBox.selectedProperty());
        //pm.limitProperty().bind(limitCheckBox.selectedProperty());
        pm.genericDisplaySelectionProperty().bind(table.displaySelectionProperty());
        // User outputs: the presentation model changes are transferred in the UI
        table.displayResultProperty().bind(pm.genericDisplayResultProperty());
    }

    @Override
    protected Node assemblyViewNodes() {
        return new BorderPane(table, searchBox, null, null, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        SceneUtil.autoFocusIfEnabled(searchBox);
    }
}
