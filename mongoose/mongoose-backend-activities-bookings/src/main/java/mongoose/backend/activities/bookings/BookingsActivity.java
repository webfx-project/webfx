package mongoose.backend.activities.bookings;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import mongoose.backend.controls.bookingdetailspanel.BookingDetailsPanel;
import mongoose.backend.controls.masterslave.group.GroupMasterSlaveView;
import mongoose.backend.controls.masterslave.group.GroupView;
import mongoose.backend.operations.bookings.RouteToNewBackendBookingRequest;
import mongoose.backend.operations.cloneevent.RouteToCloneEventRequest;
import mongoose.client.activity.eventdependent.EventDependentViewDomainActivity;
import mongoose.client.entities.util.filters.FilterButtonSelectorFactoryMixin;
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

    /*==================================================================================================================
    ===================================================== UI layer =====================================================
    ==================================================================================================================*/

    private final BookingsPresentationModel pm = new BookingsPresentationModel();

    @Override
    public BookingsPresentationModel getPresentationModel() {
        return pm; // eventId and organizationId will then be updated from route
    }

    private TextField searchBox; // Keeping this reference to activate focus on activity resume

    @Override
    public Node buildUi() {
        BorderPane container = new BorderPane();
        // Building the top bar
        Button newBookingButton = newButton(newAction(() -> new RouteToNewBackendBookingRequest(getEventId(), getHistory()))),
               cloneEventButton = newButton(newAction(() -> new RouteToCloneEventRequest(getEventId(), getHistory())));
        EntityButtonSelector<Filter> conditionSelector = createConditionFilterButtonSelectorAndBind("bookings","Document", container, pm),
                                         groupSelector = createGroupFilterButtonSelectorAndBind(    "bookings","Document", container, pm),
                                       columnsSelector = createColumnsFilterButtonSelectorAndBind(  "bookings","Document", container, pm);
        searchBox = newTextFieldWithPrompt("GenericSearchPlaceholder");
        pm.searchTextProperty().bind(searchBox.textProperty());
        container.setTop(new HBox(10, setUnmanagedWhenInvisible(newBookingButton), conditionSelector.getButton(), groupSelector.getButton(), columnsSelector.getButton(), setMaxHeightToInfinite(setHGrowable(searchBox)), setUnmanagedWhenInvisible(cloneEventButton)));

        // Building the main content, which is a group/master/slave view (group = group view, master = bookings table + limit checkbox, slave = booking details)
        DataGrid masterTable = new DataGrid();
        CheckBox masterLimitCheckBox = newCheckBox("LimitTo100");
        masterLimitCheckBox.setSelected(true);
        BorderPane masterPane = new BorderPane(masterTable, null, null, masterLimitCheckBox, null);
        BorderPane.setAlignment(masterTable, Pos.TOP_CENTER);

        GroupView<Document> groupView = GroupView.createAndBind(pm);
        groupView.setReferenceResolver(groupFilter.getRootAliasReferenceResolver());
        BookingDetailsPanel bookingDetailsPanel = new BookingDetailsPanel(container, this, getDataSourceModel());

        GroupMasterSlaveView groupMasterSlaveView = new GroupMasterSlaveView(Orientation.VERTICAL,
                groupView.buildUi(),
                masterPane,
                bookingDetailsPanel.buildUi());
        container.setCenter(groupMasterSlaveView.getSplitPane());

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        Properties.runNowAndOnPropertiesChange(() -> pm.limitProperty().setValue(masterLimitCheckBox.isSelected() ? 30 : -1), masterLimitCheckBox.selectedProperty());
        masterTable.fullHeightProperty().bind(masterLimitCheckBox.selectedProperty());
        pm.genericDisplaySelectionProperty().bindBidirectional(masterTable.displaySelectionProperty());
        // User outputs: the presentation model changes are transferred in the UI
        masterTable.displayResultProperty().bind(pm.genericDisplayResultProperty());

        groupMasterSlaveView.groupVisibleProperty() .bind(Properties.compute(pm.groupStringFilterProperty(), s -> s != null && Strings.isNotEmpty(new StringFilter(s).getGroupBy())));
        groupMasterSlaveView.masterVisibleProperty().bind(Properties.combine(groupMasterSlaveView.groupVisibleProperty(),  pm.selectedGroupProperty(),    (groupVisible, selectedGroup)     -> !groupVisible || selectedGroup != null));
        groupMasterSlaveView.slaveVisibleProperty() .bind(Properties.combine(groupMasterSlaveView.masterVisibleProperty(), pm.selectedDocumentProperty(), (masterVisible, selectedDocument) -> masterVisible && selectedDocument != null));

        bookingDetailsPanel.selectedDocumentProperty().bind(pm.selectedDocumentProperty());
        bookingDetailsPanel.activeProperty().bind(activeProperty());
        return container;
    }

    @Override
    public void onResume() {
        super.onResume();
        SceneUtil.autoFocusIfEnabled(searchBox);
    }


    /*==================================================================================================================
    ==================================================== Logic layer ===================================================
    ==================================================================================================================*/

    private ReactiveExpressionFilter<Document> groupFilter, masterFilter;

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
                .combine("{fields: `" + BookingDetailsPanel.REQUIRED_FIELDS_STRING_FILTER + "`}")
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
