package mongooses.core.activities.backend.events;

import webfx.framework.activity.impl.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
final class EventsActivity extends DomainPresentationActivityImpl<EventsPresentationModel> {

    EventsActivity() {
        super(EventsPresentationViewActivity::new, EventsPresentationLogicActivity::new);
    }
}
