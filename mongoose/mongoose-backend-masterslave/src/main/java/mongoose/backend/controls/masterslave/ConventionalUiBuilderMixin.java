package mongoose.backend.controls.masterslave;

import mongoose.client.entities.util.filters.FilterButtonSelectorFactoryMixin;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.conventions.HasGroupVisualResultProperty;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.conventions.HasMasterVisualResultProperty;
import webfx.framework.client.orm.reactive.dql.statement.conventions.HasSelectedMasterProperty;

public interface ConventionalUiBuilderMixin extends FilterButtonSelectorFactoryMixin {

    default <PM extends HasGroupVisualResultProperty & HasMasterVisualResultProperty & HasSelectedMasterProperty>
    ConventionalUiBuilder createAndBindGroupMasterSlaveViewWithFilterSearchBar(PM pm, String activityName, String domainClassId) {
        return ConventionalUiBuilder.createAndBindGroupMasterSlaveViewWithFilterSearchBar(pm, this, activityName, domainClassId);
    }

}
