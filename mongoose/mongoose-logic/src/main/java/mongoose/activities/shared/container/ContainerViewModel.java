package mongoose.activities.shared.container;

import naga.framework.ui.presentation.AbstractViewModel;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
public class ContainerViewModel extends AbstractViewModel<VPage> {

    private final Button backButton;
    private final Button forwardButton;
    private final Button organizationsButton;
    private final Button eventsButton;

    public ContainerViewModel(VPage contentNode, Button backButton, Button forwardButton, Button organizationsButton, Button eventsButton) {
        super(contentNode);
        this.backButton = backButton;
        this.forwardButton = forwardButton;
        this.organizationsButton = organizationsButton;
        this.eventsButton = eventsButton;
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
