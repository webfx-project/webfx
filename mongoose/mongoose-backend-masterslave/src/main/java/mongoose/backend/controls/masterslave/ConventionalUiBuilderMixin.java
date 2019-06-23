package mongoose.backend.controls.masterslave;

import mongoose.client.entities.util.filters.FilterButtonSelectorFactoryMixin;
import mongoose.client.presentationmodel.HasGroupDisplayResultProperty;
import mongoose.client.presentationmodel.HasMasterDisplayResultProperty;
import mongoose.client.presentationmodel.HasSelectedMasterProperty;

public interface ConventionalUiBuilderMixin extends FilterButtonSelectorFactoryMixin {

    default <PM extends HasGroupDisplayResultProperty & HasMasterDisplayResultProperty & HasSelectedMasterProperty>
    ConventionalUiBuilder createAndBindGroupMasterSlaveViewWithFilterSearchBar(PM pm, String activityName, String domainClassId) {
        return ConventionalUiBuilder.createAndBindGroupMasterSlaveViewWithFilterSearchBar(pm, this, activityName, domainClassId);
    }

}
