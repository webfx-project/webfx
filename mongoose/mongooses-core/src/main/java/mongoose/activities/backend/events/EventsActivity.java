package mongoose.activities.backend.events;

import webfx.framework.activity.base.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
final class EventsActivity extends DomainPresentationActivityImpl<EventsPresentationModel> {

    EventsActivity() {
        super(EventsPresentationViewActivity::new, EventsPresentationLogicActivity::new);
    }
}
