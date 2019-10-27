package mongoose.backend.controls.masterslave;

import mongoose.client.entities.util.filters.FilterButtonSelectorFactoryMixin;
import mongoose.client.presentationmodel.HasGroupVisualResultProperty;
import mongoose.client.presentationmodel.HasMasterVisualResultProperty;
import mongoose.client.presentationmodel.HasSelectedMasterProperty;

public interface ConventionalUiBuilderMixin extends FilterButtonSelectorFactoryMixin {

    default <PM extends HasGroupVisualResultProperty & HasMasterVisualResultProperty & HasSelectedMasterProperty>
    ConventionalUiBuilder createAndBindGroupMasterSlaveViewWithFilterSearchBar(PM pm, String activityName, String domainClassId) {
        return ConventionalUiBuilder.createAndBindGroupMasterSlaveViewWithFilterSearchBar(pm, this, activityName, domainClassId);
    }

}
