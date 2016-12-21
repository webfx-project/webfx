package mongoose.activities.shared.generic;

import naga.commons.util.function.Factory;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.fxdata.control.DataGrid;
import naga.toolkit.fx.scene.control.CheckBox;
import naga.toolkit.fx.scene.control.TextField;
import naga.toolkit.fx.scene.layout.BorderPane;

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

    protected VM buildView() {
        // Building the UI components
        TextField searchBox = new TextField();
        DataGrid table = new DataGrid();
        CheckBox limitCheckBox = new CheckBox();

        return (VM) new GenericTableViewModel(new BorderPane(table, searchBox, null, limitCheckBox, null), searchBox, table, limitCheckBox);
    }

    protected void initializePresentationModel(PM pm) {
        if (pm instanceof HasEventIdProperty)
            ((HasEventIdProperty) pm).setEventId(getParameter("eventId"));
        if (pm instanceof HasOrganizationIdProperty)
            ((HasOrganizationIdProperty) pm).setOrganizationId(getParameter("organizationId"));
    }

    protected void bindViewModelWithPresentationModel(VM vm, PM pm) {
        // Hard coded initialization
        TextField searchBox = vm.getSearchBox();
        CheckBox limitCheckBox = vm.getLimitCheckBox();
        // Initialization from the presentation model current state
        I18n i18n = getI18n();
        i18n.translatePromptText(searchBox, "GenericSearchPlaceholder").setText(pm.searchTextProperty().getValue());
        i18n.translateText(limitCheckBox, "LimitTo100").setSelected(pm.limitProperty().getValue());
        //searchBox.requestFocus();

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        pm.searchTextProperty().bind(searchBox.textProperty());
        pm.limitProperty().bind(limitCheckBox.selectedProperty());
        pm.genericDisplaySelectionProperty().bind(vm.getTable().displaySelectionProperty());
        // User outputs: the presentation model changes are transferred in the UI
        vm.getTable().displayResultSetProperty().bind(pm.genericDisplayResultSetProperty());
    }

}
