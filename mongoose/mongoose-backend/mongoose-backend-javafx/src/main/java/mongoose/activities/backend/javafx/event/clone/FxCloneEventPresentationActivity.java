package mongoose.activities.backend.javafx.event.clone;

import mongoose.activities.backend.cloneevent.CloneEventPresentationLogicActivity;
import mongoose.activities.backend.cloneevent.CloneEventPresentationModel;
import naga.framework.activity.base.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
class FxCloneEventPresentationActivity extends DomainPresentationActivityImpl<CloneEventPresentationModel> {

    FxCloneEventPresentationActivity() {
        super(FxCloneEventPresentationViewActivity::new, CloneEventPresentationLogicActivity::new);
    }
}
