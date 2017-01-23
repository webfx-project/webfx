package mongoose.activities.backend.organizations;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import mongoose.activities.shared.generic.table.GenericTablePresentationViewActivity;
import mongoose.activities.shared.logic.ui.theme.Theme;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public class OrganizationsPresentationViewActivity extends GenericTablePresentationViewActivity<OrganizationsPresentationModel> {

    private CheckBox withEventsCheckBox;

    @Override
    protected void createViewNodes(OrganizationsPresentationModel pm) {
        super.createViewNodes(pm);

        I18n i18n = getI18n();
        i18n.translatePromptTextFluent(searchBox, "YourCentrePlaceholder");

        withEventsCheckBox = i18n.translateText(new CheckBox(), "WithEvents");
        withEventsCheckBox.textFillProperty().bind(Theme.mainTextFillProperty());

        // Initialization from the presentation model current state
        withEventsCheckBox.setSelected(pm.withEventsProperty().getValue());

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        pm.withEventsProperty().bind(withEventsCheckBox.selectedProperty());
    }

    @Override
    protected Node assemblyViewNodes() {
        return new BorderPane(table, searchBox, null, new HBox(10, withEventsCheckBox, limitCheckBox), null);
    }

}
