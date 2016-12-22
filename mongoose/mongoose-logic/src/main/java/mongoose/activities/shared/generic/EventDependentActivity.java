package mongoose.activities.shared.generic;

import mongoose.activities.shared.generic.EventDependentPresentationModel;
import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.entities.Event;
import mongoose.services.EventService;
import mongoose.services.EventServiceMixin;
import naga.commons.util.Objects;
import naga.commons.util.function.Factory;
import naga.framework.ui.presentation.PresentationActivity;
import naga.framework.ui.presentation.ViewModel;

/**
 * @author Bruno Salmon
 */
public abstract class EventDependentActivity
        <VM extends ViewModel, PM extends EventDependentPresentationModel>

        extends PresentationActivity<VM, PM> implements EventServiceMixin {

    public EventDependentActivity(Factory<PM> presentationModelFactory) {
        super(presentationModelFactory);
    }

    protected void initializePresentationModel(PM pm) {
        pm.setEventId(getEventId());
    }

    protected Object getEventId() {
        return getParameter("eventId");
    }

    public EventService getEventService() { // Mainly to make EventServiceMixin work
        return EventService.getOrCreate(getEventId(), getDataSourceModel());
    }

    protected DateTimeRange getEventMaxDateTimeRange() {
        Event event = getEvent();
        return Objects.coalesce(event.getParsedMaxDateTimeRange(), event.getParsedDateTimeRange());
    }
}
