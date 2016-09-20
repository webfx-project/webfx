package mongoose.activities.backend.events;

import mongoose.activities.shared.generic.GenericTableActivity;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.Table;

/**
 * @author Bruno Salmon
 */
public class EventsActivity extends GenericTableActivity<EventsViewModel, EventsPresentationModel> {

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
        super.bindViewModelWithPresentationModel(vm, pm);
        // Hard coded initialization
        I18n i18n = getI18n();
        i18n.translatePlaceholder(vm.getSearchBox(), "EventSearchPlaceholder");
        CheckBox withBookingsCheckBox = i18n.translateText(vm.getWithBookingsCheckBox(), "WithBookings");

        // Initialization from the presentation model current state
        withBookingsCheckBox.setSelected(pm.withBookingsProperty().getValue());

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        pm.withBookingsProperty().bind(withBookingsCheckBox.selectedProperty());
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
                .displayResultSetInto(pm.genericDisplayResultSetProperty())
                .setSelectedEntityHandler(pm.genericDisplaySelectionProperty(), event -> {
                    if (event != null)
                        getHistory().push("/event/" + event.getId().getPrimaryKey() + "/bookings");
                }).start();
    }
}
