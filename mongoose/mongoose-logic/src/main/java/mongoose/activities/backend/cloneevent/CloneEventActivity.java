package mongoose.activities.backend.cloneevent;

import naga.framework.activity.base.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
class CloneEventActivity extends DomainPresentationActivityImpl<CloneEventPresentationModel> {

    CloneEventActivity() {
        super(CloneEventPresentationViewActivity::new, CloneEventPresentationLogicActivity::new);
    }
}
