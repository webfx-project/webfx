package mongoose.activities.backend.container;

import mongoose.activities.frontend.container.FrontendContainerActivity;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.Button;

/**
 * @author Bruno Salmon
 */
public class BackendContainerActivity extends FrontendContainerActivity<BackendContainerViewModel, BackendContainerPresentationModel> {

    public BackendContainerActivity() {
        super(BackendContainerPresentationModel::new);
    }

    @Override
    protected BackendContainerViewModel buildView(Toolkit toolkit) {
        Button backButton = toolkit.createButton();
        Button forwardButton = toolkit.createButton();
        Button organizationsButton = toolkit.createButton();
        Button eventsButton = toolkit.createButton();
        Button bookingsButton = toolkit.createButton();
        Button lettersButton = toolkit.createButton();
        Button monitorButton = toolkit.createButton();
        Button testerButton = toolkit.createButton();
        return new BackendContainerViewModel(toolkit.createVPage().setHeader(toolkit.createHBox(backButton, forwardButton, organizationsButton, eventsButton, bookingsButton, lettersButton, monitorButton, testerButton)),
                backButton, forwardButton, organizationsButton, eventsButton, bookingsButton, lettersButton, monitorButton, testerButton);
    }

    @Override
    protected void bindViewModelWithPresentationModel(BackendContainerViewModel vm, BackendContainerPresentationModel pm) {
        super.bindViewModelWithPresentationModel(vm, pm);

        vm.getBookingsButton().setText("Bookings");
        vm.getLettersButton().setText("Letters");
        vm.getMonitorButton().setText("Monitor");
        vm.getTesterButton().setText("Tester");

        vm.getBookingsButton().actionEventObservable().subscribe(actionEvent -> pm.bookingsButtonActionEventObservable().onNext(actionEvent));
        vm.getLettersButton().actionEventObservable().subscribe(actionEvent -> pm.lettersButtonActionEventObservable().onNext(actionEvent));
        vm.getMonitorButton().actionEventObservable().subscribe(actionEvent -> getHistory().push("/monitor"));
        vm.getTesterButton().actionEventObservable().subscribe(actionEvent -> getHistory().push("/tester"));
    }

    @Override
    protected void bindPresentationModelWithLogic(BackendContainerPresentationModel pm) {
        super.bindPresentationModelWithLogic(pm);
        pm.bookingsButtonActionEventObservable().subscribe(actionEvent -> getHistory().push("/event/" + getParameter("eventId") + "/bookings"));
        pm.lettersButtonActionEventObservable().subscribe(actionEvent -> getHistory().push("/event/" + getParameter("eventId") + "/letters"));
    }
}
