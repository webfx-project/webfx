package mongoose.activities.frontend.container;

import javafx.beans.property.Property;
import mongoose.activities.shared.container.ContainerViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
public class FrontendContainerViewModel extends ContainerViewModel {

    public FrontendContainerViewModel(VPage contentNode, Button backButton, Button forwardButton, Button organizationsButton, Button eventsButton, Button englishButton, Button frenchButton) {
        super(contentNode, backButton, forwardButton, organizationsButton, eventsButton, englishButton, frenchButton);
    }

    public FrontendContainerViewModel(VPage contentNode, Button backButton, Button forwardButton, Button organizationsButton, Button eventsButton, Button englishButton, Button frenchButton, Property<GuiNode> mountNodeProperty) {
        super(contentNode, backButton, forwardButton, organizationsButton, eventsButton, englishButton, frenchButton, mountNodeProperty);
    }
}
