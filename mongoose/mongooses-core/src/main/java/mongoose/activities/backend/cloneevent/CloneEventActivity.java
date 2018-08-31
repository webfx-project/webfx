package mongoose.activities.backend.cloneevent;

import webfx.framework.activity.base.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
final class CloneEventActivity extends DomainPresentationActivityImpl<CloneEventPresentationModel> {

    CloneEventActivity() {
        super(CloneEventPresentationViewActivity::new, CloneEventPresentationLogicActivity::new);
    }
}
