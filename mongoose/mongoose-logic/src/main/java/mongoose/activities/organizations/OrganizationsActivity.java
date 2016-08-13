package mongoose.activities.organizations;

import naga.platform.spi.Platform;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.Table;
import naga.framework.ui.presentation.PresentationActivity;

/**
 * @author Bruno Salmon
 */
public class OrganizationsActivity extends PresentationActivity<OrganizationsViewModel, OrganizationsPresentationModel> {

    public OrganizationsActivity() {
        super(OrganizationsPresentationModel::new);
    }

    protected OrganizationsViewModel buildView(Toolkit toolkit) {
        // Building the UI components
        SearchBox searchBox = toolkit.createSearchBox();
        Table table = toolkit.createTable();
        CheckBox limitCheckBox = toolkit.createCheckBox();

        return new OrganizationsViewModel(toolkit.createVPage()
                .setHeader(searchBox)
                .setCenter(table)
                .setFooter(limitCheckBox)
                , searchBox, table, limitCheckBox);
    }

    protected void bindViewModelWithPresentationModel(OrganizationsViewModel vm, OrganizationsPresentationModel pm) {
        // Hard coded initialization
        SearchBox searchBox = vm.getSearchBox();
        CheckBox limitCheckBox = vm.getLimitCheckBox();
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
        pm.organizationsDisplaySelectionProperty().bind(vm.getTable().displaySelectionProperty());
        // User outputs: the presentation model changes are transferred in the UI
        vm.getTable().displayResultSetProperty().bind(pm.organizationsDisplayResultSetProperty());
    }

    protected void bindPresentationModelWithLogic(OrganizationsPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        createReactiveExpressionFilter("{class: 'Organization', where: '!closed', orderBy: 'name'}")
                // Search box condition
                .combine(pm.searchTextProperty(), s -> s == null ? null : "{where: 'lower(name) like `%" + s.toLowerCase() + "%`'}")
                // Limit condition
                .combine(pm.limitProperty(), "{limit: '100'}")
                .setExpressionColumns("[" +
                        "{label: 'Name', expression: '[icon, name + ` (` + type.code + `)`]'}," +
                        "'country', 'country.continent'" +
                        "]")
                .applyDomainModelRowStyle()
                .displayResultSetInto(pm.organizationsDisplayResultSetProperty())
                .setSelectedEntityHandler(pm.organizationsDisplaySelectionProperty(), entity -> {
                    if (entity != null)
                        Platform.log("Selected entity: " + entity);
                });
    }
}
