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
    private final Button organizationsButton;
    private final Button eventsButton;
    private final Button bookingsButton;
    private final Button lettersButton;
    private final Button monitorButton;
    private final Button testerButton;

    public ContainerViewModel(VPage contentNode, Button backButton, Button forwardButton, Button organizationsButton, Button eventsButton, Button bookingsButton, Button lettersButton, Button monitorButton, Button testerButton) {
        this.contentNode = contentNode;
        this.backButton = backButton;
        this.forwardButton = forwardButton;
        this.organizationsButton = organizationsButton;
        this.eventsButton = eventsButton;
        this.bookingsButton = bookingsButton;
        this.lettersButton = lettersButton;
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

    Button getOrganizationsButton() {
        return organizationsButton;
    }

    public Button getEventsButton() {
        return eventsButton;
    }

    Button getBookingsButton() {
        return bookingsButton;
    }

    Button getLettersButton() {
        return lettersButton;
    }

    Button getMonitorButton() {
        return monitorButton;
    }

    Button getTesterButton() {
        return testerButton;
    }
}
