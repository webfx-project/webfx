package mongoose.activities.shared.container;

import mongoose.activities.frontend.container.FrontendContainerActivity;
import naga.commons.util.Arrays;
import naga.commons.util.function.Factory;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.scene.layout.BorderPane;
import naga.toolkit.fx.scene.layout.HBox;
import naga.toolkit.spi.Toolkit;


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
    protected VM buildView(Toolkit toolkit) {
        Button backButton = Button.create();
        Button forwardButton = Button.create();
        Button organizationsButton = Button.create();
        Button eventsButton = Button.create();
        Button englishButton = Button.create();
        Button frenchButton = Button.create();
        boolean isFrontend = this instanceof FrontendContainerActivity;
        return (VM) new ContainerViewModel(BorderPane.create(null, HBox.create(Arrays.nonNulls(Node[]::new, backButton, forwardButton, (Node) (isFrontend ? null : organizationsButton), (Node) (isFrontend ? null : eventsButton), englishButton, frenchButton)), null, null, null),
                backButton, forwardButton, organizationsButton, eventsButton, englishButton, frenchButton);
    }

    @Override
    protected void bindViewModelWithPresentationModel(VM vm, PM pm) {
        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        I18n i18n = getI18n();
        i18n.translateText(vm.getBackButton(), "<").setOnMouseClicked(event -> getHistory().goBack());
        i18n.translateText(vm.getForwardButton(), ">").setOnMouseClicked(event -> getHistory().goForward());
        i18n.translateText(vm.getOrganizationsButton(), "Organizations").setOnMouseClicked(event ->  getHistory().push("/organizations"));
        i18n.translateText(vm.getEventsButton(), "Events").setOnMouseClicked(event ->  getHistory().push("/events"));
        vm.getEnglishButton().setText("English");
        vm.getEnglishButton().setOnMouseClicked(event -> i18n.setLanguage("en"));
        vm.getFrenchButton().setText("Français");
        vm.getFrenchButton().setOnMouseClicked(event -> i18n.setLanguage("fr"));
    }

    @Override
    protected void bindPresentationModelWithLogic(PM pm) {
/*
        pm.organizationsButtonActionEventObservable().subscribe(actionEvent -> getHistory().push("/organizations"));
        pm.eventsButtonActionEventObservable().subscribe(actionEvent -> getHistory().push("/events"));
*/
    }

}
