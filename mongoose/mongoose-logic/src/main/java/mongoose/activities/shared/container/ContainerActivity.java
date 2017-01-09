package mongoose.activities.shared.container;

import mongoose.activities.frontend.container.FrontendContainerActivity;
import naga.commons.util.Arrays;
import naga.commons.util.function.Factory;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.presentation.PresentationActivity;
import naga.fx.scene.Node;
import naga.fx.scene.control.Button;
import naga.fx.scene.layout.BorderPane;
import naga.fx.scene.layout.HBox;


/**
 * @author Bruno Salmon
 */
public class ContainerActivity<VM extends ContainerViewModel, PM extends ContainerPresentationModel> extends PresentationActivity<VM, PM> {

    public ContainerActivity() {
        this(() -> (PM) new ContainerPresentationModel());
    }

    public ContainerActivity(Factory<PM> presentationModelFactory) {
        super(presentationModelFactory);
    }

    @Override
    protected VM buildView() {
        Button backButton = new Button();
        Button forwardButton = new Button();
        Button organizationsButton = new Button();
        Button eventsButton = new Button();
        Button englishButton = new Button();
        Button frenchButton = new Button();
        boolean isFrontend = this instanceof FrontendContainerActivity;
        Node[] children = Arrays.nonNulls(Node[]::new, backButton, forwardButton, isFrontend ? null : organizationsButton, (isFrontend ? null : eventsButton), englishButton, frenchButton);
        Node top = new HBox(children);
        return (VM) new ContainerViewModel(new BorderPane(null, top, null, null, null),
                backButton, forwardButton, organizationsButton, eventsButton, englishButton, frenchButton);
    }

    @Override
    protected void bindViewModelWithPresentationModel(VM vm, PM pm) {
        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        I18n i18n = getI18n();
        i18n.translateText(vm.getBackButton(), "<").setOnAction(event -> getHistory().goBack());
        i18n.translateText(vm.getForwardButton(), ">").setOnAction(event -> getHistory().goForward());
        i18n.translateText(vm.getOrganizationsButton(), "Organizations").setOnAction(event ->  getHistory().push("/organizations"));
        i18n.translateText(vm.getEventsButton(), "Events").setOnAction(event ->  getHistory().push("/events"));
        vm.getEnglishButton().setText("English");
        vm.getEnglishButton().setOnAction(event -> i18n.setLanguage("en"));
        vm.getFrenchButton().setText("Français");
        vm.getFrenchButton().setOnAction(event -> i18n.setLanguage("fr"));
    }

    @Override
    protected void bindPresentationModelWithLogic(PM pm) {
/*
        pm.organizationsButtonActionEventObservable().subscribe(actionEvent -> getHistory().push("/organizations"));
        pm.eventsButtonActionEventObservable().subscribe(actionEvent -> getHistory().push("/events"));
*/
    }

}
