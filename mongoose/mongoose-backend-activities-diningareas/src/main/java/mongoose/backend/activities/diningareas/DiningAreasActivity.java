package mongoose.backend.activities.diningareas;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import mongoose.backend.activities.statistics.StatisticsBuilder;
import mongoose.backend.controls.masterslave.ConventionalReactiveExpressionFilterFactoryMixin;
import mongoose.backend.controls.masterslave.ConventionalUiBuilderMixin;
import mongoose.backend.operations.entities.allocationrule.AddNewAllocationRuleRequest;
import mongoose.backend.operations.entities.allocationrule.DeleteAllocationRuleRequest;
import mongoose.backend.operations.entities.allocationrule.EditAllocationRuleRequest;
import mongoose.backend.operations.entities.allocationrule.TriggerAllocationRuleRequest;
import mongoose.backend.operations.entities.generic.CopyAllRequest;
import mongoose.backend.operations.entities.generic.CopySelectionRequest;
import mongoose.client.activity.eventdependent.EventDependentViewDomainActivity;
import mongoose.shared.entities.Attendance;
import mongoose.shared.entities.DocumentLine;
import webfx.framework.client.operation.action.OperationActionFactoryMixin;
import webfx.framework.client.ui.filter.ReactiveExpressionFilter;
import webfx.framework.shared.orm.entity.Entity;
import webfx.fxkit.extra.controls.displaydata.datagrid.DataGrid;

final class DiningAreasActivity extends EventDependentViewDomainActivity implements
        OperationActionFactoryMixin,
        ConventionalUiBuilderMixin,
        ConventionalReactiveExpressionFilterFactoryMixin {

    /*==================================================================================================================
    ================================================= Graphical layer ==================================================
    ==================================================================================================================*/

    private final DiningAreasPresentationModel pm = new DiningAreasPresentationModel();

    @Override
    public DiningAreasPresentationModel getPresentationModel() {
        return pm; // eventId and organizationId will then be updated from route
    }

    @Override
    public Node buildUi() {
        DataGrid sittingTable = new DataGrid();
        sittingTable.displayResultProperty().bind(pm.sittingDisplayResultProperty());
        DataGrid rulesTable = new DataGrid();
        rulesTable.displayResultProperty().bind(pm.rulesDisplayResultProperty());
        rulesTable.displaySelectionProperty().bindBidirectional(pm.rulesDisplaySelectionProperty());
        SplitPane splitPane = new SplitPane(sittingTable, rulesTable);
        splitPane.setOrientation(Orientation.VERTICAL);
        Pane container = new StackPane(splitPane);
        setUpContextMenu(rulesTable, () -> newActionGroup(
                newSeparatorActionGroup(
                        newAction(() -> new AddNewAllocationRuleRequest( getEvent(), container))
                ),
                newSeparatorActionGroup(
                        newAction(() -> new TriggerAllocationRuleRequest( rulesFilter.getSelectedEntity(), container))
                ),
                newSeparatorActionGroup(
                        newAction(() -> new EditAllocationRuleRequest(   rulesFilter.getSelectedEntity(), container)),
                        newAction(() -> new DeleteAllocationRuleRequest( rulesFilter.getSelectedEntity(), container))
                ),
                newSeparatorActionGroup(
                        newAction(() -> new CopySelectionRequest( rulesFilter.getSelectedEntities(),  rulesFilter.getExpressionColumns())),
                        newAction(() -> new CopyAllRequest(       rulesFilter.getCurrentEntityList(), rulesFilter.getExpressionColumns()))
                )));
        return container;
    }


    /*==================================================================================================================
    =================================================== Logical layer ==================================================
    ==================================================================================================================*/

    private ReactiveExpressionFilter<DocumentLine> leftSittingFilter;
    private ReactiveExpressionFilter<Attendance> rightAttendanceFilter;
    private ReactiveExpressionFilter<Entity> rulesFilter;

    @Override
    protected void startLogic() {
        // Setting up the group filter that controls the content displayed in the group view
        leftSittingFilter = this.<DocumentLine>createReactiveExpressionFilter("{class: 'DocumentLine', alias: 'dl', columns: 'resourceConfiguration,count(1)', where: `!cancelled and item.family.code='meals'`, groupBy: 'resourceConfiguration', orderBy: 'resourceConfiguration..name'}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> "{where:  `document.event=" + eventId + "`}");

        // Setting up the right group filter
        rightAttendanceFilter = this.<Attendance>createReactiveExpressionFilter("{class: 'Attendance', alias: 'a', columns: `documentLine.resourceConfiguration,date,count(1)`, where: `present and documentLine.(!cancelled and item.family.code='meals')`, groupBy: 'documentLine.resourceConfiguration,date', orderBy: 'date'}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> "{where:  `documentLine.document.event=" + eventId + "`}");

        // Building the statistics final display result from the 2 above filters
        new StatisticsBuilder(leftSittingFilter, rightAttendanceFilter, pm.sittingDisplayResultProperty()).start();

        // Setting up the master filter that controls the content displayed in the master view
        rulesFilter = this.createReactiveExpressionFilter("{class: 'AllocationRule', alias: 'ar', columns: '<default>', orderBy: 'ord,id'}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> "{where:  `event=" + eventId + "`}")
                // Displaying the result into the rules table through the presentation model
                .displayResultInto(pm.rulesDisplayResultProperty())
                .setDisplaySelectionProperty(pm.rulesDisplaySelectionProperty())
                // Colorizing the rows
                .applyDomainModelRowStyle()
                // Activating server push notification
                .setPush(true)
                // Everything set up, let's start now!
                .start();
    }

    @Override
    protected void refreshDataOnActive() {
        leftSittingFilter.refreshWhenActive();
        rightAttendanceFilter.refreshWhenActive();
        rulesFilter.refreshWhenActive();
    }

}
