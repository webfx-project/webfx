package mongoose.client.entities.util.filters;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import webfx.framework.client.orm.reactive.dql.statement.conventions.HasColumnsDqlStatementProperty;
import webfx.framework.client.orm.reactive.dql.statement.conventions.HasConditionDqlStatementProperty;
import webfx.framework.client.orm.reactive.dql.statement.conventions.HasGroupDqlStatementProperty;
import mongoose.client.presentationmodel.HasSearchTextProperty;
import mongoose.shared.entities.Filter;
import webfx.framework.client.ui.controls.entity.selector.EntityButtonSelector;
import webfx.framework.client.ui.util.scene.SceneUtil;

import static webfx.framework.client.ui.util.layout.LayoutUtil.setHGrowable;
import static webfx.framework.client.ui.util.layout.LayoutUtil.setMaxHeightToInfinite;

public final class FilterSearchBar {

    private final EntityButtonSelector<Filter> conditionSelector, groupSelector, columnsSelector;
    private final TextField searchBox; // Keeping this reference to activate focus on activity resume

    public FilterSearchBar(FilterButtonSelectorFactoryMixin mixin, String activityName, String domainClassId, Pane parent, Object pm) {
        if (pm instanceof HasConditionDqlStatementProperty)
            conditionSelector = mixin.createConditionFilterButtonSelectorAndBind(activityName,domainClassId, parent, (HasConditionDqlStatementProperty) pm);
        else
            conditionSelector = null;
        if (pm instanceof HasGroupDqlStatementProperty)
            groupSelector = mixin.createGroupFilterButtonSelectorAndBind(activityName,domainClassId, parent, (HasGroupDqlStatementProperty) pm);
        else
            groupSelector = null;
        if (pm instanceof HasColumnsDqlStatementProperty)
            columnsSelector = mixin.createColumnsFilterButtonSelectorAndBind(activityName,domainClassId, parent, (HasColumnsDqlStatementProperty) pm);
        else
            columnsSelector = null;
        if (pm instanceof HasSearchTextProperty) {
            searchBox = mixin.newTextField("GenericSearch"); // Will set the prompt
            ((HasSearchTextProperty) pm).searchTextProperty().bind(searchBox.textProperty());
        } else
            searchBox = null;
    }

    public HBox buildUi() {
        HBox bar = new HBox(10);
        ObservableList<Node> children = bar.getChildren();
        if (conditionSelector != null)
            children.add(conditionSelector.getButton());
        if (groupSelector != null)
            children.add(groupSelector.getButton());
        if (columnsSelector != null)
            children.add(columnsSelector.getButton());
        if (searchBox != null)
            children.add(setMaxHeightToInfinite(setHGrowable(searchBox)));
        return bar;
    }

    public void onResume() {
        if (searchBox != null)
            SceneUtil.autoFocusIfEnabled(searchBox);
    }

}
