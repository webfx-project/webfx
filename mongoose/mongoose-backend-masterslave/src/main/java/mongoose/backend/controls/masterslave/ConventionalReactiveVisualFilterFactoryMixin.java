package mongoose.backend.controls.masterslave;

import mongoose.client.presentationmodel.*;
import webfx.framework.client.orm.entity.filter.visual.ReactiveVisualFilterFactoryMixin;
import webfx.framework.client.orm.entity.filter.visual.ReactiveVisualFilter;
import webfx.framework.shared.orm.entity.Entity;
import webfx.kit.util.properties.Properties;

public interface ConventionalReactiveVisualFilterFactoryMixin extends ReactiveVisualFilterFactoryMixin {

    default <E extends Entity> ReactiveVisualFilter<E> createGroupReactiveVisualFilter(Object pm) {
        return initializeGroupReactiveVisualFilter(createReactiveVisualFilter(), pm);
    }

    default <E extends Entity> ReactiveVisualFilter<E> createGroupReactiveVisualFilter(Object pm, Object jsonOrClass) {
        return initializeGroupReactiveVisualFilter(createReactiveVisualFilter(jsonOrClass), pm);
    }

    default <E extends Entity> ReactiveVisualFilter<E> initializeGroupReactiveVisualFilter(ReactiveVisualFilter<E> filter, Object pm) {
        // Applying the condition and group selected by the user
        if (pm instanceof HasConditionEqlFilterStringProperty)
            filter.combineIfNotNullOtherwiseForceEmptyResult(((HasConditionEqlFilterStringProperty) pm).conditionEqlFilterStringProperty(), eqlFilterString -> eqlFilterString);
        if (pm instanceof HasGroupEqlFilterStringProperty)
            filter.combineIfNotNullOtherwiseForceEmptyResult(((HasGroupEqlFilterStringProperty) pm).groupEqlFilterStringProperty(), eqlFilterString -> eqlFilterString.contains("groupBy") ? eqlFilterString : "{where: 'false'}");
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

    default <E extends Entity> ReactiveVisualFilter<E> initializeMasterReactiveVisualFilter(ReactiveVisualFilter<E> orm, Object pm) {
        // Applying the condition and columns selected by the user
        if (pm instanceof HasConditionEqlFilterStringProperty)
            orm.combineIfNotNullOtherwiseForceEmptyResult(((HasConditionEqlFilterStringProperty) pm).conditionEqlFilterStringProperty(), eqlFilterString -> eqlFilterString);
        if (pm instanceof HasColumnsEqlFilterStringProperty)
            orm.combineIfNotNullOtherwiseForceEmptyResult(((HasColumnsEqlFilterStringProperty) pm).columnsEqlFilterStringProperty(), eqlFilterString -> eqlFilterString);
        // Also, in case groups are showing and a group is selected, applying the condition associated with that group
        if (pm instanceof HasSelectedGroupConditionEqlFilterStringProperty)
            orm.combineIfNotNull(((HasSelectedGroupConditionEqlFilterStringProperty) pm).selectedGroupConditionEqlFilterStringProperty(), s -> s);
        if (pm instanceof HasMasterVisualResultProperty)
            orm.visualizeResultInto(((HasMasterVisualResultProperty) pm).masterVisualResultProperty());
        if (pm instanceof HasMasterVisualSelectionProperty)
            orm.setVisualSelectionProperty(((HasMasterVisualSelectionProperty) pm).masterVisualSelectionProperty());
        if (pm instanceof HasSelectedMasterProperty)
            orm.setSelectedEntityHandler(((HasSelectedMasterProperty) pm)::setSelectedMaster);
        // Limit clause
        if (pm instanceof HasLimitProperty)
            orm.combineIfPositive(((HasLimitProperty) pm).limitProperty(), limit -> "{limit: `" + limit + "`}");
        return orm;
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
            ), () -> "{where: 'false'}");
        return filter;
    }
}
