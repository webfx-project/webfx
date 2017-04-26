package mongoose.activities.shared.book.event.shared;

import javafx.event.ActionEvent;
import mongoose.activities.shared.generic.eventdependent.EventDependentPresentationLogicActivity;
import naga.commons.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public abstract class BookingProcessPresentationLogicActivity<PM extends BookingProcessPresentationModel>
    extends EventDependentPresentationLogicActivity<PM> {

    private final String nextPage;

    public BookingProcessPresentationLogicActivity(Factory<PM> presentationModelFactory, String nextPage) {
        super(presentationModelFactory);
        this.nextPage = nextPage;
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
        goToNextBookingProcessPage(nextPage);
    }

    protected void goToNextBookingProcessPage(String page) {
        getHistory().push("/book/event/" + getEventId() + "/" + page);
    }
}
