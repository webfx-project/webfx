package mongoose.activities.container;

import naga.framework.ui.presentation.ViewModel;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.layouts.VPage;

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
    private final Button testerButton;

    public ContainerViewModel(VPage contentNode, Button backButton, Button forwardButton, Button bookingsButton, Button organizationsButton, Button monitorButton, Button testerButton) {
        this.contentNode = contentNode;
        this.backButton = backButton;
        this.forwardButton = forwardButton;
        this.bookingsButton = bookingsButton;
        this.organizationsButton = organizationsButton;
        this.monitorButton = monitorButton;
        this.testerButton = testerButton;
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

    public Button getTesterButton() {
        return testerButton;
    }
}
