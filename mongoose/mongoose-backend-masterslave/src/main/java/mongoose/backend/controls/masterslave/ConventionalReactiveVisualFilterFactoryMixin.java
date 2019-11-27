package mongoose.backend.controls.masterslave;

import mongoose.client.presentationmodel.*;
import webfx.framework.client.orm.dql.DqlStatement;
import webfx.framework.client.orm.entity.filter.visual.ReactiveVisualFilterFactoryMixin;
import webfx.framework.client.orm.entity.filter.visual.ReactiveVisualFilter;
import webfx.framework.shared.orm.entity.Entity;
import webfx.kit.util.properties.Properties;

import static webfx.framework.client.orm.dql.DqlStatement.limit;

public interface ConventionalReactiveVisualFilterFactoryMixin extends ReactiveVisualFilterFactoryMixin {

    default <E extends Entity> ReactiveVisualFilter<E> createGroupReactiveVisualFilter(Object pm) {
        return initializeGroupReactiveVisualFilter(createReactiveVisualFilter(), pm);
    }

    default <E extends Entity> ReactiveVisualFilter<E> createGroupReactiveVisualFilter(Object pm, Object jsonOrClass) {
        return initializeGroupReactiveVisualFilter(createReactiveVisualFilter(jsonOrClass), pm);
    }

    default <E extends Entity> ReactiveVisualFilter<E> initializeGroupReactiveVisualFilter(ReactiveVisualFilter<E> filter, Object pm) {
        // Applying the condition and group selected by the user
        if (pm instanceof HasConditionDqlStatementProperty)
            filter.combineIfNotNullOtherwiseForceEmptyResult(((HasConditionDqlStatementProperty) pm).conditionDqlStatementProperty(), conditionDqlStatement -> conditionDqlStatement);
        if (pm instanceof HasGroupDqlStatementProperty)
            filter.combineIfNotNullOtherwiseForceEmptyResult(((HasGroupDqlStatementProperty) pm).groupDqlStatementProperty(), groupDqlStatement -> groupDqlStatement.getGroupBy() != null ? groupDqlStatement : DqlStatement.EMPTY_STATEMENT);
        if (pm instanceof HasGroupVisualResultProperty)
            filter.visualizeResultInto(((HasGroupVisualResultProperty) pm).groupVisualResultProperty());
        if (pm instanceof HasGroupVisualSelectionProperty)
            filter.setVisualSelectionProperty(((HasGroupVisualSelectionProperty) pm).groupVisualSelectionProperty());
        if (pm instanceof HasSelectedGroupProperty)
            filter.setSelectedEntityHandler(((HasSelectedGroupProperty) pm)::setSelectedGroup);
        if (pm instanceof HasSelectedGroupReferenceResolver)
            ((HasSelectedGroupReferenceResolver) pm).setSelectedGroupReferenceResolver(filter.getRootAliasReferenceResolver());
        return filter;
    }

    default <E extends Entity> ReactiveVisualFilter<E> createMasterReactiveVisualFilter(Object pm) {
        return initializeMasterReactiveVisualFilter(createReactiveVisualFilter(), pm);
    }

    default <E extends Entity> ReactiveVisualFilter<E> createMasterReactiveVisualFilter(Object pm, Object jsonOrClass) {
        return initializeMasterReactiveVisualFilter(createReactiveVisualFilter(jsonOrClass), pm);
    }

    default <E extends Entity> ReactiveVisualFilter<E> initializeMasterReactiveVisualFilter(ReactiveVisualFilter<E> filter, Object pm) {
        // Applying the condition and columns selected by the user
        if (pm instanceof HasConditionDqlStatementProperty)
            filter.combineIfNotNullOtherwiseForceEmptyResult(((HasConditionDqlStatementProperty) pm).conditionDqlStatementProperty(), conditionDqlStatement -> conditionDqlStatement);
        if (pm instanceof HasColumnsDqlStatementProperty)
            filter.combineIfNotNullOtherwiseForceEmptyResult(((HasColumnsDqlStatementProperty) pm).columnsDqlStatementProperty(), columnsDqlStatement -> columnsDqlStatement);
        // Also, in case groups are showing and a group is selected, applying the condition associated with that group
        if (pm instanceof HasSelectedGroupConditionDqlStatementProperty)
            filter.combineIfNotNull(((HasSelectedGroupConditionDqlStatementProperty) pm).selectedGroupConditionDqlStatementProperty(), selectedGroupConditionDqlStatement -> selectedGroupConditionDqlStatement);
        if (pm instanceof HasMasterVisualResultProperty)
            filter.visualizeResultInto(((HasMasterVisualResultProperty) pm).masterVisualResultProperty());
        if (pm instanceof HasMasterVisualSelectionProperty)
            filter.setVisualSelectionProperty(((HasMasterVisualSelectionProperty) pm).masterVisualSelectionProperty());
        if (pm instanceof HasSelectedMasterProperty)
            filter.setSelectedEntityHandler(((HasSelectedMasterProperty<E>) pm)::setSelectedMaster);
        // Limit clause
        if (pm instanceof HasLimitProperty)
            filter.combineIfPositive(((HasLimitProperty) pm).limitProperty(), limit -> limit("?", limit));
        return filter;
    }

    default <E extends Entity> ReactiveVisualFilter<E> createSlaveReactiveVisualFilter(Object pm) {
        return initializeSlaveReactiveVisualFilter(createReactiveVisualFilter(), pm);
    }

    default <E extends Entity> ReactiveVisualFilter<E> createSlaveReactiveVisualFilter(Object pm, Object jsonOrClass) {
        return initializeSlaveReactiveVisualFilter(createReactiveVisualFilter(jsonOrClass), pm);
    }

    default <E extends Entity> ReactiveVisualFilter<E> initializeSlaveReactiveVisualFilter(ReactiveVisualFilter<E> filter, Object pm) {
        if (pm instanceof HasSlaveVisualResultProperty)
            filter.visualizeResultInto(((HasSlaveVisualResultProperty) pm).slaveVisualResultProperty());
        if (pm instanceof HasSelectedMasterProperty)
            filter.combineIfTrue(Properties.compute(((HasSelectedMasterProperty) pm).selectedMasterProperty(), selectedMaster ->
                selectedMaster == null || pm instanceof HasSlaveVisibilityCondition && !((HasSlaveVisibilityCondition) pm).isSlaveVisible(selectedMaster)
            ), DqlStatement.EMPTY_STATEMENT);
        return filter;
    }
}
