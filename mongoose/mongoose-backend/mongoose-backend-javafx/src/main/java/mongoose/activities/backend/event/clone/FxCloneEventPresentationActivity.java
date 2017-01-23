package mongoose.activities.backend.event.clone;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
public class FxCloneEventPresentationActivity extends DomainPresentationActivityImpl<CloneEventPresentationModel> {

    public FxCloneEventPresentationActivity() {
        super(FxCloneEventPresentationViewActivity::new, CloneEventPresentationLogicActivity::new);
    }
}
