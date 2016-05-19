package mongoose.logic.organizations;

import mongoose.domainmodel.DomainModelSnapshotLoader;
import naga.core.ngui.displayresultset.DisplayColumn;
import naga.core.ngui.presentation.PresentationActivity;
import naga.core.ngui.presentation.UiBuilder;
import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.nodes.BorderPane;
import naga.core.spi.toolkit.nodes.CheckBox;
import naga.core.spi.toolkit.nodes.SearchBox;
import naga.core.spi.toolkit.nodes.Table;

/**
 * @author Bruno Salmon
 */
public class OrganizationsActivity extends PresentationActivity<OrganizationUiModel, OrganizationsPresentationModel> {

    public static UiBuilder<OrganizationUiModel> uiBuilder;

    public OrganizationsActivity() {
        setPresentationModelFactory(OrganizationsPresentationModel::new);
        setUiBuilder(uiBuilder);
    }

    protected OrganizationUiModel buildUiModel(Toolkit toolkit) {
        // Building the UI components
        SearchBox searchBox = toolkit.createNode(SearchBox.class);
        Table table = toolkit.createNode(Table.class);
        CheckBox limitCheckBox = toolkit.createNode(CheckBox.class);
        return new OrganizationUiModel(toolkit.createNode(BorderPane.class)
                .setTop(searchBox)
                .setCenter(table)
                .setBottom(limitCheckBox)
                , searchBox, table, limitCheckBox);
    }

    protected void bindUiModelWithPresentationModel(OrganizationUiModel um, OrganizationsPresentationModel pm) {
        // Hard coded initialization
        SearchBox searchBox = um.getSearchBox();
        CheckBox limitCheckBox = um.getLimitCheckBox();
        searchBox.setPlaceholder("Enter your centre name to narrow the list");
        searchBox.requestFocus();
        limitCheckBox.setText("Limit to 100");

        // Initialization from the presentation model current state
        searchBox.setText(pm.searchTextProperty().getValue());
        limitCheckBox.setSelected(pm.limitProperty().getValue());

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        pm.searchTextProperty().bind(searchBox.textProperty());
        pm.limitProperty().bind(limitCheckBox.selectedProperty());
        // User outputs: the presentation model changes are transferred in the UI
        um.getTable().displayResultSetProperty().bind(pm.organizationDisplayResultSetProperty());
    }

    protected void bindPresentationModelWithLogic(OrganizationsPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        createRxFilter("{class: 'Organization', where: '!closed', orderBy: 'name'}")
                .setDataSourceModel(DomainModelSnapshotLoader.getDataSourceModel())
                // Search box condition
                .combine(pm.searchTextProperty(), s -> "{where: 'lower(name) like `%" + s.toLowerCase() + "%`'}")
                // Limit condition
                .combine(pm.limitProperty(), "{limit: '100'}")
                .setDisplayColumns(
                        new DisplayColumn("Name", "name + ' (' + type.code + ')'"),
                        new DisplayColumn("Country", "country.(name + ' (' + continent.name + ')')"))
                .displayResultSetInto(pm.organizationDisplayResultSetProperty());
    }
}
