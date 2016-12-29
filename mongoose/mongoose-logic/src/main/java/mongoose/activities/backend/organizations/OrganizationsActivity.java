package mongoose.activities.backend.organizations;

import mongoose.activities.shared.generic.GenericTableActivity;
import naga.framework.ui.i18n.I18n;
import naga.fxdata.control.DataGrid;
import naga.fx.scene.control.CheckBox;
import naga.fx.scene.control.TextField;
import naga.fx.scene.layout.BorderPane;
import naga.fx.scene.layout.HBox;

/**
 * @author Bruno Salmon
 */
public class OrganizationsActivity extends GenericTableActivity<OrganizationsViewModel, OrganizationsPresentationModel> {

    public OrganizationsActivity() {
        super(OrganizationsPresentationModel::new);
    }

    protected OrganizationsViewModel buildView() {
        // Building the UI components
        TextField searchBox = new TextField();
        DataGrid table = new DataGrid();
        CheckBox withEventsCheckBox = new CheckBox();
        CheckBox limitCheckBox = new CheckBox();

        searchBox.setPrefWidth(Double.MAX_VALUE);
        searchBox.setMaxWidth(Double.MAX_VALUE);
        table.setMaxWidth(Double.MAX_VALUE);
        table.setMaxHeight(Double.MAX_VALUE);

        return new OrganizationsViewModel(new BorderPane(table, searchBox, null, new HBox((double) 10, withEventsCheckBox, limitCheckBox), null)
                , searchBox, table, withEventsCheckBox, limitCheckBox);
    }

    protected void bindViewModelWithPresentationModel(OrganizationsViewModel vm, OrganizationsPresentationModel pm) {
        super.bindViewModelWithPresentationModel(vm, pm);
        // Hard coded initialization
        I18n i18n = getI18n();
        i18n.translatePromptTextFluent(vm.getSearchBox(), "YourCentrePlaceholder");
        CheckBox withEventsCheckBox = i18n.translateText(vm.getWithEventsCheckBox(), "WithEvents");

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
                        "{label: 'Centre', expression: '[icon, name + ` (` + type.code + `)`]'}," +
                        "{label: 'Country', expression: '[country.icon, country.(name + ` (` + continent.name + `)`)]'}" +
                        "]")
                .applyDomainModelRowStyle()
                .displayResultSetInto(pm.genericDisplayResultSetProperty())
                .setSelectedEntityHandler(pm.genericDisplaySelectionProperty(), organization -> {
                    if (organization != null)
                        getHistory().push("/organization/" + organization.getPrimaryKey() + "/events");
                }).start();
    }
}
