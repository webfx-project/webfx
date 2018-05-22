package mongoose.activities.bothends.generic.eventdependent;

import mongoose.activities.bothends.generic.MongooseButtonFactoryMixin;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.activity.view.impl.ViewActivityImpl;

/**
 * @author Bruno Salmon
 */
public abstract class EventDependentViewDomainActivity
    extends ViewActivityImpl
    implements EventDependentActivityMixin<ViewDomainActivityContextFinal>,
        MongooseButtonFactoryMixin {

    // Should the presentation model be stored in the context instead (like for logic presentation activity?)
    private final EventDependentPresentationModel presentationModel = new EventDependentPresentationModelImpl();

    @Override
    public EventDependentPresentationModel getPresentationModel() {
        return presentationModel;
    }

    @Override
    protected void updateModelFromContextParameters() {
        updateEventDependentPresentationModelFromContextParameters();
        super.updateModelFromContextParameters();
    }
}
