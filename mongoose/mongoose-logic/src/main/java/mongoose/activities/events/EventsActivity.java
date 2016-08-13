package mongoose.activities.events;

import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.Table;

/**
 * @author Bruno Salmon
 */
public class EventsActivity extends PresentationActivity<EventsViewModel, EventsPresentationModel> {

    public EventsActivity() {
        super(EventsPresentationModel::new);
    }

    protected EventsViewModel buildView(Toolkit toolkit) {
        // Building the UI components
        SearchBox searchBox = toolkit.createSearchBox();
        Table table = toolkit.createTable();
        CheckBox limitCheckBox = toolkit.createCheckBox();

        return new EventsViewModel(toolkit.createVPage()
                .setHeader(searchBox)
                .setCenter(table)
                .setFooter(limitCheckBox)
                , searchBox, table, limitCheckBox);
    }

    protected void bindViewModelWithPresentationModel(EventsViewModel vm, EventsPresentationModel pm) {
        // Hard coded initialization
        SearchBox searchBox = vm.getSearchBox();
        CheckBox limitCheckBox = vm.getLimitCheckBox();
        searchBox.setPlaceholder("Enter the event name to narrow the list");
        searchBox.requestFocus();
        limitCheckBox.setText("Limit to 100");

        // Initialization from the presentation model current state
        searchBox.setText(pm.searchTextProperty().getValue());
        limitCheckBox.setSelected(pm.limitProperty().getValue());

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        pm.searchTextProperty().bind(searchBox.textProperty());
        pm.limitProperty().bind(limitCheckBox.selectedProperty());
        pm.eventsDisplaySelectionProperty().bind(vm.getTable().displaySelectionProperty());
        // User outputs: the presentation model changes are transferred in the UI
        vm.getTable().displayResultSetProperty().bind(pm.eventsDisplayResultSetProperty());
    }

    protected void bindPresentationModelWithLogic(EventsPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        createReactiveExpressionFilter("{class: 'Event', alias: 'e', fields: '(select count(1) from Document where !cancelled and event=e) as bc', where: 'live', orderBy: 'startDate desc,id desc'}")
                // Search box condition
                .combine(pm.searchTextProperty(), s -> s == null ? null : "{where: 'lower(name) like `%" + s.toLowerCase() + "%`'}")
                // Limit condition
                .combine(pm.limitProperty(), "{limit: '100'}")
                .setExpressionColumns("[" +
                        "{label: 'Name', expression: 'icon, name + ` ~ ` + dateIntervalFormat(startDate,endDate) + ` (` + (0 as bc) + `)`'}," +
                        "{label: 'Centre', expression: 'organization'}" +
                        "]")
                .applyDomainModelRowStyle()
                .displayResultSetInto(pm.eventsDisplayResultSetProperty())
                .setSelectedEntityHandler(pm.eventsDisplaySelectionProperty(), event -> {
                    if (event != null)
                        getHistory().push("/event/" + event.getId().getPrimaryKey() + "/bookings");
                });
    }
}
