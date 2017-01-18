package mongoose.activities.backend.container;

import mongoose.activities.shared.container.ContainerViewModel;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

/**
 * @author Bruno Salmon
 */
class BackendContainerViewModel extends ContainerViewModel {

    private final Button bookingsButton;
    private final Button lettersButton;
    private final Button monitorButton;
    private final Button testerButton;
    private final Button lightTheme;
    private final Button darkTheme;

    BackendContainerViewModel() {
        this(new BorderPane(), new Button(), new Button(), new Button(), new Button(), new Button(), new Button(), new Button(), new Button(), new Button(), new Button(), new Button(), new Button());
    }

    public BackendContainerViewModel(BorderPane contentNode, Button backButton, Button forwardButton, Button organizationsButton, Button eventsButton, Button englishButton, Button frenchButton, Button bookingsButton, Button lettersButton, Button monitorButton, Button testerButton, Button lightTheme, Button darkTheme) {
        super(contentNode, backButton, forwardButton, organizationsButton, eventsButton, englishButton, frenchButton);
        this.bookingsButton = bookingsButton;
        this.lettersButton = lettersButton;
        this.monitorButton = monitorButton;
        this.testerButton = testerButton;
        this.lightTheme = lightTheme;
        this.darkTheme = darkTheme;
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

    Button getLightTheme() {
        return lightTheme;
    }

    Button getDarkTheme() {
        return darkTheme;
    }
}
