package mongoose.activities.shared.container;

import javafx.beans.property.Property;
import naga.framework.activity.client.HasMountNodeProperty;
import naga.framework.ui.presentation.AbstractViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
public class ContainerViewModel extends AbstractViewModel<VPage> implements HasMountNodeProperty {

    private final Button backButton;
    private final Button forwardButton;
    private final Button organizationsButton;
    private final Button eventsButton;
    private final Button englishButton;
    private final Button frenchButton;
    private final Property<GuiNode> mountNodeProperty;

    public ContainerViewModel(VPage contentNode, Button backButton, Button forwardButton, Button organizationsButton, Button eventsButton, Button englishButton, Button frenchButton) {
        this(contentNode, backButton, forwardButton, organizationsButton, eventsButton, englishButton, frenchButton, contentNode.centerProperty());
    }

    public ContainerViewModel(VPage contentNode, Button backButton, Button forwardButton, Button organizationsButton, Button eventsButton, Button englishButton, Button frenchButton, Property<GuiNode> mountNodeProperty) {
        super(contentNode);
        this.backButton = backButton;
        this.forwardButton = forwardButton;
        this.organizationsButton = organizationsButton;
        this.eventsButton = eventsButton;
        this.englishButton = englishButton;
        this.frenchButton = frenchButton;
        this.mountNodeProperty = mountNodeProperty;
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

    @Override
    public Property<GuiNode> mountNodeProperty() {
        return mountNodeProperty;
    }
}
