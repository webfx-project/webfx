package mongoose.client.activity.eventdependent;

import mongoose.client.presentationmodel.HasOrganizationIdProperty;
import mongoose.client.presentationmodel.HasEventIdProperty;

/**
 * @author Bruno Salmon
 */
public interface EventDependentPresentationModel
        extends HasEventIdProperty,
        HasOrganizationIdProperty {

}
