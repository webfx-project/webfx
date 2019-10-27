package mongoose.backend.activities.statistics;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import mongoose.backend.controls.bookingdetailspanel.BookingDetailsPanel;
import mongoose.backend.controls.masterslave.ConventionalReactiveExpressionFilterFactoryMixin;
import mongoose.backend.controls.masterslave.ConventionalUiBuilder;
import mongoose.backend.controls.masterslave.ConventionalUiBuilderMixin;
import mongoose.backend.operations.entities.document.SendLetterRequest;
import mongoose.backend.operations.entities.documentline.DeleteDocumentLineRequest;
import mongoose.backend.operations.entities.documentline.EditDocumentLineRequest;
import mongoose.backend.operations.entities.documentline.ToggleCancelDocumentLineRequest;
import mongoose.backend.operations.entities.generic.CopyAllRequest;
import mongoose.backend.operations.entities.generic.CopySelectionRequest;
import mongoose.client.activity.eventdependent.EventDependentViewDomainActivity;
import mongoose.shared.entities.Attendance;
import mongoose.shared.entities.DocumentLine;
import webfx.framework.client.operation.action.OperationActionFactoryMixin;
import webfx.framework.client.ui.filter.ReactiveExpressionFilter;
import webfx.framework.client.ui.filter.StringFilter;
import webfx.framework.client.ui.layouts.LayoutUtil;
import webfx.extras.visual.controls.grid.VisualGrid;

final class StatisticsActivity extends EventDependentViewDomainActivity implements
        OperationActionFactoryMixin,
        ConventionalUiBuilderMixin,
        ConventionalReactiveExpressionFilterFactoryMixin {

    /*==================================================================================================================
    ================================================= Graphical layer ==================================================
    ==================================================================================================================*/

    private final StatisticsPresentationModel pm = new StatisticsPresentationModel();

    @Override
    public StatisticsPresentationModel getPresentationModel() {
        return pm; // eventId and organizationId will then be updated from route
    }

    @Override
    public Node buildUi() {
        ConventionalUiBuilder ui = createAndBindGroupMasterSlaveViewWithFilterSearchBar(pm, "statistics", "DocumentLine");

        Pane container = ui.buildUi();

        setUpContextMenu(LayoutUtil.lookupChild(ui.getGroupMasterSlaveView().getMasterView(), n -> n instanceof VisualGrid), () -> newActionGroup(
                newOperationAction(() -> new SendLetterRequest(                            pm.getSelectedDocument(), container)),
                newSeparatorActionGroup(
                        newOperationAction(() -> new EditDocumentLineRequest(         pm.getSelectedDocumentLine(), container)),
                        newOperationAction(() -> new ToggleCancelDocumentLineRequest( pm.getSelectedDocumentLine(), container)),
                        newOperationAction(() -> new DeleteDocumentLineRequest(       pm.getSelectedDocumentLine(), container))
                ),
                newSeparatorActionGroup(
                        newOperationAction(() -> new CopySelectionRequest( masterFilter.getSelectedEntities(),  masterFilter.getExpressionColumns())),
                        newOperationAction(() -> new CopyAllRequest(       masterFilter.getCurrentEntityList(), masterFilter.getExpressionColumns()))
                )
        ));

        return container;
    }


    /*==================================================================================================================
    =================================================== Logical layer ==================================================
    ==================================================================================================================*/

    private ReactiveExpressionFilter<DocumentLine> leftGroupFilter, masterFilter;
    private ReactiveExpressionFilter<Attendance> rightAttendanceFilter;

    @Override
    protected void startLogic() {
        // Setting up the left group filter for the left content displayed in the group view
        leftGroupFilter = this.<DocumentLine>createGroupReactiveExpressionFilter(pm, "{class: 'DocumentLine', alias: 'dl'}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> "{where:  `document.event=" + eventId + "`}")
                ;

        // Setting up the right group filter
        rightAttendanceFilter = this.<Attendance>createReactiveExpressionFilter("{class: 'Attendance', alias: 'a', where: 'present', orderBy: 'date'}")
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> "{where:  `documentLine.document.event=" + eventId + "`}")
                // Applying the condition and group selected by the user
                .combineIfNotNullOtherwiseForceEmptyResult(pm.conditionStringFilterProperty(), stringFilter -> {
                    StringFilter gsf = new StringFilter(stringFilter);
                    String where = gsf.getWhere();
                    if (where == null)
                        return "{where: 'false'}";
                    where = "a.[documentLine as dl].(" + where + ')';
                    return "{where: `" + where + "`}";
                })
                .combineIfNotNullOtherwiseForceEmptyResult(pm.groupStringFilterProperty(), stringFilter -> {
                    StringFilter gsf = new StringFilter(stringFilter);
                    String groupBy = gsf.getGroupBy();
                    if (groupBy == null)
                        return "{where: 'false'}";
                    groupBy = "documentLine.(" + groupBy + ')';
                    return "{columns: `" + groupBy + ",date,count(1)`, groupBy: `" + groupBy + ",date`}";
                })
                ;

        // Building the statistics final display result from the 2 above filters
        new StatisticsBuilder(leftGroupFilter, rightAttendanceFilter, pm.groupVisualResultProperty()).start();

        // Setting up the master filter for the content displayed in the master view
        masterFilter = this.<DocumentLine>createMasterReactiveExpressionFilter(pm, "{class: 'DocumentLine', alias: 'dl', orderBy: 'document.ref,item.family.ord,site..ord,item.ord'}")
                // Always loading the fields required for viewing the booking details
                .combine("{fields: `document.(" + BookingDetailsPanel.REQUIRED_FIELDS_STRING_FILTER + ")`}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> "{where:  `document.event=" + eventId + "`}")
                // Colorizing the rows
                .applyDomainModelRowStyle()
                // Everything set up, let's start now!
                .start();

    }

    @Override
    protected void refreshDataOnActive() {
        leftGroupFilter       .refreshWhenActive();
        rightAttendanceFilter .refreshWhenActive();
        masterFilter          .refreshWhenActive();
    }
}
