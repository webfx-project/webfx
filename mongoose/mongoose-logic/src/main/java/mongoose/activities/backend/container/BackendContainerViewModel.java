package mongoose.activities.backend.container;

import mongoose.activities.shared.container.ContainerViewModel;
import naga.fx.scene.control.Button;
import naga.fx.scene.layout.BorderPane;

/**
 * @author Bruno Salmon
 */
class BackendContainerViewModel extends ContainerViewModel {

    private final Button bookingsButton;
    private final Button lettersButton;
    private final Button monitorButton;
    private final Button testerButton;

    public BackendContainerViewModel(BorderPane contentNode, Button backButton, Button forwardButton, Button organizationsButton, Button eventsButton, Button englishButton, Button frenchButton, Button bookingsButton, Button lettersButton, Button monitorButton, Button testerButton) {
        super(contentNode, backButton, forwardButton, organizationsButton, eventsButton, englishButton, frenchButton);
        this.bookingsButton = bookingsButton;
        this.lettersButton = lettersButton;
        this.monitorButton = monitorButton;
        this.testerButton = testerButton;
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
