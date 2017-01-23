package mongoose.activities.shared.generic;

import naga.commons.util.function.Factory;
import naga.framework.activity.combinations.domainpresentationlogic.impl.DomainPresentationLogicActivityContextFinal;
import naga.framework.activity.combinations.domainpresentationlogic.impl.DomainPresentationLogicActivityImpl;

/**
 * @author Bruno Salmon
 */
public abstract class EventDependentPresentationLogicActivity
        <PM extends EventDependentPresentationModel>

        extends DomainPresentationLogicActivityImpl<PM>
        implements EventDependentMixin<DomainPresentationLogicActivityContextFinal<PM>> {

    public EventDependentPresentationLogicActivity(Factory<PM> presentationModelFactory) {
        super(presentationModelFactory);
    }

    protected void initializePresentationModel(PM pm) {
        pm.setEventId(getEventId());
    }

}
