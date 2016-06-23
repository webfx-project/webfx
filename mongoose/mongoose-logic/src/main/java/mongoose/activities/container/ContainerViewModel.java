package mongoose.activities.container;

import naga.core.ui.presentation.ViewModel;
import naga.core.spi.toolkit.controls.Button;
import naga.core.spi.toolkit.layouts.VPage;

/**
 * @author Bruno Salmon
 */
class ContainerViewModel implements ViewModel {

    private final VPage contentNode;
    private final Button backButton;
    private final Button forwardButton;
    private final Button bookingsButton;
    private final Button organizationsButton;
    private final Button monitorButton;

    public ContainerViewModel(VPage contentNode, Button backButton, Button forwardButton, Button bookingsButton, Button organizationsButton, Button monitorButton) {
        this.contentNode = contentNode;
        this.backButton = backButton;
        this.forwardButton = forwardButton;
        this.bookingsButton = bookingsButton;
        this.organizationsButton = organizationsButton;
        this.monitorButton = monitorButton;
    }

    @Override
    public VPage getContentNode() {
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

    public Button getMonitorButton() {
        return monitorButton;
    }
}
