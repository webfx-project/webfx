package mongoose.activities.container;

import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
class BackendContainerViewModel extends FrontendContainerViewModel {

    private final Button bookingsButton;
    private final Button lettersButton;
    private final Button monitorButton;
    private final Button testerButton;

    BackendContainerViewModel(VPage contentNode, Button backButton, Button forwardButton, Button organizationsButton, Button eventsButton, Button bookingsButton, Button lettersButton, Button monitorButton, Button testerButton) {
        super(contentNode, backButton, forwardButton, organizationsButton, eventsButton);
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
