package mongoose.activities.organizations;

import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.Table;

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
        CheckBox withEventsCheckBox = toolkit.createCheckBox();
        CheckBox limitCheckBox = toolkit.createCheckBox();

        return new OrganizationsViewModel(toolkit.createVPage()
                .setHeader(searchBox)
                .setCenter(table)
                .setFooter(toolkit.createHBox(withEventsCheckBox, limitCheckBox))
                , searchBox, table, withEventsCheckBox, limitCheckBox);
    }

    protected void bindViewModelWithPresentationModel(OrganizationsViewModel vm, OrganizationsPresentationModel pm) {
        // Hard coded initialization
        SearchBox searchBox = vm.getSearchBox();
        CheckBox limitCheckBox = vm.getLimitCheckBox();
        CheckBox withEventsCheckBox = vm.getWithEventsCheckBox();
        searchBox.setPlaceholder("Enter your centre name to narrow the list");
        searchBox.requestFocus();
        limitCheckBox.setText("Limit to 100");
        withEventsCheckBox.setText("With events");

        // Initialization from the presentation model current state
        searchBox.setText(pm.searchTextProperty().getValue());
        limitCheckBox.setSelected(pm.limitProperty().getValue());
        withEventsCheckBox.setSelected(pm.withEventsProperty().getValue());

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        pm.searchTextProperty().bind(searchBox.textProperty());
        pm.limitProperty().bind(limitCheckBox.selectedProperty());
        pm.withEventsProperty().bind(withEventsCheckBox.selectedProperty());
        pm.organizationsDisplaySelectionProperty().bind(vm.getTable().displaySelectionProperty());
        // User outputs: the presentation model changes are transferred in the UI
        vm.getTable().displayResultSetProperty().bind(pm.organizationsDisplayResultSetProperty());
    }

    protected void bindPresentationModelWithLogic(OrganizationsPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        createReactiveExpressionFilter("{class: 'Organization', alias: 'o', where: '!closed', orderBy: 'name'}")
                // Search box condition
                .combine(pm.searchTextProperty(), s -> s == null ? null : "{where: 'lower(name) like `%" + s.toLowerCase() + "%`'}")
                // Limit condition
                .combine(pm.limitProperty(), "{limit: '100'}")
                .combine(pm.withEventsProperty(), "{where: 'exists(select Event where live and organization=o)', orderBy: 'id'}")
                .setExpressionColumns("[" +
                        "{label: 'Center', expression: '[icon, name + ` (` + type.code + `)`]'}," +
                        "{label: 'Country', expression: '[country.icon, country.(name + ` (` + continent.name + `)`)]'}" +
                        "]")
                .applyDomainModelRowStyle()
                .displayResultSetInto(pm.organizationsDisplayResultSetProperty())
                .setSelectedEntityHandler(pm.organizationsDisplaySelectionProperty(), organization -> {
                    if (organization != null)
                        getHistory().push("/organization/" + organization.getId().getPrimaryKey() + "/events");
                });
    }
}
