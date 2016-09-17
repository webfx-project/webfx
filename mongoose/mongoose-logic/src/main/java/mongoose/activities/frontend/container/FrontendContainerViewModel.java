package mongoose.activities.frontend.container;

import mongoose.activities.shared.container.ContainerViewModel;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
public class FrontendContainerViewModel extends ContainerViewModel {

    public FrontendContainerViewModel(VPage contentNode, Button backButton, Button forwardButton, Button organizationsButton, Button eventsButton) {
        super(contentNode, backButton, forwardButton, organizationsButton, eventsButton);
    }
}
