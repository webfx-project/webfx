package mongoose.activities.container;

import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.Button;

/**
 * @author Bruno Salmon
 */
public class ContainerActivity extends PresentationActivity<ContainerViewModel, ContainerPresentationModel> {

    public ContainerActivity() {
        super(ContainerPresentationModel::new);
    }

    @Override
    protected ContainerViewModel buildView(Toolkit toolkit) {
        Button backButton = toolkit.createButton();
        Button forwardButton = toolkit.createButton();
        Button organizationsButton = toolkit.createButton();
        Button bookingsButton = toolkit.createButton();
        Button lettersButton = toolkit.createButton();
        Button monitorButton = toolkit.createButton();
        Button testerButton = toolkit.createButton();
        return new ContainerViewModel(toolkit.createVPage().setHeader(toolkit.createHBox(backButton, forwardButton, organizationsButton, bookingsButton, lettersButton, monitorButton, testerButton)),
                backButton, forwardButton, bookingsButton, lettersButton, organizationsButton, monitorButton, testerButton);
    }

    @Override
    protected void bindViewModelWithPresentationModel(ContainerViewModel vm, ContainerPresentationModel pm) {
        // Hard coded initialization
        vm.getBackButton().setText("<");
        vm.getForwardButton().setText(">");
        vm.getOrganizationsButton().setText("Organizations");
        vm.getBookingsButton().setText("Bookings");
        vm.getLettersButton().setText("Letters");
        vm.getMonitorButton().setText("Monitor");
        vm.getTesterButton().setText("Tester");
        // Binding the mount node property so that child sub routed pages are displayed in the center
        vm.getContentNode().centerProperty().bind(mountNodeProperty());

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        vm.getBackButton().actionEventObservable().subscribe(actionEvent -> getHistory().goBack());
        vm.getForwardButton().actionEventObservable().subscribe(actionEvent -> getHistory().goForward());
        vm.getMonitorButton().actionEventObservable().subscribe(actionEvent -> getHistory().push("/monitor"));
        vm.getTesterButton().actionEventObservable().subscribe(actionEvent -> getHistory().push("/tester"));
        vm.getOrganizationsButton().actionEventObservable().subscribe(actionEvent -> pm.organizationsButtonActionEventObservable().onNext(actionEvent));
        vm.getBookingsButton().actionEventObservable().subscribe(actionEvent -> pm.bookingsButtonActionEventObservable().onNext(actionEvent));
        vm.getLettersButton().actionEventObservable().subscribe(actionEvent -> pm.lettersButtonActionEventObservable().onNext(actionEvent));
    }

    @Override
    protected void bindPresentationModelWithLogic(ContainerPresentationModel pm) {
        pm.organizationsButtonActionEventObservable().subscribe(actionEvent -> getHistory().push("/organizations"));
        pm.bookingsButtonActionEventObservable().subscribe(actionEvent -> getHistory().push("/event/" + getParameter("eventId") + "/bookings"));
        pm.lettersButtonActionEventObservable().subscribe(actionEvent -> getHistory().push("/event/" + getParameter("eventId") + "/letters"));
    }

}
