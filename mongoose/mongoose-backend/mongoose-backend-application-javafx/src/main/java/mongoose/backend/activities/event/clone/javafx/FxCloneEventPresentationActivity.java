package mongoose.backend.activities.event.clone.javafx;

import mongoose.backend.activities.cloneevent.CloneEventPresentationLogicActivity;
import mongoose.backend.activities.cloneevent.CloneEventPresentationModel;
import webfx.framework.client.activity.impl.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
final class FxCloneEventPresentationActivity extends DomainPresentationActivityImpl<CloneEventPresentationModel> {

    FxCloneEventPresentationActivity() {
        super(FxCloneEventPresentationViewActivity::new, CloneEventPresentationLogicActivity::new);
    }
}
