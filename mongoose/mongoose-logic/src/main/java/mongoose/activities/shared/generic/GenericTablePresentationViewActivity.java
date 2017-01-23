package mongoose.activities.shared.generic;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import mongoose.activities.shared.theme.Theme;
import naga.framework.activity.presentation.view.impl.PresentationViewActivityImpl;
import naga.framework.ui.i18n.I18n;
import naga.fxdata.control.DataGrid;

/**
 * @author Bruno Salmon
 */
public abstract class GenericTablePresentationViewActivity<PM extends GenericTablePresentationModel>
        extends PresentationViewActivityImpl<PM> {

    protected TextField searchBox;
    protected DataGrid table;
    protected CheckBox limitCheckBox;

    @Override
    protected void createViewNodes(PM pm) {
        I18n i18n = getI18n();
        searchBox = i18n.translatePromptText(new TextField(), "GenericSearchPlaceholder");
        table = new DataGrid();
        limitCheckBox = i18n.translateText(new CheckBox(), "LimitTo100");

        table.setMaxWidth(Double.MAX_VALUE);
        table.setMaxHeight(Double.MAX_VALUE);

        limitCheckBox.textFillProperty().bind(Theme.mainTextFillProperty());

        // Initialization from the presentation model current state
        searchBox.setText(pm.searchTextProperty().getValue());
        limitCheckBox.setSelected(pm.limitProperty().getValue());
        //searchBox.requestFocus();

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        pm.searchTextProperty().bind(searchBox.textProperty());
        pm.limitProperty().bind(limitCheckBox.selectedProperty());
        pm.genericDisplaySelectionProperty().bind(table.displaySelectionProperty());
        // User outputs: the presentation model changes are transferred in the UI
        table.displayResultSetProperty().bind(pm.genericDisplayResultSetProperty());
    }

    @Override
    protected Node assemblyViewNodes() {
        return new BorderPane(table, searchBox, null, limitCheckBox, null);
    }

}
