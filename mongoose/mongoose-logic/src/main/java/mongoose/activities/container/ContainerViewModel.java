package mongoose.activities.container;

import naga.core.ui.presentation.ViewModel;
import naga.core.spi.toolkit.nodes.ActionButton;
import naga.core.spi.toolkit.nodes.BorderPane;

/**
 * @author Bruno Salmon
 */
class ContainerViewModel implements ViewModel {

    private final BorderPane contentNode;
    private final ActionButton backButton;
    private final ActionButton forwardButton;
    private final ActionButton bookingsButton;
    private final ActionButton organizationsButton;

    ContainerViewModel(BorderPane contentNode, ActionButton backButton, ActionButton forwardButton, ActionButton bookingsButton, ActionButton organizationsButton) {
        this.contentNode = contentNode;
        this.backButton = backButton;
        this.forwardButton = forwardButton;
        this.bookingsButton = bookingsButton;
        this.organizationsButton = organizationsButton;
    }

    @Override
    public BorderPane getContentNode() {
        return contentNode;
    }

    ActionButton getBackButton() {
        return backButton;
    }

    ActionButton getForwardButton() {
        return forwardButton;
    }

    ActionButton getBookingsButton() {
        return bookingsButton;
    }

    ActionButton getOrganizationsButton() {
        return organizationsButton;
    }

}
