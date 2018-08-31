package mongoose.activities.bothends.generic.eventdependent;

import mongoose.activities.bothends.generic.MongooseDomainPresentationLogicActivityBase;
import webfx.framework.activity.base.combinations.domainpresentationlogic.impl.DomainPresentationLogicActivityContextFinal;
import webfx.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public abstract class EventDependentPresentationLogicActivity
        <PM extends EventDependentPresentationModel>

        extends MongooseDomainPresentationLogicActivityBase<PM>
        implements EventDependentActivityMixin<DomainPresentationLogicActivityContextFinal<PM>> {

    public EventDependentPresentationLogicActivity(Factory<PM> presentationModelFactory) {
        super(presentationModelFactory);
    }

    @Override
    public PM getPresentationModel() {
        return getActivityContext().getPresentationModel();
    }

    @Override
    protected void updatePresentationModelFromContextParameters(PM pm) {
        updateEventDependentPresentationModelFromContextParameters();
    }
}
