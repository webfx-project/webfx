package mongoose.backend.activities.statistics;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import mongoose.backend.controls.bookingdetailspanel.BookingDetailsPanel;
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
import webfx.extras.visual.controls.grid.VisualGrid;
import webfx.framework.client.ui.action.operation.OperationActionFactoryMixin;
import webfx.framework.shared.orm.dql.DqlClause;
import webfx.framework.shared.orm.dql.DqlStatement;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.ReactiveVisualMapper;
import webfx.framework.client.ui.util.layout.LayoutUtil;

import static webfx.framework.shared.orm.dql.DqlStatement.where;

final class StatisticsActivity extends EventDependentViewDomainActivity implements
        OperationActionFactoryMixin,
        ConventionalUiBuilderMixin {

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
                newOperationAction(() -> new SendLetterRequest(pm.getSelectedDocument(), container)),
                newSeparatorActionGroup(
                        newOperationAction(() -> new EditDocumentLineRequest(pm.getSelectedDocumentLine(), container)),
                        newOperationAction(() -> new ToggleCancelDocumentLineRequest(pm.getSelectedDocumentLine(), container)),
                        newOperationAction(() -> new DeleteDocumentLineRequest(pm.getSelectedDocumentLine(), container))
                ),
                newSeparatorActionGroup(
                        newOperationAction(() -> new CopySelectionRequest(masterVisualMapper.getSelectedEntities(), masterVisualMapper.getEntityColumns())),
                        newOperationAction(() -> new CopyAllRequest(masterVisualMapper.getCurrentEntities(), masterVisualMapper.getEntityColumns()))
                )
        ));

        return container;
    }


    /*==================================================================================================================
    =================================================== Logical layer ==================================================
    ==================================================================================================================*/

    private ReactiveVisualMapper<DocumentLine> leftGroupVisualMapper, masterVisualMapper;
    private ReactiveVisualMapper<Attendance> rightAttendanceVisualMapper;
    private StatisticsBuilder statisticsBuilder; // to avoid GC

    @Override
    protected void startLogic() {
        // Setting up the left group filter for the left content displayed in the group view
        leftGroupVisualMapper = ReactiveVisualMapper.<DocumentLine>createGroupReactiveChain(this, pm)
                .always("{class: 'DocumentLine', alias: 'dl'}")
                // Applying the event condition
                .ifNotNullOtherwiseEmpty(pm.eventIdProperty(), eventId -> where("document.event=?", eventId))
        ;

        rightAttendanceVisualMapper = ReactiveVisualMapper.<Attendance>createReactiveChain(this)
                .always("{class: 'Attendance', alias: 'a', where: 'present', orderBy: 'date'}")
                .ifNotNullOtherwiseEmpty(pm.eventIdProperty(), eventId -> where("documentLine.document.event=?", eventId))
                // Applying the condition and group selected by the user
                .ifNotNullOtherwiseEmpty(pm.conditionDqlStatementProperty(), conditionDqlStatement -> {
                    DqlClause where = conditionDqlStatement.getWhere();
                    if (where == null)
                        return DqlStatement.EMPTY_STATEMENT;
                    return where("a.[documentLine as dl].(" + where.getDql() + ')', where.getParameterValues());
                })
                .ifNotNullOtherwiseEmpty(pm.groupDqlStatementProperty(), groupDqlStatement -> {
                    DqlClause groupBy = groupDqlStatement.getGroupBy();
                    if (groupBy == null)
                        return DqlStatement.EMPTY_STATEMENT;
                    String dqlGroupBy = "documentLine.(" + groupBy.getDql() + ')';
                    return DqlStatement.parse("{columns: `" + dqlGroupBy + ",date,count(1)`, groupBy: `" + dqlGroupBy + ",date`}");
                })
        ;

        // Building the statistics final display result from the 2 above filters
        statisticsBuilder = new StatisticsBuilder(leftGroupVisualMapper, rightAttendanceVisualMapper, leftGroupVisualMapper.visualResultProperty()).start();

        // Setting up the master filter for the content displayed in the master view
        masterVisualMapper = ReactiveVisualMapper.<DocumentLine>createMasterReactiveChain(this, pm)
                .always("{class: 'DocumentLine', alias: 'dl', orderBy: 'document.ref,item.family.ord,site..ord,item.ord'}")
                // Always loading the fields required for viewing the booking details
                .always("{fields: `document.(" + BookingDetailsPanel.REQUIRED_FIELDS + ")`}")
                // Applying the event condition
                .ifNotNullOtherwiseEmpty(pm.eventIdProperty(), eventId -> where("document.event=?", eventId))
                .applyDomainModelRowStyle() // Colorizing the rows
                .start();
    }

    @Override
    protected void refreshDataOnActive() {
        leftGroupVisualMapper.refreshWhenActive();
        rightAttendanceVisualMapper.refreshWhenActive();
        masterVisualMapper.refreshWhenActive();
    }
}
