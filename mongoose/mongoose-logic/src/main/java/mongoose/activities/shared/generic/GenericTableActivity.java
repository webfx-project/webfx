package mongoose.activities.shared.generic;

import naga.commons.util.function.Factory;
import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.Table;

/**
 * @author Bruno Salmon
 */
public abstract class GenericTableActivity<VM extends GenericTableViewModel, PM extends GenericTablePresentationModel> extends PresentationActivity<VM, PM> {

    public GenericTableActivity() {
        this(() -> (PM) new GenericTablePresentationModel());
    }

    public GenericTableActivity(Factory<PM> presentationModelFactory) {
        super(presentationModelFactory);
    }

    protected VM buildView(Toolkit toolkit) {
        // Building the UI components
        SearchBox searchBox = toolkit.createSearchBox();
        Table table = toolkit.createTable();
        CheckBox limitCheckBox = toolkit.createCheckBox();

        return (VM) new GenericTableViewModel(toolkit.createVPage()
                .setHeader(searchBox)
                .setCenter(table)
                .setFooter(limitCheckBox)
                , searchBox, table, limitCheckBox);
    }

    protected void initializePresentationModel(PM pm) {
        if (pm instanceof HasEventIdProperty)
            ((HasEventIdProperty) pm).setEventId(getParameter("eventId"));
        if (pm instanceof HasOrganizationIdProperty)
            ((HasOrganizationIdProperty) pm).setOrganizationId(getParameter("organizationId"));
    }

    protected void bindViewModelWithPresentationModel(VM vm, PM pm) {
        // Hard coded initialization
        SearchBox searchBox = vm.getSearchBox();
        CheckBox limitCheckBox = vm.getLimitCheckBox();
        searchBox.setPlaceholder("Search here to narrow the list");
        searchBox.requestFocus();
        limitCheckBox.setText("Limit to 100");

        // Initialization from the presentation model current state
        searchBox.setText(pm.searchTextProperty().getValue());
        limitCheckBox.setSelected(pm.limitProperty().getValue());

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        pm.searchTextProperty().bind(searchBox.textProperty());
        pm.limitProperty().bind(limitCheckBox.selectedProperty());
        pm.genericDisplaySelectionProperty().bind(vm.getTable().displaySelectionProperty());
        // User outputs: the presentation model changes are transferred in the UI
        vm.getTable().displayResultSetProperty().bind(pm.genericDisplayResultSetProperty());
    }

}
