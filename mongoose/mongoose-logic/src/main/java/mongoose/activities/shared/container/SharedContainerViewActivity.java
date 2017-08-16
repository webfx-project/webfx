package mongoose.activities.shared.container;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import mongoose.activities.shared.generic.MongooseButtonFactoryMixin;
import naga.framework.activity.view.impl.ViewActivityImpl;

/**
 * @author Bruno Salmon
 */
public class SharedContainerViewActivity extends ViewActivityImpl implements MongooseButtonFactoryMixin {

    protected Button backButton;
    protected Button forwardButton;
    protected Button englishButton;
    protected Button frenchButton;
    protected BorderPane borderPane;

    @Override
    public Node buildUi() {
        backButton = newButton("<<", () -> getHistory().goBack());
        forwardButton = newButton(">>", () -> getHistory().goForward());
        englishButton = newButton("English", () -> setLanguage("en"));
        frenchButton = newButton("Français", () -> setLanguage("fr"));

        borderPane = new BorderPane(null, new HBox(backButton, forwardButton, englishButton, frenchButton), null, null, null);
        borderPane.centerProperty().bind(mountNodeProperty());

        return borderPane;
    }
}
