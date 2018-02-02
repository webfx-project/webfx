package mongoose.activities.backend.javafx.event.clone;

import mongoose.activities.backend.event.clone.CloneEventPresentationLogicActivity;
import mongoose.activities.backend.event.clone.CloneEventPresentationModel;
import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
class FxCloneEventPresentationActivity extends DomainPresentationActivityImpl<CloneEventPresentationModel> {

    public FxCloneEventPresentationActivity() {
        super(FxCloneEventPresentationViewActivity::new, CloneEventPresentationLogicActivity::new);
    }
}
