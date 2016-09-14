package mongoose.activities.frontend.event.booking;

import naga.commons.util.function.Factory;
import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.spi.events.ActionEvent;
import naga.toolkit.spi.nodes.controls.Button;

/**
 * @author Bruno Salmon
 */
public abstract class BookingProcessActivity<VM extends BookingProcessViewModel, PM extends BookingProcessPresentationModel> extends PresentationActivity<VM, PM> {

    private final String nextPage;

    public BookingProcessActivity(Factory<PM> presentationModelFactory, String nextPage) {
        super(presentationModelFactory);
        this.nextPage = nextPage;
    }

    protected void bindViewModelWithPresentationModel(VM vm, PM pm) {
        Button previousButton = vm.getPreviousButton();
        previousButton.setText("« Previous");
        Button nextButton = vm.getNextButton();
        nextButton.setText("Next »");
        previousButton.actionEventObservable().subscribe(this::onPreviousButtonPressed);
        nextButton.actionEventObservable().subscribe(this::onNextButtonPressed);
    }

    protected void onPreviousButtonPressed(ActionEvent actionEvent) {
        getHistory().goBack();
    }

    protected void onNextButtonPressed(ActionEvent actionEvent) {
        getHistory().push("/event/" + getParameter("eventId") + "/" + nextPage);
    }

    protected void bindPresentationModelWithLogic(PM pm) {
    }

}
