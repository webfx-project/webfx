package mongoose.backend.activities.income;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import mongoose.backend.controls.masterslave.group.GroupView;
import mongoose.client.activity.eventdependent.EventDependentViewDomainActivity;
import mongoose.client.entities.util.filters.FilterButtonSelectorFactoryMixin;
import mongoose.shared.entities.Document;
import mongoose.shared.entities.DocumentLine;
import mongoose.shared.entities.Filter;
import webfx.extras.visual.controls.grid.VisualGrid;
import webfx.framework.client.ui.action.operation.OperationActionFactoryMixin;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.ReactiveVisualMapper;
import webfx.framework.client.ui.controls.entity.selector.EntityButtonSelector;

import static webfx.framework.shared.orm.dql.DqlStatement.where;

final class IncomeActivity extends EventDependentViewDomainActivity implements
        OperationActionFactoryMixin,
        FilterButtonSelectorFactoryMixin {

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
        VisualGrid totalTable = new VisualGrid();
        totalTable.setFullHeight(true);
        totalTable.visualResultProperty().bind(pm.genericVisualResultProperty());

        // Also putting the breakdown group selector just below the total table (also on top of the container)
        EntityButtonSelector<Filter> breakdownGroupSelector = createGroupFilterButtonSelectorAndBind("income", "DocumentLine", container, pm);

        container.setTop(new VBox(totalTable, breakdownGroupSelector.getButton()));

        // Creating the breakdown group view and put it in the center of the container
        container.setCenter(GroupView.createAndBind(pm).buildUi());

        return container;
    }


    /*==================================================================================================================
    =================================================== Logical layer ==================================================
    ==================================================================================================================*/

    private ReactiveVisualMapper<Document> totalVisualMapper;
    private ReactiveVisualMapper<DocumentLine> breakdownVisualMapper;

    @Override
    protected void startLogic() {
        totalVisualMapper = ReactiveVisualMapper.<Document>createReactiveChain(this)
                .always("{class: 'Document', alias: 'd'}")
                // Applying the event condition
                .ifNotNullOtherwiseEmpty(pm.eventIdProperty(), eventId -> where("event=?", eventId))
                .always("{columns: `null as Totals,sum(price_deposit) as Deposit,sum(price_net) as Invoiced,sum(price_minDeposit) as MinDeposit,sum(price_nonRefundable) as NonRefundable,sum(price_balance) as Balance,count(1) as Bookings,sum(price_balance!=0 ? 1 : 0) as Unreconciled`, groupBy: `event`}")
                .visualizeResultInto(pm.genericVisualResultProperty())
                .start();

        breakdownVisualMapper = ReactiveVisualMapper.<DocumentLine>createGroupReactiveChain(this, pm)
                .always("{class: 'DocumentLine', alias: 'dl'}")
                // Applying the event condition
                .ifNotNullOtherwiseEmpty(pm.eventIdProperty(), eventId -> where("document.event=?", eventId))
                .start();
    }

    @Override
    protected void refreshDataOnActive() {
        totalVisualMapper.refreshWhenActive();
        breakdownVisualMapper.refreshWhenActive();
    }
}
