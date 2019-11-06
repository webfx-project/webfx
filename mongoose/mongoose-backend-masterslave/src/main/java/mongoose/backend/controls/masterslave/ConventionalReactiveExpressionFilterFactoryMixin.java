package mongoose.backend.controls.masterslave;

import mongoose.client.presentationmodel.*;
import webfx.framework.client.ui.filter.ReactiveExpressionFilter;
import webfx.framework.client.ui.filter.ReactiveExpressionFilterFactoryMixin;
import webfx.framework.shared.orm.entity.Entity;
import webfx.kit.util.properties.Properties;

public interface ConventionalReactiveExpressionFilterFactoryMixin extends ReactiveExpressionFilterFactoryMixin {

    default <E extends Entity> ReactiveExpressionFilter<E> createGroupReactiveExpressionFilter(Object pm) {
        return initializeGroupReactiveExpressionFilter(createReactiveExpressionFilter(), pm);
    }

    default <E extends Entity> ReactiveExpressionFilter<E> createGroupReactiveExpressionFilter(Object pm, Object jsonOrClass) {
        return initializeGroupReactiveExpressionFilter(createReactiveExpressionFilter(jsonOrClass), pm);
    }

    default <E extends Entity> ReactiveExpressionFilter<E> initializeGroupReactiveExpressionFilter(ReactiveExpressionFilter<E> groupReactiveExpressionFilter, Object pm) {
        // Applying the condition and group selected by the user
        if (pm instanceof HasConditionStringFilterProperty)
            groupReactiveExpressionFilter.combineIfNotNullOtherwiseForceEmptyResult(((HasConditionStringFilterProperty) pm).conditionStringFilterProperty(), stringFilter -> stringFilter);
        if (pm instanceof HasGroupStringFilterProperty)
            groupReactiveExpressionFilter.combineIfNotNullOtherwiseForceEmptyResult(((HasGroupStringFilterProperty) pm).groupStringFilterProperty(), stringFilter -> stringFilter.contains("groupBy") ? stringFilter : "{where: 'false'}");
        if (pm instanceof HasGroupVisualResultProperty)
            groupReactiveExpressionFilter.visualizeResultInto(((HasGroupVisualResultProperty) pm).groupVisualResultProperty());
        if (pm instanceof HasGroupVisualSelectionProperty)
            groupReactiveExpressionFilter.setVisualSelectionProperty(((HasGroupVisualSelectionProperty) pm).groupVisualSelectionProperty());
        if (pm instanceof HasSelectedGroupProperty)
            groupReactiveExpressionFilter.setSelectedEntityHandler(((HasSelectedGroupProperty) pm)::setSelectedGroup);
        if (pm instanceof HasSelectedGroupReferenceResolver)
            ((HasSelectedGroupReferenceResolver) pm).setSelectedGroupReferenceResolver(groupReactiveExpressionFilter.getRootAliasReferenceResolver());
        return groupReactiveExpressionFilter;
    }

    default <E extends Entity> ReactiveExpressionFilter<E> createMasterReactiveExpressionFilter(Object pm) {
        return initializeMasterReactiveExpressionFilter(createReactiveExpressionFilter(), pm);
    }

    default <E extends Entity> ReactiveExpressionFilter<E> createMasterReactiveExpressionFilter(Object pm, Object jsonOrClass) {
        return initializeMasterReactiveExpressionFilter(createReactiveExpressionFilter(jsonOrClass), pm);
    }

    default <E extends Entity> ReactiveExpressionFilter<E> initializeMasterReactiveExpressionFilter(ReactiveExpressionFilter<E> masterReactiveExpressionFilter, Object pm) {
        // Applying the condition and columns selected by the user
        if (pm instanceof HasConditionStringFilterProperty)
            masterReactiveExpressionFilter.combineIfNotNullOtherwiseForceEmptyResult(((HasConditionStringFilterProperty) pm).conditionStringFilterProperty(), stringFilter -> stringFilter);
        if (pm instanceof HasColumnsStringFilterProperty)
            masterReactiveExpressionFilter.combineIfNotNullOtherwiseForceEmptyResult(((HasColumnsStringFilterProperty) pm).columnsStringFilterProperty(), stringFilter -> stringFilter);
        // Also, in case groups are showing and a group is selected, applying the condition associated with that group
        if (pm instanceof HasSelectedGroupConditionStringFilterProperty)
            masterReactiveExpressionFilter.combineIfNotNull(((HasSelectedGroupConditionStringFilterProperty) pm).selectedGroupConditionStringFilterProperty(), s -> s);
        if (pm instanceof HasMasterVisualResultProperty)
            masterReactiveExpressionFilter.visualizeResultInto(((HasMasterVisualResultProperty) pm).masterVisualResultProperty());
        if (pm instanceof HasMasterVisualSelectionProperty)
            masterReactiveExpressionFilter.setVisualSelectionProperty(((HasMasterVisualSelectionProperty) pm).masterVisualSelectionProperty());
        if (pm instanceof HasSelectedMasterProperty)
            masterReactiveExpressionFilter.setSelectedEntityHandler(((HasSelectedMasterProperty) pm)::setSelectedMaster);
        // Limit clause
        if (pm instanceof HasLimitProperty)
            masterReactiveExpressionFilter.combineIfPositive(((HasLimitProperty) pm).limitProperty(), limit -> "{limit: `" + limit + "`}");
        return masterReactiveExpressionFilter;
    }

    default <E extends Entity> ReactiveExpressionFilter<E> createSlaveReactiveExpressionFilter(Object pm) {
        return initializeSlaveReactiveExpressionFilter(createReactiveExpressionFilter(), pm);
    }

    default <E extends Entity> ReactiveExpressionFilter<E> createSlaveReactiveExpressionFilter(Object pm, Object jsonOrClass) {
        return initializeSlaveReactiveExpressionFilter(createReactiveExpressionFilter(jsonOrClass), pm);
    }

    default <E extends Entity> ReactiveExpressionFilter<E> initializeSlaveReactiveExpressionFilter(ReactiveExpressionFilter<E> slaveReactiveExpressionFilter, Object pm) {
        if (pm instanceof HasSlaveVisualResultProperty)
            slaveReactiveExpressionFilter.visualizeResultInto(((HasSlaveVisualResultProperty) pm).slaveVisualResultProperty());
        if (pm instanceof HasSelectedMasterProperty)
            slaveReactiveExpressionFilter.combineIfTrue(Properties.compute(((HasSelectedMasterProperty) pm).selectedMasterProperty(), selectedMaster ->
                selectedMaster == null || pm instanceof HasSlaveVisibilityCondition && !((HasSlaveVisibilityCondition) pm).isSlaveVisible(selectedMaster)
            ), () -> "{where: 'false'}");
        return slaveReactiveExpressionFilter;
    }
}
