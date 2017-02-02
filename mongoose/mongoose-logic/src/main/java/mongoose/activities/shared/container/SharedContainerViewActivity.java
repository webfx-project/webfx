package mongoose.activities.shared.container;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import naga.framework.activity.view.impl.ViewActivityImpl;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public class SharedContainerViewActivity extends ViewActivityImpl {

    protected Button backButton;
    protected Button forwardButton;
    protected Button englishButton;
    protected Button frenchButton;
    protected BorderPane borderPane;

    @Override
    public Node buildUi() {
        I18n i18n = getI18n();
        backButton = i18n.translateText(new Button(), "<");
        forwardButton = i18n.translateText(new Button(), ">");
        englishButton = new Button("English");
        frenchButton = new Button("Français");

        backButton.setOnAction(event -> getHistory().goBack());
        forwardButton.setOnAction(event -> getHistory().goForward());
        englishButton.setOnAction(event -> i18n.setLanguage("en"));
        frenchButton.setOnAction(event -> i18n.setLanguage("fr"));

        borderPane = new BorderPane(null, new HBox(backButton, forwardButton, englishButton, frenchButton), null, null, null);
        borderPane.centerProperty().bind(mountNodeProperty());

        return borderPane;
    }
}
