package mongoose.backend.activities.bookings;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import mongoose.backend.operations.bookings.RouteToNewBackendBookingRequest;
import mongoose.backend.operations.cloneevent.RouteToCloneEventRequest;
import mongoose.client.activity.eventdependent.EventDependentPresentationModel;
import mongoose.client.activity.eventdependent.EventDependentViewDomainActivity;
import mongoose.client.activity.table.GroupMasterSlaveView;
import mongoose.client.entities.util.FilterButtonSelectorFactoryMixin;
import mongoose.client.entities.util.Filters;
import mongoose.shared.domainmodel.functions.AbcNames;
import mongoose.shared.entities.Document;
import mongoose.shared.entities.Filter;
import webfx.framework.client.operation.action.OperationActionFactoryMixin;
import webfx.framework.client.ui.controls.button.EntityButtonSelector;
import webfx.framework.client.ui.filter.ReactiveExpressionFilter;
import webfx.framework.client.ui.filter.ReactiveExpressionFilterFactoryMixin;
import webfx.framework.client.ui.filter.StringFilter;
import webfx.framework.client.ui.layouts.SceneUtil;
import webfx.fxkit.extra.controls.displaydata.datagrid.DataGrid;
import webfx.fxkit.util.properties.Properties;
import webfx.platform.shared.util.Strings;

import static webfx.framework.client.ui.layouts.LayoutUtil.*;

