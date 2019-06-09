package mongoose.backend.activities.bookings;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import mongoose.backend.operations.bookings.RouteToNewBackendBookingRequest;
import mongoose.backend.operations.cloneevent.RouteToCloneEventRequest;
import mongoose.client.activity.eventdependent.EventDependentPresentationModel;
import mongoose.client.activity.eventdependent.EventDependentViewDomainActivity;
import mongoose.client.activity.table.GenericTableView;
import mongoose.client.activity.table.GroupMasterSlaveView;
import mongoose.client.entities.util.Filters;
import mongoose.shared.domainmodel.functions.AbcNames;
import mongoose.shared.entities.Document;
import mongoose.shared.entities.Filter;
import webfx.framework.client.operation.action.OperationActionFactoryMixin;
import webfx.framework.client.ui.controls.button.EntityButtonSelector;
import webfx.framework.client.ui.filter.ReactiveExpressionFilter;
import webfx.framework.client.ui.filter.ReactiveExpressionFilterFactoryMixin;
import webfx.framework.client.ui.filter.StringFilter;
import webfx.fxkit.util.properties.Properties;
import webfx.platform.shared.util.Strings;

import java.util.function.Predicate;

import static webfx.framework.client.ui.layouts.LayoutUtil.*;

final class BookingsActivity extends EventDependentViewDomainActivity
        implements OperationActionFactoryMixin,
        ReactiveExpressionFilterFactoryMixin {

    private GenericTableView genericTableView;

    private final BookingsPresentationModel pm = new BookingsPresentationModel();

    @Override
    public EventDependentPresentationModel getPresentationModel() {
        return pm; // eventId and organizationId will then be updated from route
    }

    private final static String FILTER_SELECTOR_TEMPLATE = "{class: 'Filter', fields: 'class,alias,fields,whereClause,groupByClause,havingClause,orderByClause,limitClause,columns', where: `class='Document' and ${condition}`, orderBy: 'id'}";

    private EntityButtonSelector<Filter> createFilterButtonSelector(String conditionToken, Predicate<Filter> autoSelectPredicate, Pane parent) {
        EntityButtonSelector<Filter> selector = new EntityButtonSelector<>(FILTER_SELECTOR_TEMPLATE.replace("${condition}", conditionToken), this, parent, getDataSourceModel());
        selector.autoSelectFirstEntity(autoSelectPredicate);
        selector.setAutoOpenOnMouseEntered(true);
        return selector;
    }

    @Override
    public Node buildUi() {
        Button newBookingButton = newButton(newAction(() -> new RouteToNewBackendBookingRequest(getEventId(), getHistory())));
        Button cloneEventButton = newButton(newAction(() -> new RouteToCloneEventRequest(getEventId(), getHistory())));
        BorderPane container = new BorderPane();
        EntityButtonSelector<Filter> conditionSelector = createFilterButtonSelector("isCondition", filter -> "!cancelled".equals(filter.getWhereClause()), container);
        EntityButtonSelector<Filter> groupSelector     = createFilterButtonSelector("isGroup",     filter -> "".equals(filter.getName()), container);
        EntityButtonSelector<Filter> columnsSelector   = createFilterButtonSelector("isColumns",   filter -> "prices".equals(filter.getName()), container);
        GroupView groupView = new GroupView();
        BookingDetailsPanel bookingDetailsPanel = new BookingDetailsPanel((Pane) getNode(), this, getDataSourceModel());
        GroupMasterSlaveView groupMasterSlaveView = new GroupMasterSlaveView(Orientation.VERTICAL, groupView.buildUi(), null, bookingDetailsPanel.buildUi());
        genericTableView = new GenericTableView(groupMasterSlaveView) {
            @Override
            public void initUi() {
                super.initUi();
                borderPane.setTop(null);

                // Initialization from the presentation model current state
                searchBox.setText(pm.searchTextProperty().getValue());

                // Binding the UI with the presentation model for further state changes
                // User inputs: the UI state changes are transferred in the presentation model
                pm.searchTextProperty().bind(searchBox.textProperty());
                Properties.runNowAndOnPropertiesChange(() -> pm.limitProperty().setValue(limitCheckBox.isSelected() ? 30 : -1), limitCheckBox.selectedProperty());
                table.fullHeightProperty().bind(limitCheckBox.selectedProperty());
                //pm.limitProperty().bind(limitCheckBox.selectedProperty());
                pm.genericDisplaySelectionProperty().bind(table.displaySelectionProperty());
                // User outputs: the presentation model changes are transferred in the UI
                table.displayResultProperty().bind(pm.genericDisplayResultProperty());
            }
        };
        container.setCenter(genericTableView.buildUi());
        container.setTop(new HBox(10, setUnmanagedWhenInvisible(newBookingButton), conditionSelector.getButton(), groupSelector.getButton(), columnsSelector.getButton(), setMaxHeightToInfinite(setHGrowable(genericTableView.getSearchBox())), setUnmanagedWhenInvisible(cloneEventButton)));
        groupMasterSlaveView.groupVisibleProperty() .bind(Properties.compute(pm.groupStringFilterProperty(), s -> s != null && Strings.isNotEmpty(new StringFilter(s).getGroupBy())));
        groupMasterSlaveView.masterVisibleProperty().bind(Properties.combine(groupMasterSlaveView.groupVisibleProperty(),  pm.selectedGroupProperty(),    (groupVisible, selectedGroup)     -> !groupVisible || selectedGroup != null));
        groupMasterSlaveView.slaveVisibleProperty() .bind(Properties.combine(groupMasterSlaveView.masterVisibleProperty(), pm.selectedDocumentProperty(), (masterVisible, selectedDocument) -> masterVisible && selectedDocument != null));

        // Group view data binding
        groupView.groupDisplayResultProperty().bind(pm.groupDisplayResultProperty());
        groupView.visibleProperty().bind(groupMasterSlaveView.groupVisibleProperty());
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
        genericTableView.onResume();
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
                .combineIfNotNullOtherwiseForceEmptyResult(pm.groupStringFilterProperty(), stringFilter -> stringFilter)
                // Displaying the result in the group view
                .displayResultInto(pm.groupDisplayResultProperty())
                // Reacting to a group selection
                .setSelectedEntityHandler(pm.groupDisplaySelectionProperty(), pm::setSelectedGroup)
                // Everything set up, let's start now!
                .start();

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
