package mongoose.activities.frontend.event.booking;

import mongoose.services.EventService;
import mongoose.services.EventServiceMixin;
import naga.commons.util.function.Factory;
import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.spi.events.ActionEvent;
import naga.toolkit.spi.nodes.controls.Button;

/**
 * @author Bruno Salmon
 */
public abstract class BookingProcessActivity<VM extends BookingProcessViewModel, PM extends BookingProcessPresentationModel>
        extends PresentationActivity<VM, PM> implements EventServiceMixin {

    private final String nextPage;

    public BookingProcessActivity(Factory<PM> presentationModelFactory, String nextPage) {
        super(presentationModelFactory);
        this.nextPage = nextPage;
    }

    protected void bindViewModelWithPresentationModel(VM vm, PM pm) {
        Button previousButton = vm.getPreviousButton();
        if (previousButton != null)
            getI18n().translateText(previousButton, "Back").actionEventObservable().subscribe(this::onPreviousButtonPressed);
        Button nextButton = vm.getNextButton();
        if (nextButton != null)
            getI18n().translateText(nextButton, "Next").actionEventObservable().subscribe(this::onNextButtonPressed);
    }

    private void onPreviousButtonPressed(ActionEvent actionEvent) {
        getHistory().goBack();
    }

    private void onNextButtonPressed(ActionEvent actionEvent) {
        goToNextBookingProcessPage(nextPage);
    }

    protected void goToNextBookingProcessPage(String page) {
        getHistory().push("/event/" + getEventId() + "/" + page);
    }

    protected void initializePresentationModel(PM pm) {
        pm.setEventId(getEventId());
    }

    protected void bindPresentationModelWithLogic(PM pm) {
    }

    private Object getEventId() {
        return getParameter("eventId");
    }

    public EventService getEventService() { // Mainly to make EventServiceMixin work
        return EventService.getOrCreate(getEventId(), getDataSourceModel());
    }

}
