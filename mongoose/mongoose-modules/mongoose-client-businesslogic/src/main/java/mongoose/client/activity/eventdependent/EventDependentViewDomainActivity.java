package mongoose.client.activity.eventdependent;

import mongoose.client.activity.MongooseButtonFactoryMixin;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityBase;

/**
 * @author Bruno Salmon
 */
public abstract class EventDependentViewDomainActivity
    extends ViewDomainActivityBase
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
