package mongoose.activities.backend.events;

import naga.framework.activity.view.presentation.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
public class EventsPresentationActivity extends DomainPresentationActivityImpl<EventsPresentationModel> {

    public EventsPresentationActivity() {
        super(EventsPresentationViewActivity::new, EventsPresentationLogicActivity::new);
    }
}
