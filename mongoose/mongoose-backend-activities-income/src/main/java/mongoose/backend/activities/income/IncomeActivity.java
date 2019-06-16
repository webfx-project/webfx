package mongoose.backend.activities.income;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import mongoose.backend.controls.masterslave.group.GroupView;
import mongoose.client.activity.eventdependent.EventDependentPresentationModel;
import mongoose.client.activity.eventdependent.EventDependentViewDomainActivity;
import mongoose.client.entities.util.FilterButtonSelectorFactoryMixin;
import mongoose.client.entities.util.Filters;
import mongoose.shared.entities.DocumentLine;
import mongoose.shared.entities.Filter;
import webfx.framework.client.operation.action.OperationActionFactoryMixin;
import webfx.framework.client.ui.controls.button.EntityButtonSelector;
import webfx.framework.client.ui.filter.ReactiveExpressionFilter;
import webfx.framework.client.ui.filter.ReactiveExpressionFilterFactoryMixin;
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

    private GroupView<DocumentLine> groupView; // keeping reference to avoid GC

    @Override
    public Node buildUi() {
        BorderPane container = new BorderPane();

        // Building the top bar
        EntityButtonSelector<Filter> conditionSelector = createConditionFilterButtonSelector("income", "DocumentLine", container),
                                     groupSelector     = createGroupFilterButtonSelector(    "income", "DocumentLine", container);
        container.setTop(new HBox(10, conditionSelector.getButton(), groupSelector.getButton()));

        groupView = new GroupView<>();

        container.setCenter(groupView.buildUi());

        pm.conditionStringFilterProperty() .bind(Properties.compute(conditionSelector .selectedItemProperty(), Filters::toStringJson));
        pm.groupStringFilterProperty()     .bind(Properties.compute(groupSelector     .selectedItemProperty(), Filters::toStringJson));

        groupView.groupDisplayResultProperty().bind(pm.groupDisplayResultProperty());
        groupView.groupStringFilterProperty().bind(pm.groupStringFilterProperty());
        pm.selectedGroupConditionStringFilterProperty().bind(groupView.selectedGroupConditionStringFilterProperty());
        groupView.setReferenceResolver(groupFilter.getRootAliasReferenceResolver());

        return container;
    }

    private ReactiveExpressionFilter<DocumentLine> groupFilter;

    @Override
    protected void startLogic() {
        // Setting up the left group filter for the left content displayed in the group view
        groupFilter = this.<DocumentLine>createReactiveExpressionFilter("{class: 'DocumentLine', alias: 'dl'}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> "{where: `document.event=" + eventId + "`}")
                // Applying the condition and group selected by the user
                .combineIfNotNullOtherwiseForceEmptyResult(pm.conditionStringFilterProperty(), stringFilter -> stringFilter)
                //.combine("{where: '!cancelled'}")
                .combineIfNotNullOtherwiseForceEmptyResult(pm.groupStringFilterProperty(), stringFilter -> stringFilter.contains("groupBy") ? stringFilter : "{where: 'false'}")
                // Displaying the result in the group view
                .displayResultInto(pm.groupDisplayResultProperty())
                // Everything set up, let's start now!
                .start();
    }

    @Override
    protected void refreshDataOnActive() {
        groupFilter.refreshWhenActive();
    }
}
