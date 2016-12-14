package mongoose.activities.frontend.container;

import javafx.beans.property.Property;
import mongoose.activities.shared.container.ContainerViewModel;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.scene.layout.BorderPane;

/**
 * @author Bruno Salmon
 */
public class FrontendContainerViewModel extends ContainerViewModel {

    public FrontendContainerViewModel(BorderPane contentNode, Button backButton, Button forwardButton, Button organizationsButton, Button eventsButton, Button englishButton, Button frenchButton) {
        super(contentNode, backButton, forwardButton, organizationsButton, eventsButton, englishButton, frenchButton);
    }

    public FrontendContainerViewModel(BorderPane contentNode, Button backButton, Button forwardButton, Button organizationsButton, Button eventsButton, Button englishButton, Button frenchButton, Property<Node> mountNodeProperty) {
        super(contentNode, backButton, forwardButton, organizationsButton, eventsButton, englishButton, frenchButton, mountNodeProperty);
    }
}
