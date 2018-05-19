package mongoose.activities.backend.event.clone;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
class CloneEventPresentationActivity extends DomainPresentationActivityImpl<CloneEventPresentationModel> {

    CloneEventPresentationActivity() {
        super(CloneEventPresentationViewActivity::new, CloneEventPresentationLogicActivity::new);
    }
}
