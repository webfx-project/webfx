package mongoose.logic.container;

import naga.core.ngui.presentation.ViewModel;
import naga.core.spi.toolkit.nodes.ActionButton;
import naga.core.spi.toolkit.nodes.BorderPane;

/**
 * @author Bruno Salmon
 */
public class ContainerViewModel implements ViewModel {

    private final BorderPane contentNode;
    private final ActionButton organizationsButton;
    private final ActionButton cartButton;

    public ContainerViewModel(BorderPane contentNode, ActionButton organizationsButton, ActionButton cartButton) {
        this.contentNode = contentNode;
        this.organizationsButton = organizationsButton;
        this.cartButton = cartButton;
    }

    @Override
    public BorderPane getContentNode() {
        return contentNode;
    }

    public ActionButton getOrganizationsButton() {
        return organizationsButton;
    }

    public ActionButton getCartButton() {
        return cartButton;
    }
}
