package mongoose.activities.container;

import naga.core.ui.presentation.ViewModel;
import naga.core.spi.toolkit.nodes.ActionButton;
import naga.core.spi.toolkit.nodes.BorderPane;

/**
 * @author Bruno Salmon
 */
public class ContainerViewModel implements ViewModel {

    private final BorderPane contentNode;
    private final ActionButton bookingsButton;
    private final ActionButton organizationsButton;

    public ContainerViewModel(BorderPane contentNode, ActionButton bookingsButton, ActionButton organizationsButton) {
        this.contentNode = contentNode;
        this.organizationsButton = organizationsButton;
        this.bookingsButton = bookingsButton;
    }

    @Override
    public BorderPane getContentNode() {
        return contentNode;
    }

    public ActionButton getOrganizationsButton() {
        return organizationsButton;
    }

    public ActionButton getBookingsButton() {
        return bookingsButton;
    }
}
