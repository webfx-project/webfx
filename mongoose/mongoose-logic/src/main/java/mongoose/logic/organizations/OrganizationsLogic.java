package mongoose.logic.organizations;

import mongoose.domainmodel.DomainModelSnapshotLoader;
import naga.core.ngui.displayresultset.DisplayColumn;
import naga.core.ngui.routing.UiRouteHandler;
import naga.core.ngui.routing.UiState;
import naga.core.ngui.rx.RxFilter;
import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.gui.nodes.BorderPane;
import naga.core.spi.gui.nodes.CheckBox;
import naga.core.spi.gui.nodes.SearchBox;
import naga.core.spi.gui.nodes.Table;

/**
 * @author Bruno Salmon
 */
public class OrganizationsLogic {

    public static UiRouteHandler organizationsUiRouterHandler = new UiRouteHandler()
            .setPresentationModelFactory(OrganizationsPresentationModel::new)
            .setUiBuilder(OrganizationsLogic::buildOrganizationsUi)
            .setPresentationModelLogicBinder(OrganizationsLogic::doOrganizationsPresentationModelLogicBinding);

    private static void buildOrganizationsUi(UiState uiState) {
        // Building the UI components
        GuiToolkit toolkit = GuiToolkit.get();
        SearchBox searchBox = toolkit.createNode(SearchBox.class);
        searchBox.setPlaceholder("Enter your centre name to narrow the list");
        Table table = toolkit.createNode(Table.class);
        CheckBox limitCheckBox = toolkit.createNode(CheckBox.class);
        limitCheckBox.setText("Limit to 100");

        // Displaying the UI
        toolkit.getApplicationWindow().setNode(toolkit.createNode(BorderPane.class)
                .setTop(searchBox)
                .setCenter(table)
                .setBottom(limitCheckBox));
        // Requesting the focus on the search box
        searchBox.requestFocus();

        // Initializing the UI state from the presentation model current state
        OrganizationsPresentationModel pm = (OrganizationsPresentationModel) uiState.presentationModel();
        searchBox.setText(pm.searchTextProperty().getValue());
        limitCheckBox.setSelected(pm.limitProperty().getValue());

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        pm.searchTextProperty().bind(searchBox.textProperty());
        pm.limitProperty().bind(limitCheckBox.selectedProperty());
        // User outputs: the presentation model changes are transferred in the UI
        table.displayResultSetProperty().bind(pm.organizationDisplayResultSetProperty());
    }

    private static void doOrganizationsPresentationModelLogicBinding(OrganizationsPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        new RxFilter("{class: 'Organization', where: '!closed', orderBy: 'name'}")
                .setDomainModel(DomainModelSnapshotLoader.getOrLoadDomainModel())
                .setDataSourceId(3)
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
