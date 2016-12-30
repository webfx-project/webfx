package mongoose.activities.backend.events;

import mongoose.activities.shared.generic.GenericTableActivity;
import mongoose.activities.shared.theme.Theme;
import naga.framework.ui.i18n.I18n;
import naga.fxdata.control.DataGrid;
import naga.fx.scene.control.CheckBox;
import naga.fx.scene.control.TextField;
import naga.fx.scene.layout.BorderPane;

/**
 * @author Bruno Salmon
 */
public class EventsActivity extends GenericTableActivity<EventsViewModel, EventsPresentationModel> {

    public EventsActivity() {
        super(EventsPresentationModel::new);
    }

    protected EventsViewModel buildView() {
        // Building the UI components
        TextField searchBox = new TextField();
        DataGrid table = new DataGrid();
        CheckBox withBookingsCheckBox = new CheckBox();
        CheckBox limitCheckBox = new CheckBox();

        searchBox.setPrefWidth(Double.MAX_VALUE);
        searchBox.setMaxWidth(Double.MAX_VALUE);
        table.setMaxWidth(Double.MAX_VALUE);
        table.setMaxHeight(Double.MAX_VALUE);

        withBookingsCheckBox.textFillProperty().bind(Theme.mainTextFillProperty());
        limitCheckBox.textFillProperty().bind(Theme.mainTextFillProperty());

        return new EventsViewModel(new BorderPane(table, searchBox, null, limitCheckBox, null), searchBox, table, limitCheckBox, withBookingsCheckBox);
    }

    protected void bindViewModelWithPresentationModel(EventsViewModel vm, EventsPresentationModel pm) {
        super.bindViewModelWithPresentationModel(vm, pm);
        // Hard coded initialization
        I18n i18n = getI18n();
        i18n.translatePromptText(vm.getSearchBox(), "EventSearchPlaceholder");
        CheckBox withBookingsCheckBox = i18n.translateText(vm.getWithBookingsCheckBox(), "WithBookings");

        // Initialization from the presentation model current state
        withBookingsCheckBox.setSelected(pm.withBookingsProperty().getValue());

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        pm.withBookingsProperty().bind(withBookingsCheckBox.selectedProperty());
    }

    protected void bindPresentationModelWithLogic(EventsPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        createReactiveExpressionFilter("{class: 'Event', alias: 'e', fields2: '(select count(1) from Document where !cancelled and event=e) as bookingsCount', where2: 'live', orderBy: 'startDate desc,id desc'}")
                // Search box condition
                .combine(pm.searchTextProperty(), s -> s == null ? null : "{where: 'lower(name) like `%" + s.toLowerCase() + "%`'}")
                .combine(pm.organizationIdProperty(), o -> o == null ? null : "{where: 'organization=" + o + "'}")
                // Limit condition
                //.combine(pm.withBookingsProperty(), "{where: '(select count(1) from Document where !cancelled and event=e) > 0'}")
                .combine(pm.limitProperty(), "{limit: '100'}")
                .setExpressionColumns("[" +
                        //"{label: 'Image', expression: 'image(`images/calendar.svg`)'}," +
                        //"{label: 'Event', expression: 'icon, name + ` ~ ` + dateIntervalFormat(startDate,endDate) + ` (` + bookingsCount + `)`'}" +
                        "{label: 'Event', expression: 'icon, name + ` ~ ` + dateIntervalFormat(startDate,endDate)`'}," +
                        "'type'," +
                        "{role: 'background', expression: 'type.background'}" +
                        "]")
                .displayResultSetInto(pm.genericDisplayResultSetProperty())
                .setSelectedEntityHandler(pm.genericDisplaySelectionProperty(), event -> {
                    if (event != null)
                        getHistory().push("/event/" + event.getPrimaryKey() + "/bookings");
                }).start();
    }
}
