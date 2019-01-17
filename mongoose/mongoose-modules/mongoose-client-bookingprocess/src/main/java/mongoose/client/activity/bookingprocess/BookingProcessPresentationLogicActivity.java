package mongoose.client.activity.bookingprocess;

import javafx.event.ActionEvent;
import mongoose.client.activity.eventdependent.EventDependentPresentationLogicActivity;
import webfx.platform.shared.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public abstract class BookingProcessPresentationLogicActivity<PM extends BookingProcessPresentationModel>
    extends EventDependentPresentationLogicActivity<PM> {

    public BookingProcessPresentationLogicActivity(Factory<PM> presentationModelFactory) {
        super(presentationModelFactory);
    }

    @Override
    protected void initializePresentationModel(PM pm) {
        super.initializePresentationModel(pm);
        pm.setOnPreviousAction(this::onPreviousButtonPressed);
        pm.setOnNextAction(this::onNextButtonPressed);
    }

    private void onPreviousButtonPressed(ActionEvent event) {
        getHistory().goBack();
    }

    protected void onNextButtonPressed(ActionEvent event) {
    }
}
