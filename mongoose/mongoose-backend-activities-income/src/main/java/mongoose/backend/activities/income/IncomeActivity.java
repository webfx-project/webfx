package mongoose.backend.activities.income;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import mongoose.backend.controls.masterslave.ConventionalReactiveExpressionFilterFactoryMixin;
import mongoose.backend.controls.masterslave.group.GroupView;
import mongoose.client.activity.eventdependent.EventDependentViewDomainActivity;
import mongoose.client.entities.util.filters.FilterButtonSelectorFactoryMixin;
import mongoose.shared.entities.Document;
import mongoose.shared.entities.DocumentLine;
import mongoose.shared.entities.Filter;
import webfx.framework.client.operation.action.OperationActionFactoryMixin;
import webfx.framework.client.ui.controls.button.EntityButtonSelector;
import webfx.framework.client.ui.filter.ReactiveExpressionFilter;
import webfx.fxkit.extra.controls.displaydata.datagrid.DataGrid;

final class IncomeActivity extends EventDependentViewDomainActivity implements
        OperationActionFactoryMixin,
        FilterButtonSelectorFactoryMixin,
        ConventionalReactiveExpressionFilterFactoryMixin {

    /*==================================================================================================================
    ================================================= Graphical layer ==================================================
    ==================================================================================================================*/

    private final IncomePresentationModel pm = new IncomePresentationModel();

    @Override
    public IncomePresentationModel getPresentationModel() {
        return pm; // eventId and organizationId will then be updated from route
    }

    @Override
    public Node buildUi() {
        BorderPane container = new BorderPane();

        // Creating the total table that will be on top of the container
        DataGrid totalTable = new DataGrid();
        totalTable.setFullHeight(true);
        totalTable.displayResultProperty().bind(pm.genericDisplayResultProperty());

        // Also putting the breakdown group selector just below the total table (also on top of the container)
        EntityButtonSelector<Filter> breakdownGroupSelector = createGroupFilterButtonSelectorAndBind("income","DocumentLine", container, pm);

        container.setTop(new VBox(totalTable, breakdownGroupSelector.getButton()));

        // Creating the breakdown group view and put it in the center of the container
        container.setCenter(GroupView.createAndBind(pm).buildUi());

        return container;
    }


    /*==================================================================================================================
    =================================================== Logical layer ==================================================
    ==================================================================================================================*/

    private ReactiveExpressionFilter<Document>     totalFilter;
    private ReactiveExpressionFilter<DocumentLine> breakdownFilter;

    @Override
    protected void startLogic() {
        totalFilter = this.<Document>createReactiveExpressionFilter("{class: 'Document', alias: 'd'}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> "{where: `event=" + eventId + "`}")
                .combine("{columns: `null as Totals,sum(price_deposit) as Deposit,sum(price_net) as Invoiced,sum(price_minDeposit) as MinDeposit,sum(price_nonRefundable) as NonRefundable,sum(price_balance) as Balance,count(1) as Bookings,sum(price_balance!=0 ? 1 : 0) as Unreconciled`, groupBy: `event`}")
                .displayResultInto(pm.genericDisplayResultProperty())
                .start();

        // Setting up the left group filter for the left content displayed in the group view
        breakdownFilter = this.<DocumentLine>createGroupReactiveExpressionFilter(pm,"{class: 'DocumentLine', alias: 'dl'}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> "{where: `document.event=" + eventId + "`}")
                // Everything set up, let's start now!
                .start();
    }

    @Override
    protected void refreshDataOnActive() {
        totalFilter    .refreshWhenActive();
        breakdownFilter.refreshWhenActive();
    }
}
