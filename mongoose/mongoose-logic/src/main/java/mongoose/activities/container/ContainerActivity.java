package mongoose.activities.container;

import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.layouts.VPage;
import naga.toolkit.spi.nodes.Parent;
import naga.framework.ui.presentation.PresentationActivity;

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
        Button monitorButton = toolkit.createButton();
        Button testerButton = toolkit.createButton();
        Parent header = toolkit.createHBox();
        header.getChildren().setAll(backButton, forwardButton, bookingsButton, organizationsButton, monitorButton, testerButton);
        VPage borderPane = toolkit.createVPage().setHeader(header);
        return new ContainerViewModel(borderPane, backButton, forwardButton, bookingsButton, organizationsButton, monitorButton, testerButton);
    }

    @Override
    protected void bindViewModelWithPresentationModel(ContainerViewModel vm, ContainerPresentationModel pm) {
        // Hard coded initialization
        vm.getBackButton().setText("<");
        vm.getForwardButton().setText(">");
        vm.getOrganizationsButton().setText("Organizations");
        vm.getBookingsButton().setText("Bookings");
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
    }

    @Override
    protected void bindPresentationModelWithLogic(ContainerPresentationModel pm) {
        pm.organizationsButtonActionEventObservable().subscribe(actionEvent -> getHistory().push("/organizations"));
        pm.bookingsButtonActionEventObservable().subscribe(actionEvent -> getHistory().push("/event/115/bookings"));
    }

}
