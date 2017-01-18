package mongoose.activities.shared.container;

import javafx.beans.property.Property;
import naga.framework.activity.client.HasMountNodeProperty;
import naga.framework.ui.presentation.AbstractViewModel;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

/**
 * @author Bruno Salmon
 */
public class ContainerViewModel extends AbstractViewModel<BorderPane> implements HasMountNodeProperty {

    private final Button backButton;
    private final Button forwardButton;
    private final Button organizationsButton;
    private final Button eventsButton;
    private final Button englishButton;
    private final Button frenchButton;
    private final Property<Node> mountNodeProperty;

    public ContainerViewModel(BorderPane contentNode, Button backButton, Button forwardButton, Button organizationsButton, Button eventsButton, Button englishButton, Button frenchButton) {
        this(contentNode, backButton, forwardButton, organizationsButton, eventsButton, englishButton, frenchButton, contentNode.centerProperty());
    }

    public ContainerViewModel(BorderPane contentNode, Button backButton, Button forwardButton, Button organizationsButton, Button eventsButton, Button englishButton, Button frenchButton, Property<Node> mountNodeProperty) {
        super(contentNode);
        this.backButton = backButton;
        this.forwardButton = forwardButton;
        this.organizationsButton = organizationsButton;
        this.eventsButton = eventsButton;
        this.englishButton = englishButton;
        this.frenchButton = frenchButton;
        this.mountNodeProperty = mountNodeProperty;
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getForwardButton() {
        return forwardButton;
    }

    public Button getOrganizationsButton() {
        return organizationsButton;
    }

    public Button getEventsButton() {
        return eventsButton;
    }

    public Button getEnglishButton() {
        return englishButton;
    }

    public Button getFrenchButton() {
        return frenchButton;
    }

    @Override
    public Property<Node> mountNodeProperty() {
        return mountNodeProperty;
    }
}
