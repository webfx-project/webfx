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
    private final Button englishButton;
    private final Button frenchButton;

    public ContainerViewModel(VPage contentNode, Button backButton, Button forwardButton, Button organizationsButton, Button eventsButton, Button englishButton, Button frenchButton) {
        super(contentNode);
        this.backButton = backButton;
        this.forwardButton = forwardButton;
        this.organizationsButton = organizationsButton;
        this.eventsButton = eventsButton;
        this.englishButton = englishButton;
        this.frenchButton = frenchButton;
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

    Button getEnglishButton() {
        return englishButton;
    }

    Button getFrenchButton() {
        return frenchButton;
    }
}
