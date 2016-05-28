package mongoose.logic.organizations;

import mongoose.domainmodel.DomainModelSnapshotLoader;
import naga.core.ngui.displayresultset.DisplayColumn;
import naga.core.ngui.presentation.PresentationActivity;
import naga.core.ngui.presentation.ViewBuilder;
import naga.core.ngui.rx.RxFilter;
import naga.core.spi.platform.Platform;
import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.nodes.*;

/**
 * @author Bruno Salmon
 */
public class OrganizationsActivity extends PresentationActivity<OrganizationsViewModel, OrganizationsPresentationModel> {

    public static ViewBuilder<OrganizationsViewModel> viewBuilder;

    public OrganizationsActivity() {
        setPresentationModelFactory(OrganizationsPresentationModel::new);
        setViewBuilder(viewBuilder);
    }

    protected OrganizationsViewModel buildView(Toolkit toolkit) {
        // Building the UI components
        SearchBox searchBox = toolkit.createNode(SearchBox.class);
        Table table = toolkit.createNode(Table.class);
        CheckBox limitCheckBox = toolkit.createNode(CheckBox.class);
        ActionButton testButton = toolkit.createNode(ActionButton.class);
        testButton.setText("Cart");
        VBox vbox = toolkit.createNode(VBox.class);
        vbox.getChildren().setAll(limitCheckBox, testButton);

        return new OrganizationsViewModel(toolkit.createNode(BorderPane.class)
                .setTop(searchBox)
                .setCenter(table)
                .setBottom(vbox)
                , searchBox, table, limitCheckBox, testButton);
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
        vm.getTestButton().actionEventObservable().subscribe(actionEvent -> pm.testButtonActionEventObservable().onNext(actionEvent));
        // User outputs: the presentation model changes are transferred in the UI
        vm.getTable().displayResultSetProperty().bind(pm.organizationDisplayResultSetProperty());
    }

    protected void bindPresentationModelWithLogic(OrganizationsPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        RxFilter rxFilter = createRxFilter("{class: 'Organization', where: '!closed', orderBy: 'name'}")
                .setDataSourceModel(DomainModelSnapshotLoader.getDataSourceModel())
                // Search box condition
                .combine(pm.searchTextProperty(), s -> s == null ? null : "{where: 'lower(name) like `%" + s.toLowerCase() + "%`'}")
                // Limit condition
                .combine(pm.limitProperty(), "{limit: '100'}")
                .setDisplayColumns(
                        new DisplayColumn("Name", "name + ' (' + type.code + ')'"),
                        new DisplayColumn("Country", "country.(name + ' (' + continent.name + ')')"))
                .displayResultSetInto(pm.organizationDisplayResultSetProperty());

        pm.organizationsDisplaySelectionProperty().addListener((observable, oldValue, newValue) -> {
            int selectedRow = newValue.getSelectedRow();
            Platform.log("Selected row: " + selectedRow);
            if (selectedRow >= 0)
                Platform.log("Selected entity: " + rxFilter.getCurrentEntityList().get(selectedRow));
        });

        pm.testButtonActionEventObservable().subscribe(actionEvent -> getActivityContext().getActivityRouter().getHistory().push("/cart/.570d0151724b2."));
    }
}
