package mongoose.activities.backend.organizations;

import mongoose.activities.shared.generic.GenericTableActivity;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.Table;

/**
 * @author Bruno Salmon
 */
public class OrganizationsActivity extends GenericTableActivity<OrganizationsViewModel, OrganizationsPresentationModel> {

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
        super.bindViewModelWithPresentationModel(vm, pm);
        // Hard coded initialization
        vm.getSearchBox().setPlaceholder("Enter your centre name to narrow the list");
        CheckBox withEventsCheckBox = vm.getWithEventsCheckBox();
        withEventsCheckBox.setText("With events");

        // Initialization from the presentation model current state
        withEventsCheckBox.setSelected(pm.withEventsProperty().getValue());

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        pm.withEventsProperty().bind(withEventsCheckBox.selectedProperty());
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
                .displayResultSetInto(pm.genericDisplayResultSetProperty())
                .setSelectedEntityHandler(pm.genericDisplaySelectionProperty(), organization -> {
                    if (organization != null)
                        getHistory().push("/organization/" + organization.getId().getPrimaryKey() + "/events");
                });
    }
}
