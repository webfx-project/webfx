package mongoose.activities.shared.generic.eventdependent;

import mongoose.activities.shared.generic.MongooseButtonFactoryMixin;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.activity.view.impl.ViewActivityImpl;

/**
 * @author Bruno Salmon
 */
public abstract class EventDependentViewDomainActivity
    extends ViewActivityImpl
    implements EventDependentActivityMixin<ViewDomainActivityContextFinal>,
        MongooseButtonFactoryMixin {

    @Override
    protected void fetchRouteParameters() {
        updateEventDependentPresentationModelFromRouteParameters();
        super.fetchRouteParameters();
    }
}
