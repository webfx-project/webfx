package mongoose.backend.activities.bookings;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import mongoose.backend.controls.bookingdetailspanel.BookingDetailsPanel;
import mongoose.backend.controls.masterslave.ConventionalUiBuilder;
import mongoose.backend.controls.masterslave.ConventionalUiBuilderMixin;
import mongoose.backend.operations.entities.document.SendLetterRequest;
import mongoose.backend.operations.entities.document.registration.*;
import mongoose.backend.operations.entities.document.security.ToggleMarkDocumentAsKnownRequest;
import mongoose.backend.operations.entities.document.security.ToggleMarkDocumentAsUncheckedRequest;
import mongoose.backend.operations.entities.document.security.ToggleMarkDocumentAsUnknownRequest;
import mongoose.backend.operations.entities.document.security.ToggleMarkDocumentAsVerifiedRequest;
import mongoose.backend.operations.entities.generic.CopyAllRequest;
import mongoose.backend.operations.entities.generic.CopySelectionRequest;
import mongoose.backend.operations.routes.bookings.RouteToNewBackendBookingRequest;
import mongoose.backend.operations.routes.cloneevent.RouteToCloneEventRequest;
import mongoose.client.activity.eventdependent.EventDependentViewDomainActivity;
import mongoose.shared.domainmodel.functions.AbcNames;
import mongoose.shared.entities.Document;
import webfx.extras.visual.controls.grid.VisualGrid;
import webfx.framework.client.ui.action.operation.OperationActionFactoryMixin;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.ReactiveVisualMapper;
import webfx.framework.client.ui.util.layout.LayoutUtil;

import static webfx.framework.shared.orm.dql.DqlStatement.fields;
import static webfx.framework.shared.orm.dql.DqlStatement.where;
import static webfx.framework.client.ui.util.layout.LayoutUtil.setUnmanagedWhenInvisible;

final class BookingsActivity extends EventDependentViewDomainActivity implements
        OperationActionFactoryMixin,
        ConventionalUiBuilderMixin {

    /*==================================================================================================================
    ================================================= Graphical layer ==================================================
    ==================================================================================================================*/

    private final BookingsPresentationModel pm = new BookingsPresentationModel();

    @Override
    public BookingsPresentationModel getPresentationModel() {
        return pm; // eventId and organizationId will then be updated from route
    }

    private ConventionalUiBuilder ui; // Keeping this reference for activity resume

    @Override
    public Node buildUi() {
        ui = createAndBindGroupMasterSlaveViewWithFilterSearchBar(pm, "bookings", "Document");

        // Adding new booking button on left and clone event on right of the filter search bar
        Button newBookingButton = newButton(newOperationAction(() -> new RouteToNewBackendBookingRequest(getEventId(), getHistory()))),
                cloneEventButton = newButton(newOperationAction(() -> new RouteToCloneEventRequest(getEventId(), getHistory())));
        ui.setLeftTopNodes(setUnmanagedWhenInvisible(newBookingButton));
        ui.setRightTopNodes(setUnmanagedWhenInvisible(cloneEventButton));

        Pane container = ui.buildUi();

        setUpContextMenu(LayoutUtil.lookupChild(ui.getGroupMasterSlaveView().getMasterView(), n -> n instanceof VisualGrid), () -> newActionGroup(
                newOperationAction(() -> new SendLetterRequest(pm.getSelectedDocument(), container)),
                newSeparatorActionGroup("Registration",
                        newOperationAction(() -> new ToggleMarkDocumentAsReadRequest(pm.getSelectedDocument(), container)),
                        newOperationAction(() -> new ToggleMarkDocumentAsWillPayRequest(pm.getSelectedDocument(), container)),
                        newOperationAction(() -> new ToggleCancelDocumentRequest(pm.getSelectedDocument(), container)),
                        newOperationAction(() -> new ToggleConfirmDocumentRequest(pm.getSelectedDocument(), container)),
                        newOperationAction(() -> new ToggleFlagDocumentRequest(pm.getSelectedDocument(), container)),
                        newOperationAction(() -> new ToggleMarkDocumentPassAsReadyRequest(pm.getSelectedDocument(), container)),
                        newOperationAction(() -> new MarkDocumentPassAsUpdatedRequest(pm.getSelectedDocument(), container)),
                        newOperationAction(() -> new ToggleMarkDocumentAsArrivedRequest(pm.getSelectedDocument(), container))
                ),
                newSeparatorActionGroup("Security",
                        newOperationAction(() -> new ToggleMarkDocumentAsUncheckedRequest(pm.getSelectedDocument(), container)),
                        newOperationAction(() -> new ToggleMarkDocumentAsUnknownRequest(pm.getSelectedDocument(), container)),
                        newOperationAction(() -> new ToggleMarkDocumentAsKnownRequest(pm.getSelectedDocument(), container)),
                        newOperationAction(() -> new ToggleMarkDocumentAsVerifiedRequest(pm.getSelectedDocument(), container))
                ),
                newSeparatorActionGroup(
                        newOperationAction(() -> new CopySelectionRequest(masterVisualMapper.getSelectedEntities(), masterVisualMapper.getEntityColumns())),
                        newOperationAction(() -> new CopyAllRequest(masterVisualMapper.getCurrentEntities(), masterVisualMapper.getEntityColumns()))
                )
        ));

        return container;
    }

    @Override
    public void onResume() {
        super.onResume();
        ui.onResume(); // activate search text focus on activity resume
    }


    /*==================================================================================================================
    =================================================== Logical layer ==================================================
    ==================================================================================================================*/

    private ReactiveVisualMapper<Document> groupVisualMapper, masterVisualMapper;

    @Override
    protected void startLogic() {
        // Setting up the group mapper that build the content displayed in the group view
        groupVisualMapper = ReactiveVisualMapper.<Document>createGroupReactiveChain(this, pm)
                .always("{class: 'Document', alias: 'd'}")
                // Applying the event condition
                .ifNotNullOtherwiseEmpty(pm.eventIdProperty(), eventId -> where("event=?", eventId))
                .start();

        // Setting up the master mapper that build the content displayed in the master view
        masterVisualMapper = ReactiveVisualMapper.<Document>createMasterPushReactiveChain(this, pm)
                .always("{class: 'Document', alias: 'd', orderBy: 'ref desc'}")
                // Always loading the fields required for viewing the booking details
                .always(fields(BookingDetailsPanel.REQUIRED_FIELDS))
                // Applying the event condition
                .ifNotNullOtherwiseEmpty(pm.eventIdProperty(), eventId -> where("event=?", eventId))
                // Applying the user search
                .ifTrimNotEmpty(pm.searchTextProperty(), s ->
                        Character.isDigit(s.charAt(0)) ? where("ref = ?", Integer.parseInt(s))
                                : s.contains("@") ? where("lower(person_email) like ?", "%" + s.toLowerCase() + "%")
                                : where("person_abcNames like ?", AbcNames.evaluate(s, true)))
                .applyDomainModelRowStyle() // Colorizing the rows
                .autoSelectSingleRow() // When the result is a singe row, automatically select it
                .start();
    }

    @Override
    protected void refreshDataOnActive() {
        groupVisualMapper.refreshWhenActive();
        masterVisualMapper.refreshWhenActive();
    }
}
