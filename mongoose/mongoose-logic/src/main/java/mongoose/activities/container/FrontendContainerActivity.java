package mongoose.activities.container;

import naga.commons.util.function.Factory;
import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.Button;

/**
 * @author Bruno Salmon
 */
public class FrontendContainerActivity<VM extends FrontendContainerViewModel, PM extends FrontendContainerPresentationModel> extends PresentationActivity<VM, PM> {

    public FrontendContainerActivity() {
        this(() -> (PM) new FrontendContainerPresentationModel());
    }

    public FrontendContainerActivity(Factory<PM> presentationModelFactory) {
        super(presentationModelFactory);
    }

    @Override
    protected VM buildView(Toolkit toolkit) {
        Button backButton = toolkit.createButton();
        Button forwardButton = toolkit.createButton();
        Button organizationsButton = toolkit.createButton();
        Button eventsButton = toolkit.createButton();
        return (VM) new FrontendContainerViewModel(toolkit.createVPage().setHeader(toolkit.createHBox(backButton, forwardButton, organizationsButton, eventsButton)),
                backButton, forwardButton, organizationsButton, eventsButton);
    }

    @Override
    protected void bindViewModelWithPresentationModel(VM vm, PM pm) {
        // Hard coded initialization
        vm.getBackButton().setText("<");
        vm.getForwardButton().setText(">");
        vm.getOrganizationsButton().setText("Organizations");
        vm.getEventsButton().setText("Events");
        // Binding the mount node property so that child sub routed pages are displayed in the center
        vm.getContentNode().centerProperty().bind(mountNodeProperty());

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        vm.getBackButton().actionEventObservable().subscribe(actionEvent -> getHistory().goBack());
        vm.getForwardButton().actionEventObservable().subscribe(actionEvent -> getHistory().goForward());
        vm.getOrganizationsButton().actionEventObservable().subscribe(actionEvent -> pm.organizationsButtonActionEventObservable().onNext(actionEvent));
        vm.getEventsButton().actionEventObservable().subscribe(actionEvent -> pm.eventsButtonActionEventObservable().onNext(actionEvent));
    }

    @Override
    protected void bindPresentationModelWithLogic(PM pm) {
        pm.organizationsButtonActionEventObservable().subscribe(actionEvent -> getHistory().push("/organizations"));
        pm.eventsButtonActionEventObservable().subscribe(actionEvent -> getHistory().push("/events"));
    }

}
