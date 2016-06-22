package mongoose.activities.container;

import naga.core.ui.presentation.ViewModel;
import naga.core.spi.toolkit.nodes.Button;
import naga.core.spi.toolkit.nodes.BorderPane;

/**
 * @author Bruno Salmon
 */
class ContainerViewModel implements ViewModel {

    private final BorderPane contentNode;
    private final Button backButton;
    private final Button forwardButton;
    private final Button bookingsButton;
    private final Button organizationsButton;

    ContainerViewModel(BorderPane contentNode, Button backButton, Button forwardButton, Button bookingsButton, Button organizationsButton) {
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

    Button getBackButton() {
        return backButton;
    }

    Button getForwardButton() {
        return forwardButton;
    }

    Button getBookingsButton() {
        return bookingsButton;
    }

    Button getOrganizationsButton() {
        return organizationsButton;
    }

}
