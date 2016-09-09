package mongoose.activities.container;

import naga.framework.ui.presentation.ViewModel;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
class FrontendContainerViewModel implements ViewModel {

    private final VPage contentNode;
    private final Button backButton;
    private final Button forwardButton;
    private final Button organizationsButton;
    private final Button eventsButton;

    public FrontendContainerViewModel(VPage contentNode, Button backButton, Button forwardButton, Button organizationsButton, Button eventsButton) {
        this.contentNode = contentNode;
        this.backButton = backButton;
        this.forwardButton = forwardButton;
        this.organizationsButton = organizationsButton;
        this.eventsButton = eventsButton;
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

    Button getEventsButton() {
        return eventsButton;
    }
}
