package mongoose.activities.shared.container;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import mongoose.activities.frontend.container.FrontendContainerViewActivity;
import naga.commons.util.Arrays;
import naga.framework.activity.view.impl.ViewActivityImpl;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public class ContainerViewActivity extends ViewActivityImpl {

    protected Button backButton;
    protected Button forwardButton;
    protected Button organizationsButton;
    protected Button eventsButton;
    protected Button englishButton;
    protected Button frenchButton;
    protected BorderPane borderPane;

    @Override
    public Node buildUi() {
        I18n i18n = getI18n();
        backButton = i18n.translateText(new Button(), "<");
        forwardButton = i18n.translateText(new Button(), ">");
        organizationsButton = i18n.translateText(new Button(), "Organizations");
        eventsButton = i18n.translateText(new Button(), "Events");
        englishButton = new Button("English");
        frenchButton = new Button("Français");

        backButton.setOnAction(event -> getHistory().goBack());
        forwardButton.setOnAction(event -> getHistory().goForward());
        organizationsButton.setOnAction(event ->  getHistory().push("/organizations"));
        eventsButton.setOnAction(event ->  getHistory().push("/events"));
        englishButton.setOnAction(event -> i18n.setLanguage("en"));
        frenchButton.setOnAction(event -> i18n.setLanguage("fr"));

        boolean isFrontend = this instanceof FrontendContainerViewActivity;
        Node[] children = Arrays.nonNulls(Node[]::new, backButton, forwardButton, isFrontend ? null : organizationsButton, (isFrontend ? null : eventsButton), englishButton, frenchButton);
        Node top = new HBox(children);
        borderPane = new BorderPane(null, top, null, null, null);
        return borderPane;
    }
}
