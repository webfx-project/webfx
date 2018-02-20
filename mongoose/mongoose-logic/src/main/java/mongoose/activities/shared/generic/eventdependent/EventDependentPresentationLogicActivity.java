package mongoose.activities.shared.generic.eventdependent;

import mongoose.activities.shared.generic.MongooseDomainPresentationLogicActivityBase;
import naga.framework.activity.combinations.domainpresentationlogic.impl.DomainPresentationLogicActivityContextFinal;
import naga.util.function.Factory;

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