final class BookingsActivity extends EventDependentViewDomainActivity
        implements OperationActionFactoryMixin,
        FilterButtonSelectorFactoryMixin,
        ReactiveExpressionFilterFactoryMixin {

    private TextField searchBox; // Keeping this reference to activate focus on activity resume

    private final BookingsPresentationModel pm = new BookingsPresentationModel();

    @Override
    public EventDependentPresentationModel getPresentationModel() {
        return pm; // eventId and organizationId will then be updated from route
    }

    @Override
    public Node buildUi() {
        BorderPane container = new BorderPane();
        // Building the top bar
        Button newBookingButton = newButton(newAction(() -> new RouteToNewBackendBookingRequest(getEventId(), getHistory()))),
               cloneEventButton = newButton(newAction(() -> new RouteToCloneEventRequest(getEventId(), getHistory())));
        EntityButtonSelector<Filter> conditionSelector = createDocumentConditionFilterButtonSelector(container),
                                     groupSelector     = createDocumentGroupFilterButtonSelector(container),
                                     columnsSelector   = createDocumentColumnsFilterButtonSelector(container);
        searchBox = newTextFieldWithPrompt("GenericSearchPlaceholder");
        container.setTop(new HBox(10, setUnmanagedWhenInvisible(newBookingButton), conditionSelector.getButton(), groupSelector.getButton(), columnsSelector.getButton(), setMaxHeightToInfinite(setHGrowable(searchBox)), setUnmanagedWhenInvisible(cloneEventButton)));

        // Building the main content, which is a group/master/slave view (group = group view, master = bookings table + limit checkbox, slave = booking details)
        GroupView groupView = new GroupView();

        DataGrid bookingsTable = new DataGrid();
        BorderPane.setAlignment(bookingsTable, Pos.TOP_CENTER);
        CheckBox limitCheckBox = newCheckBox("LimitTo100");
        limitCheckBox.setSelected(true);
        BorderPane masterPane = new BorderPane(bookingsTable, null, null, limitCheckBox, null);

        BookingDetailsPanel bookingDetailsPanel = new BookingDetailsPanel((Pane) getNode(), this, getDataSourceModel());

        GroupMasterSlaveView groupMasterSlaveView = new GroupMasterSlaveView(Orientation.VERTICAL,
                groupView.buildUi(),
                masterPane,
                bookingDetailsPanel.buildUi());
        container.setCenter(groupMasterSlaveView.getSplitPane());

        // Initialization from the presentation model current state
        searchBox.setText(pm.searchTextProperty().getValue());

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        pm.searchTextProperty().bind(searchBox.textProperty());
        Properties.runNowAndOnPropertiesChange(() -> pm.limitProperty().setValue(limitCheckBox.isSelected() ? 30 : -1), limitCheckBox.selectedProperty());
        bookingsTable.fullHeightProperty().bind(limitCheckBox.selectedProperty());
        //pm.limitProperty().bind(limitCheckBox.selectedProperty());
        pm.genericDisplaySelectionProperty().bindBidirectional(bookingsTable.displaySelectionProperty());
        // User outputs: the presentation model changes are transferred in the UI
        bookingsTable.displayResultProperty().bind(pm.genericDisplayResultProperty());

        groupMasterSlaveView.groupVisibleProperty() .bind(Properties.compute(pm.groupStringFilterProperty(), s -> s != null && Strings.isNotEmpty(new StringFilter(s).getGroupBy())));
        groupMasterSlaveView.masterVisibleProperty().bind(Properties.combine(groupMasterSlaveView.groupVisibleProperty(),  pm.selectedGroupProperty(),    (groupVisible, selectedGroup)     -> !groupVisible || selectedGroup != null));
        groupMasterSlaveView.slaveVisibleProperty() .bind(Properties.combine(groupMasterSlaveView.masterVisibleProperty(), pm.selectedDocumentProperty(), (masterVisible, selectedDocument) -> masterVisible && selectedDocument != null));

        // Group view data binding
        groupView.groupDisplayResultProperty().bind(pm.groupDisplayResultProperty());
        groupView.selectedGroupProperty().bind(pm.selectedGroupProperty());
        groupView.groupStringFilterProperty().bind(pm.groupStringFilterProperty());
        pm.selectedGroupConditionStringFilterProperty().bind(groupView.selectedGroupConditionStringFilterProperty());
        pm.groupDisplaySelectionProperty().bind(groupView.groupDisplaySelectionProperty());
        groupView.setReferenceResolver(groupFilter.getRootAliasReferenceResolver());

        pm.conditionStringFilterProperty() .bind(Properties.compute(conditionSelector .selectedItemProperty(), Filters::toStringJson));
        pm.groupStringFilterProperty()     .bind(Properties.compute(groupSelector     .selectedItemProperty(), Filters::toStringJson));
        pm.columnsStringFilterProperty()   .bind(Properties.compute(columnsSelector   .selectedItemProperty(), Filters::toStringJson));
        bookingDetailsPanel.selectedDocumentProperty().bind(pm.selectedDocumentProperty());
        bookingDetailsPanel.activeProperty().bind(activeProperty());
        return container;
    }

    @Override
    public void onResume() {
        super.onResume();
        SceneUtil.autoFocusIfEnabled(searchBox);
    }

    private ReactiveExpressionFilter<Document> groupFilter;
    private ReactiveExpressionFilter<Document> masterFilter;

    @Override
    protected void startLogic() {
        // Setting up the group filter that controls the content displayed in the group view
        groupFilter = this.<Document>createReactiveExpressionFilter("{class: 'Document', alias: 'd'}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> "{where:  `event=" + eventId + "`}")
                // Applying the condition and group selected by the user
                .combineIfNotNullOtherwiseForceEmptyResult(pm.conditionStringFilterProperty(), stringFilter -> stringFilter)
                .combineIfNotNullOtherwiseForceEmptyResult(pm.groupStringFilterProperty(), stringFilter -> stringFilter.contains("groupBy") ? stringFilter : "{where: 'false'}")
                // Displaying the result in the group view
                .displayResultInto(pm.groupDisplayResultProperty())
                // Reacting to a group selection
                .setSelectedEntityHandler(pm.groupDisplaySelectionProperty(), pm::setSelectedGroup)
                // Everything set up, let's start now!
                .start();
        // Setting up the master filter that controls the content displayed in the master view
        masterFilter = this.<Document>createReactiveExpressionFilter("{class: 'Document', alias: 'd', orderBy: 'ref desc'}")
                // Always loading the fields required for viewing the booking details
                .combine(BookingDetailsPanel.REQUIRED_FIELDS_STRING_FILTER)
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> "{where:  `event=" + eventId + "`}")
                // Applying the condition and columns selected by the user
                .combineIfNotNullOtherwiseForceEmptyResult(pm.conditionStringFilterProperty(), stringFilter -> stringFilter)
                .combineIfNotNullOtherwiseForceEmptyResult(pm.columnsStringFilterProperty(),   stringFilter -> stringFilter)
                // Also, in case groups are showing and a group is selected, applying the condition associated with that group
                .combineIfNotNull(pm.selectedGroupConditionStringFilterProperty(), s -> s)
                // Applying the user search
                .combineIfNotEmptyTrim(pm.searchTextProperty(), s ->
                        Character.isDigit(s.charAt(0)) ? "{where: `ref = " + s + "`}"
                                : s.contains("@") ? "{where: `lower(person_email) like '%" + s.toLowerCase() + "%'`}"
                                : "{where: `person_abcNames like '" + AbcNames.evaluate(s, true) + "'`}")
                // Limit clause
                .combineIfPositive(pm.limitProperty(), limit -> "{limit: `" + limit + "`}")
                // Colorizing the rows
                .applyDomainModelRowStyle()
                // Displaying the result in the master view
                .displayResultInto(pm.genericDisplayResultProperty())
                // When the result is a singe row, automatically select it
                .autoSelectSingleRow()
                // Reacting the a booking selection
                .setSelectedEntityHandler(pm.genericDisplaySelectionProperty(), pm::setSelectedDocument)
                // Activating server push notification
                .setPush(true)
                // Everything set up, let's start now!
                .start();
    }

    @Override
    protected void refreshDataOnActive() {
        groupFilter .refreshWhenActive();
        masterFilter.refreshWhenActive();
    }
}
