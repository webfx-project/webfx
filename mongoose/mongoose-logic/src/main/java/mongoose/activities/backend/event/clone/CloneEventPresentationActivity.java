package mongoose.activities.backend.event.clone;

import naga.framework.activity.view.presentation.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
public class CloneEventPresentationActivity extends DomainPresentationActivityImpl<CloneEventPresentationModel> {

    public CloneEventPresentationActivity() {
        super(CloneEventPresentationViewActivity::new, CloneEventPresentationLogicActivity::new);
    }
}
