package mongoose.client.entities.util.filters;

import javafx.beans.property.Property;
import javafx.scene.layout.Pane;
import webfx.framework.client.orm.reactive.dql.statement.conventions.HasColumnsDqlStatementProperty;
import webfx.framework.client.orm.reactive.dql.statement.conventions.HasConditionDqlStatementProperty;
import webfx.framework.client.orm.reactive.dql.statement.conventions.HasGroupDqlStatementProperty;
import mongoose.shared.entities.Filter;
import webfx.framework.shared.orm.dql.DqlStatement;
import webfx.framework.client.ui.controls.button.ButtonFactoryMixin;
import webfx.framework.client.ui.controls.entity.selector.EntityButtonSelector;
import webfx.framework.shared.orm.domainmodel.HasDataSourceModel;
import webfx.kit.util.properties.Properties;

import java.util.function.Predicate;

public interface FilterButtonSelectorFactoryMixin extends ButtonFactoryMixin, HasDataSourceModel {

    String FILTER_SELECTOR_TEMPLATE = "{class: 'Filter', fields: 'class,alias,fields,whereClause,groupByClause,havingClause,orderByClause,limitClause,columns', where: `class='#class#' and #condition# and (activityName='#activityName#' or !exists(select Filter where class='#class#' and #condition# and activityName='#activityName#') and activityName=null)`, orderBy: 'ord,id'}";

    default EntityButtonSelector<Filter> createFilterButtonSelector(String activityName, String domainClassId, String conditionToken, Predicate<Filter> autoSelectPredicate, Pane parent) {
        EntityButtonSelector<Filter> selector = new EntityButtonSelector<>(FILTER_SELECTOR_TEMPLATE.replaceAll("#class#", domainClassId).replaceAll("#condition#", conditionToken).replaceAll("#activityName#", activityName), this, parent, getDataSourceModel());
        selector.autoSelectFirstEntity(autoSelectPredicate);
        selector.setAutoOpenOnMouseEntered(true);
        return selector;
    }

    default EntityButtonSelector<Filter> createConditionFilterButtonSelector(String activityName, String domainClassId, Predicate<Filter> autoSelectPredicate, Pane parent) {
        return createFilterButtonSelector(activityName, domainClassId, "isCondition", autoSelectPredicate, parent);
    }

    default EntityButtonSelector<Filter> createGroupFilterButtonSelector(String activityName, String domainClassId, Predicate<Filter> autoSelectPredicate, Pane parent) {
        return createFilterButtonSelector(activityName, domainClassId, "isGroup", autoSelectPredicate, parent);
    }

    default EntityButtonSelector<Filter> createColumnsFilterButtonSelector(String domainClassId, String activityName, Predicate<Filter> autoSelectPredicate, Pane parent) {
        return createFilterButtonSelector(activityName, domainClassId, "isColumns", autoSelectPredicate, parent);
    }

    default EntityButtonSelector<Filter> createConditionFilterButtonSelector(String activityName, String domainClassId, Pane parent) {
        Predicate<Filter> predicate;
        switch (domainClassId) {
            case "Document":
            case "DocumentLine":
                predicate = filter -> "!cancelled".equals(filter.getWhereClause()); break;
            case "MoneyTransfer":
                predicate = filter -> "pending or successful".equals(filter.getWhereClause()); break;
            case "Person": predicate = filter -> "All".equals(filter.getName()); break;
            default:          predicate = null;
        }
        return createConditionFilterButtonSelector(activityName, domainClassId, predicate, parent);
    }

    default EntityButtonSelector<Filter> createGroupFilterButtonSelector(String activityName, String domainClassId, Pane parent) {
        Predicate<Filter> predicate;
        if ("income".equals(activityName) && "DocumentLine".equals(domainClassId)) {
            predicate = filter -> "Family".equals(filter.getName());
        } else switch (domainClassId) {
            case "DocumentLine": predicate = filter -> "Family, site and item".equals(filter.getName()); break;
            default:             predicate = filter -> "".equals(filter.getName()); break;
        }
        return createGroupFilterButtonSelector(activityName, domainClassId, predicate, parent);
    }

    default EntityButtonSelector<Filter> createColumnsFilterButtonSelector(String activityName, String domainClassId, Pane parent) {
        Predicate<Filter> predicate;
        switch (domainClassId) {
            case "Document"     : predicate = filter -> "prices".equals(filter.getName()); break;
            case "DocumentLine" : predicate = filter -> "statistics".equals(filter.getName()); break;
            case "Person"       : predicate = filter -> "Contact".equals(filter.getName()); break;
            default:          predicate = null;
        }
        return createColumnsFilterButtonSelector(domainClassId, activityName, predicate, parent);
    }


    default EntityButtonSelector<Filter> createConditionFilterButtonSelectorAndBind(String activityName, String domainClassId, Pane parent, Property<DqlStatement> conditionDqlStatementProperty) {
        EntityButtonSelector<Filter> conditionSelector = createConditionFilterButtonSelector(activityName, domainClassId, parent);
        conditionDqlStatementProperty.bind(Properties.compute(conditionSelector.selectedItemProperty(), Filters::toDqlStatement));
        return conditionSelector;
    }

    default EntityButtonSelector<Filter> createGroupFilterButtonSelectorAndBind(String activityName, String domainClassId, Pane parent, Property<DqlStatement> groupDqlStatementProperty) {
        EntityButtonSelector<Filter> conditionSelector = createGroupFilterButtonSelector(activityName, domainClassId, parent);
        groupDqlStatementProperty.bind(Properties.compute(conditionSelector.selectedItemProperty(), Filters::toDqlStatement));
        return conditionSelector;
    }

    default EntityButtonSelector<Filter> createColumnsFilterButtonSelectorAndBind(String activityName, String domainClassId, Pane parent, Property<DqlStatement> columnsDqlStatementProperty) {
        EntityButtonSelector<Filter> conditionSelector = createColumnsFilterButtonSelector(activityName, domainClassId, parent);
        columnsDqlStatementProperty.bind(Properties.compute(conditionSelector.selectedItemProperty(), Filters::toDqlStatement));
        return conditionSelector;
    }

    default EntityButtonSelector<Filter> createConditionFilterButtonSelectorAndBind(String activityName, String domainClassId, Pane parent, HasConditionDqlStatementProperty pm) {
        return createConditionFilterButtonSelectorAndBind(activityName, domainClassId, parent, pm.conditionDqlStatementProperty());
    }

    default EntityButtonSelector<Filter> createGroupFilterButtonSelectorAndBind(String activityName, String domainClassId, Pane parent, HasGroupDqlStatementProperty pm) {
        return createGroupFilterButtonSelectorAndBind(activityName, domainClassId, parent, pm.groupDqlStatementProperty());
    }

    default EntityButtonSelector<Filter> createColumnsFilterButtonSelectorAndBind(String activityName, String domainClassId, Pane parent, HasColumnsDqlStatementProperty pm) {
        return createColumnsFilterButtonSelectorAndBind(activityName, domainClassId, parent, pm.columnsDqlStatementProperty());
    }

    default FilterSearchBar createFilterSearchBar(String activityName, String domainClassId, Pane parent, Object pm) {
        return new FilterSearchBar(this, activityName, domainClassId, parent, pm);
    }
}
