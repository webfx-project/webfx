package mongoose.activities.backend.event.clone;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
class CloneEventActivity extends DomainPresentationActivityImpl<CloneEventPresentationModel> {

    CloneEventActivity() {
        super(CloneEventPresentationViewActivity::new, CloneEventPresentationLogicActivity::new);
    }
}
