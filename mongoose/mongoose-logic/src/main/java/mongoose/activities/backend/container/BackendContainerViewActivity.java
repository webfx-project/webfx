package mongoose.activities.backend.container;

import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import mongoose.activities.shared.container.SharedContainerViewActivity;
import mongoose.activities.shared.logic.ui.theme.Theme;

/**
 * @author Bruno Salmon
 */
public class BackendContainerViewActivity extends SharedContainerViewActivity {

    @Override
    public Node buildUi() {
        super.buildUi();

        borderPane.setTop(new FlowPane(backButton, forwardButton
                , newButton( "Organizations", () ->  getHistory().push("/organizations"))
                , newButton("Events", () -> getHistory().push("/events"))
                , newButton("Bookings", () -> getHistory().push("/documents/event/" + getParameter("eventId")))
                , newButton("Letters", () -> getHistory().push("/event/" + getParameter("eventId") + "/letters"))
                , newButton("Monitor", () -> getHistory().push("/monitor"))
                , newButton("Tester", () -> getHistory().push("/tester"))
                , englishButton
                , frenchButton
                //, newButton("Light", () -> new LightTheme().apply())
                //, newButton("Dark", () -> new DarkTheme().apply())
        ));

        borderPane.backgroundProperty().bind(Theme.mainBackgroundProperty());
        return borderPane;
    }
}
