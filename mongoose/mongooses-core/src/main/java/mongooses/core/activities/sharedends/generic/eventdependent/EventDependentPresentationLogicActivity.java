package mongooses.core.activities.sharedends.generic.eventdependent;

import mongooses.core.activities.sharedends.generic.MongooseDomainPresentationLogicActivityBase;
import webfx.framework.activity.impl.combinations.domainpresentationlogic.impl.DomainPresentationLogicActivityContextFinal;
import webfx.platforms.core.util.function.Factory;

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
