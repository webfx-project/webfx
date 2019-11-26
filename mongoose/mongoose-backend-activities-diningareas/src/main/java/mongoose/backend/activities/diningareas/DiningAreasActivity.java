package mongoose.backend.activities.diningareas;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import mongoose.backend.activities.statistics.StatisticsBuilder;
import mongoose.backend.controls.masterslave.ConventionalReactiveVisualFilterFactoryMixin;
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
import webfx.extras.visual.controls.grid.VisualGrid;
import webfx.framework.client.operation.action.OperationActionFactoryMixin;
import webfx.framework.client.orm.entity.filter.visual.ReactiveVisualFilter;
import webfx.framework.shared.orm.entity.Entity;

import static webfx.framework.client.orm.entity.filter.DqlStatement.where;

final class DiningAreasActivity extends EventDependentViewDomainActivity implements
        OperationActionFactoryMixin,
        ConventionalUiBuilderMixin,
        ConventionalReactiveVisualFilterFactoryMixin {

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
        VisualGrid sittingTable = new VisualGrid();
        sittingTable.visualResultProperty().bind(pm.sittingVisualResultProperty());
        VisualGrid rulesTable = new VisualGrid();
        rulesTable.visualResultProperty().bind(pm.rulesVisualResultProperty());
        rulesTable.visualSelectionProperty().bindBidirectional(pm.rulesVisualSelectionProperty());
        SplitPane splitPane = new SplitPane(sittingTable, rulesTable);
        splitPane.setOrientation(Orientation.VERTICAL);
        Pane container = new StackPane(splitPane);
        setUpContextMenu(rulesTable, () -> newActionGroup(
                newSeparatorActionGroup(
                        newOperationAction(() -> new AddNewAllocationRuleRequest( getEvent(), container))
                ),
                newSeparatorActionGroup(
                        newOperationAction(() -> new TriggerAllocationRuleRequest( rulesFilter.getSelectedEntity(), container))
                ),
                newSeparatorActionGroup(
                        newOperationAction(() -> new EditAllocationRuleRequest(   rulesFilter.getSelectedEntity(), container)),
                        newOperationAction(() -> new DeleteAllocationRuleRequest( rulesFilter.getSelectedEntity(), container))
                ),
                newSeparatorActionGroup(
                        newOperationAction(() -> new CopySelectionRequest( rulesFilter.getSelectedEntities(),  rulesFilter.getEntityColumns())),
                        newOperationAction(() -> new CopyAllRequest(       rulesFilter.getCurrentEntityList(), rulesFilter.getEntityColumns()))
                )));
        return container;
    }


    /*==================================================================================================================
    =================================================== Logical layer ==================================================
    ==================================================================================================================*/

    private ReactiveVisualFilter<DocumentLine> leftSittingFilter;
    private ReactiveVisualFilter<Attendance> rightAttendanceFilter;
    private ReactiveVisualFilter<Entity> rulesFilter;

    @Override
    protected void startLogic() {
        // Setting up the group filter that controls the content displayed in the group view
        leftSittingFilter = this.<DocumentLine>createReactiveVisualFilter("{class: 'DocumentLine', alias: 'dl', columns: 'resourceConfiguration,count(1)', where: `!cancelled and item.family.code='meals'`, groupBy: 'resourceConfiguration', orderBy: 'resourceConfiguration..name'}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> where("document.event=?", eventId));

        // Setting up the right group filter
        rightAttendanceFilter = this.<Attendance>createReactiveVisualFilter("{class: 'Attendance', alias: 'a', columns: `documentLine.resourceConfiguration,date,count(1)`, where: `present and documentLine.(!cancelled and item.family.code='meals')`, groupBy: 'documentLine.resourceConfiguration,date', orderBy: 'date'}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> where("documentLine.document.event=?", eventId));

        // Building the statistics final display result from the 2 above filters
        new StatisticsBuilder(leftSittingFilter, rightAttendanceFilter, pm.sittingVisualResultProperty()).start();

        // Setting up the master filter that controls the content displayed in the master view
        rulesFilter = this.createReactiveVisualFilter("{class: 'AllocationRule', alias: 'ar', columns: '<default>', orderBy: 'ord,id'}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> where("event=?", eventId))
                // Displaying the result into the rules table through the presentation model
                .visualizeResultInto(pm.rulesVisualResultProperty())
                .setVisualSelectionProperty(pm.rulesVisualSelectionProperty())
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
