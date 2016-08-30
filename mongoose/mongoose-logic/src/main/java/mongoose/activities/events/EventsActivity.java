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
        CheckBox withBookingsCheckBox = toolkit.createCheckBox();
        CheckBox limitCheckBox = toolkit.createCheckBox();

        return new EventsViewModel(toolkit.createVPage()
                .setHeader(searchBox)
                .setCenter(table)
                .setFooter(toolkit.createHBox(withBookingsCheckBox, limitCheckBox))
                , searchBox, table, withBookingsCheckBox, limitCheckBox);
    }

    protected void bindViewModelWithPresentationModel(EventsViewModel vm, EventsPresentationModel pm) {
        // Hard coded initialization
        SearchBox searchBox = vm.getSearchBox();
        CheckBox withBookingsCheckBox = vm.getWithBookingsCheckBox();
        CheckBox limitCheckBox = vm.getLimitCheckBox();
        searchBox.setPlaceholder("Enter the event name to narrow the list");
        searchBox.requestFocus();
        withBookingsCheckBox.setText("With bookings");
        limitCheckBox.setText("Limit to 100");

        // Initialization from the presentation model current state
        searchBox.setText(pm.searchTextProperty().getValue());
        withBookingsCheckBox.setSelected(pm.withBookingsProperty().getValue());
        limitCheckBox.setSelected(pm.limitProperty().getValue());

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        pm.searchTextProperty().bind(searchBox.textProperty());
        pm.withBookingsProperty().bind(withBookingsCheckBox.selectedProperty());
        pm.limitProperty().bind(limitCheckBox.selectedProperty());
        pm.eventsDisplaySelectionProperty().bind(vm.getTable().displaySelectionProperty());
        // User outputs: the presentation model changes are transferred in the UI
        vm.getTable().displayResultSetProperty().bind(pm.eventsDisplayResultSetProperty());
    }

    @Override
    protected void initializePresentationModel(EventsPresentationModel pm) {
        pm.organizationIdProperty().setValue(getParameter("organizationId"));
    }

    protected void bindPresentationModelWithLogic(EventsPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        createReactiveExpressionFilter("{class: 'Event', alias: 'e', fields: '(select count(1) from Document where !cancelled and event=e) as bookingsCount', where: 'live', orderBy: 'startDate desc,id desc'}")
                // Search box condition
                .combine(pm.searchTextProperty(), s -> s == null ? null : "{where: 'lower(name) like `%" + s.toLowerCase() + "%`'}")
                .combine(pm.organizationIdProperty(), o -> o == null ? null : "{where: 'organization=" + o + "'}")
                // Limit condition
                .combine(pm.withBookingsProperty(), "{where: '(select count(1) from Document where !cancelled and event=e) > 0'}")
                .combine(pm.limitProperty(), "{limit: '100'}")
                .setExpressionColumns("[" +
                        "{label: 'Event', expression: 'icon, name + ` ~ ` + dateIntervalFormat(startDate,endDate) + ` (` + bookingsCount + `)`'}" +
                        "]")
                //.applyDomainModelRowStyle()
                .displayResultSetInto(pm.eventsDisplayResultSetProperty())
                .setSelectedEntityHandler(pm.eventsDisplaySelectionProperty(), event -> {
                    if (event != null)
                        getHistory().push("/event/" + event.getId().getPrimaryKey() + "/bookings");
                });
    }
}
