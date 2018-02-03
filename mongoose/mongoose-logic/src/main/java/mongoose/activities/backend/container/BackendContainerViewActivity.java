package mongoose.activities.backend.container;

import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import mongoose.activities.backend.event.bookings.BookingsRouting;
import mongoose.activities.backend.event.letters.LettersRouting;
import mongoose.activities.backend.events.EventsRouting;
import mongoose.activities.backend.monitor.MonitorRooting;
import mongoose.activities.backend.organizations.OrganizationsRouting;
import mongoose.activities.backend.tester.TesterRooting;
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
                , newButton( "Organizations", () -> OrganizationsRouting.route(getHistory()))
                , newButton("Events", () -> EventsRouting.route(getHistory()))
                , newButton("Bookings", () -> BookingsRouting.routeUsingEventId(getParameter("eventId"), getHistory()))
                , newButton("Letters", () -> LettersRouting.routeUsingEventId(getParameter("eventId"), getHistory()))
                , newButton("Monitor", () -> MonitorRooting.route(getHistory()))
                , newButton("Tester", () -> TesterRooting.route(getHistory()))
                , englishButton
                , frenchButton
                //, newButton("Light", () -> new LightTheme().apply())
                //, newButton("Dark", () -> new DarkTheme().apply())
        ));

        borderPane.backgroundProperty().bind(Theme.mainBackgroundProperty());
        return borderPane;
    }
}
