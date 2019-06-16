package mongoose.backend.activities.income;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import mongoose.backend.controls.masterslave.group.GroupView;
import mongoose.client.activity.eventdependent.EventDependentPresentationModel;
import mongoose.client.activity.eventdependent.EventDependentViewDomainActivity;
import mongoose.client.entities.util.FilterButtonSelectorFactoryMixin;
import mongoose.client.entities.util.Filters;
import mongoose.shared.entities.Document;
import mongoose.shared.entities.DocumentLine;
import mongoose.shared.entities.Filter;
import webfx.framework.client.operation.action.OperationActionFactoryMixin;
import webfx.framework.client.ui.controls.button.EntityButtonSelector;
import webfx.framework.client.ui.filter.ReactiveExpressionFilter;
import webfx.framework.client.ui.filter.ReactiveExpressionFilterFactoryMixin;
import webfx.fxkit.extra.controls.displaydata.datagrid.DataGrid;
import webfx.fxkit.util.properties.Properties;

final class IncomeActivity extends EventDependentViewDomainActivity
        implements OperationActionFactoryMixin,
        FilterButtonSelectorFactoryMixin,
        ReactiveExpressionFilterFactoryMixin {

    private final IncomePresentationModel pm = new IncomePresentationModel();

    @Override
    public EventDependentPresentationModel getPresentationModel() {
        return pm; // eventId and organizationId will then be updated from route
    }

    private GroupView<DocumentLine> breakdownGroupView; // keeping reference to avoid GC

    @Override
    public Node buildUi() {
        BorderPane container = new BorderPane();

        DataGrid totalTable = new DataGrid();
        totalTable.setFullHeight(true);
        totalTable.displayResultProperty().bind(pm.genericDisplayResultProperty());

        // Building the top bar
        EntityButtonSelector<Filter> breakdownGroupSelector = createGroupFilterButtonSelector(    "income", "DocumentLine", container);

        breakdownGroupView = new GroupView<>();

        pm.groupStringFilterProperty()     .bind(Properties.compute(breakdownGroupSelector     .selectedItemProperty(), Filters::toStringJson));

        breakdownGroupView.groupDisplayResultProperty().bind(pm.groupDisplayResultProperty());
        breakdownGroupView.groupStringFilterProperty().bind(pm.groupStringFilterProperty());
        pm.selectedGroupConditionStringFilterProperty().bind(breakdownGroupView.selectedGroupConditionStringFilterProperty());
        breakdownGroupView.setReferenceResolver(breakdownFilter.getRootAliasReferenceResolver());

        container.setTop(new VBox(totalTable, breakdownGroupSelector.getButton()));
        container.setCenter(breakdownGroupView.buildUi());
        return container;
    }

    private ReactiveExpressionFilter<Document> totalFilter;
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
        breakdownFilter = this.<DocumentLine>createReactiveExpressionFilter("{class: 'DocumentLine', alias: 'dl'}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> "{where: `document.event=" + eventId + "`}")
                //.combine("{where: '!cancelled'}")
                .combineIfNotNullOtherwiseForceEmptyResult(pm.groupStringFilterProperty(), stringFilter -> stringFilter.contains("groupBy") ? stringFilter : "{where: 'false'}")
                // Displaying the result in the group view
                .displayResultInto(pm.groupDisplayResultProperty())
                // Everything set up, let's start now!
                .start();
    }

    @Override
    protected void refreshDataOnActive() {
        totalFilter.refreshWhenActive();
        breakdownFilter.refreshWhenActive();
    }
}
