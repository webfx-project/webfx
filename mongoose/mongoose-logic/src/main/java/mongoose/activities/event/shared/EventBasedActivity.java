package mongoose.activities.event.shared;

import naga.commons.util.function.Factory;
import naga.framework.ui.presentation.PresentationActivity;
import naga.framework.ui.presentation.ViewModel;

/**
 * @author Bruno Salmon
 */
public abstract class EventBasedActivity<VM extends ViewModel, PM extends EventBasedPresentationModel> extends PresentationActivity<VM, PM> {

    protected EventBasedActivity() {
    }

    protected EventBasedActivity(Factory<PM> presentationModelFactory) {
        super(presentationModelFactory);
    }

    @Override
    protected void initializePresentationModel(PM pm) {
        pm.eventIdProperty().setValue(getParameter("eventId"));
    }

}
