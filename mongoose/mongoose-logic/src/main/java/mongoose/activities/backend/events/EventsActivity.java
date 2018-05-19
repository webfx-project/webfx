package mongoose.activities.backend.events;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
class EventsActivity extends DomainPresentationActivityImpl<EventsPresentationModel> {

    EventsActivity() {
        super(EventsPresentationViewActivity::new, EventsPresentationLogicActivity::new);
    }
}
