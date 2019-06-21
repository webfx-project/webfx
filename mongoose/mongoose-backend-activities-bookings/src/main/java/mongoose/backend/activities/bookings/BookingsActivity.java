package mongoose.backend.activities.bookings;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import mongoose.backend.controls.bookingdetailspanel.BookingDetailsPanel;
import mongoose.backend.controls.masterslave.MasterTableView;
import mongoose.backend.controls.masterslave.group.GroupMasterSlaveView;
import mongoose.backend.controls.masterslave.group.GroupView;
import mongoose.backend.operations.bookings.RouteToNewBackendBookingRequest;
import mongoose.backend.operations.cloneevent.RouteToCloneEventRequest;
import mongoose.client.activity.eventdependent.EventDependentViewDomainActivity;
import mongoose.client.entities.util.filters.FilterButtonSelectorFactoryMixin;
import mongoose.client.entities.util.filters.FilterSearchBar;
import mongoose.shared.domainmodel.functions.AbcNames;
import mongoose.shared.entities.Document;
import webfx.framework.client.operation.action.OperationActionFactoryMixin;
import webfx.framework.client.ui.filter.ReactiveExpressionFilter;
import webfx.framework.client.ui.filter.ReactiveExpressionFilterFactoryMixin;

import static webfx.framework.client.ui.layouts.LayoutUtil.setHGrowable;
import static webfx.framework.client.ui.layouts.LayoutUtil.setUnmanagedWhenInvisible;

final class BookingsActivity extends EventDependentViewDomainActivity implements
        OperationActionFactoryMixin,
        FilterButtonSelectorFactoryMixin,
        ReactiveExpressionFilterFactoryMixin {

    /*==================================================================================================================
    ================================================= Graphical layer ==================================================
    ==================================================================================================================*/

    private final BookingsPresentationModel pm = new BookingsPresentationModel();

    @Override
    public BookingsPresentationModel getPresentationModel() {
        return pm; // eventId and organizationId will then be updated from route
    }

    private FilterSearchBar filterSearchBar; // Keeping this reference for activity resume

    @Override
    public Node buildUi() {
        BorderPane container = new BorderPane();
        // Building the top bar
        Button newBookingButton = newButton(newAction(() -> new RouteToNewBackendBookingRequest(getEventId(), getHistory()))),
               cloneEventButton = newButton(newAction(() -> new RouteToCloneEventRequest(getEventId(), getHistory())));
        filterSearchBar = createFilterSearchBar("bookings", "Document", container, pm);
        container.setTop(new HBox(10,
                setUnmanagedWhenInvisible(newBookingButton),
                setHGrowable(filterSearchBar.buildUi()),
                setUnmanagedWhenInvisible(cloneEventButton)));

        container.setCenter(
                GroupMasterSlaveView.createAndBind(Orientation.VERTICAL,
                        GroupView.createAndBind(pm).setReferenceResolver(groupFilter.getRootAliasReferenceResolver()),
                        MasterTableView.createAndBind(this, pm).buildUi(),
                        BookingDetailsPanel.createAndBind(container,this, pm).buildUi(),
                        pm.selectedDocumentProperty()
                ).buildUi());

        return container;
    }

    @Override
    public void onResume() {
        super.onResume();
        filterSearchBar.onResume(); // activate search text focus on activity resume
    }


    /*==================================================================================================================
    =================================================== Logical layer ==================================================
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
                .displayResultInto(pm.masterDisplayResultProperty())
                // When the result is a singe row, automatically select it
                .autoSelectSingleRow()
                // Reacting the a booking selection
                .setSelectedEntityHandler(pm.masterDisplaySelectionProperty(), pm::setSelectedMaster)
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
