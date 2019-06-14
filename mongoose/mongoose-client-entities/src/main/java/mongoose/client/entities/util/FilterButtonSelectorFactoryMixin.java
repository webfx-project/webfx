package mongoose.client.entities.util;

import javafx.scene.layout.Pane;
import mongoose.shared.entities.Filter;
import webfx.framework.client.ui.controls.button.ButtonFactoryMixin;
import webfx.framework.client.ui.controls.button.EntityButtonSelector;
import webfx.framework.shared.orm.domainmodel.HasDataSourceModel;

import java.util.function.Predicate;

public interface FilterButtonSelectorFactoryMixin extends ButtonFactoryMixin, HasDataSourceModel {

    String FILTER_SELECTOR_TEMPLATE = "{class: 'Filter', fields: 'class,alias,fields,whereClause,groupByClause,havingClause,orderByClause,limitClause,columns', where: `class='${class}' and ${condition}`, orderBy: 'ord,id'}";

    default EntityButtonSelector<Filter> createFilterButtonSelector(String domainClassId, String conditionToken, Predicate<Filter> autoSelectPredicate, Pane parent) {
        EntityButtonSelector<Filter> selector = new EntityButtonSelector<>(FILTER_SELECTOR_TEMPLATE.replace("${class}", domainClassId).replace("${condition}", conditionToken), this, parent, getDataSourceModel());
        selector.autoSelectFirstEntity(autoSelectPredicate);
        selector.setAutoOpenOnMouseEntered(true);
        return selector;
    }

    default EntityButtonSelector<Filter> createConditionFilterButtonSelector(String domainClassId, Predicate<Filter> autoSelectPredicate, Pane parent) {
        return createFilterButtonSelector(domainClassId,"isCondition", autoSelectPredicate, parent);
    }

    default EntityButtonSelector<Filter> createGroupFilterButtonSelector(String domainClassId, Predicate<Filter> autoSelectPredicate, Pane parent) {
        return createFilterButtonSelector(domainClassId,"isGroup", autoSelectPredicate, parent);
    }

    default EntityButtonSelector<Filter> createColumnsFilterButtonSelector(String domainClassId, Predicate<Filter> autoSelectPredicate, Pane parent) {
        return createFilterButtonSelector(domainClassId,"isColumns", autoSelectPredicate, parent);
    }

    default EntityButtonSelector<Filter> createConditionFilterButtonSelector(String domainClassId, Pane parent) {
        Predicate<Filter> predicate;
        switch (domainClassId) {
            case "Document" :
            case "DocumentLine" :
                predicate = filter -> "!cancelled".equals(filter.getWhereClause()); break;
            default:          predicate = null;
        }
        return createConditionFilterButtonSelector(domainClassId, predicate, parent);
    }

    default EntityButtonSelector<Filter> createGroupFilterButtonSelector(String domainClassId, Pane parent) {
        Predicate<Filter> predicate;
        switch (domainClassId) {
            case "Document" :     predicate = filter -> "".equals(filter.getName()); break;
            case "DocumentLine" : predicate = filter -> "Family, site and item".equals(filter.getName()); break;
            default:              predicate = null;
        }
        return createGroupFilterButtonSelector(domainClassId, predicate, parent);
    }

    default EntityButtonSelector<Filter> createColumnsFilterButtonSelector(String domainClassId, Pane parent) {
        Predicate<Filter> predicate;
        switch (domainClassId) {
            case "Document"     : predicate = filter -> "prices".equals(filter.getName()); break;
            case "DocumentLine" : predicate = filter -> "statistics".equals(filter.getName()); break;
            default:          predicate = null;
        }
        return createColumnsFilterButtonSelector(domainClassId, predicate, parent);
    }
}
