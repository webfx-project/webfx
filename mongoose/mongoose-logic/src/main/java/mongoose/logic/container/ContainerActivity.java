package mongoose.logic.container;

import naga.core.ngui.presentation.PresentationActivity;
import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.nodes.ActionButton;
import naga.core.spi.toolkit.nodes.BorderPane;
import naga.core.spi.toolkit.nodes.VBox;

/**
 * @author Bruno Salmon
 */
public class ContainerActivity extends PresentationActivity<ContainerViewModel, ContainerPresentationModel> {

    public ContainerActivity() {
        setPresentationModelFactory(ContainerPresentationModel::new);
    }

    @Override
    protected ContainerViewModel buildView(Toolkit toolkit) {
        ActionButton organizationsButton = toolkit.createNode(ActionButton.class);
        ActionButton cartButton = toolkit.createNode(ActionButton.class);
        VBox header = toolkit.createNode(VBox.class);
        header.getChildren().setAll(organizationsButton, cartButton);
        BorderPane borderPane = toolkit.createNode(BorderPane.class).setTop(header);
        return new ContainerViewModel(borderPane, organizationsButton, cartButton);
    }

    @Override
    protected void bindViewModelWithPresentationModel(ContainerViewModel vm, ContainerPresentationModel pm) {
        // Hard coded initialization
        vm.getOrganizationsButton().setText("Organizations");
        vm.getCartButton().setText("Cart");

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        vm.getOrganizationsButton().actionEventObservable().subscribe(actionEvent -> pm.organizationButtonActionEventObservable().onNext(actionEvent));
        vm.getCartButton().actionEventObservable().subscribe(actionEvent -> pm.carButtonActionEventObservable().onNext(actionEvent));

        vm.getContentNode().centerProperty().bind(getActivityContext().mountNodeProperty());
    }

    @Override
    protected void bindPresentationModelWithLogic(ContainerPresentationModel pm) {
        pm.organizationButtonActionEventObservable().subscribe(actionEvent -> getActivityContext().getActivityRouter().getHistory().push("/organizations"));
        pm.carButtonActionEventObservable().subscribe(actionEvent -> getActivityContext().getActivityRouter().getHistory().push("/cart/.570d0151724b2."));
    }

}
