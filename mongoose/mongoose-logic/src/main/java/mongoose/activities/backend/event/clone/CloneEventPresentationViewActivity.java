package mongoose.activities.backend.event.clone;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import mongoose.activities.shared.logic.ui.theme.Theme;
import mongoose.domainmodel.format.DateFormatter;
import naga.framework.activity.presentation.view.impl.PresentationViewActivityImpl;
import naga.framework.ui.dialog.DialogUtil;
import naga.framework.ui.i18n.I18n;

import static javafx.scene.layout.Region.USE_PREF_SIZE;

/**
 * @author Bruno Salmon
 */
public class CloneEventPresentationViewActivity extends PresentationViewActivityImpl<CloneEventPresentationModel> {

    protected GridPane gp;
    protected TextField dateTextField;

    private StackPane stackPane;

    @Override
    protected void createViewNodes(CloneEventPresentationModel pm) {
        Label nameLabel = new Label(), dateLabel = new Label();
        TextField nameTextField = new TextField();
        dateTextField = new TextField();
        Button submitButton = new Button();
        gp = new GridPane();
        gp.add(nameLabel, 0, 0);
        gp.add(nameTextField, 1, 0);
        gp.add(dateLabel, 0, 1);
        gp.add(dateTextField, 1, 1);
        gp.add(submitButton, 1, 2);
        gp.setHgap(10);
        gp.setVgap(10);
        GridPane.setHalignment(nameLabel, HPos.RIGHT);
        GridPane.setHalignment(dateLabel, HPos.RIGHT);
        GridPane.setHalignment(submitButton, HPos.RIGHT);
        nameTextField.setPrefWidth(150d);
        dateTextField.setPrefWidth(150d);
        ColumnConstraints cc1 = new ColumnConstraints();
        cc1.setHgrow(Priority.NEVER);
        ColumnConstraints cc2 = new ColumnConstraints();
        cc2.setHgrow(Priority.ALWAYS);
        gp.getColumnConstraints().setAll(cc1, cc2);
        // Setting max width/height to pref width/height (otherwise the grid pane takes all space with cells in top left corner)
        gp.setMaxWidth(USE_PREF_SIZE);
        gp.setMaxHeight(USE_PREF_SIZE);
        gp.setPadding(new Insets(50, 50, 50, 50));

        nameLabel.textFillProperty().bind(Theme.dialogTextFillProperty());
        dateLabel.textFillProperty().bind(Theme.dialogTextFillProperty());

        Font font = Font.getDefault();
        nameLabel.setFont(font);
        dateLabel.setFont(font);
        nameTextField.setFont(font);
        dateTextField.setFont(font);
        submitButton.setFont(font);

        BorderPane bp = new BorderPane(gp);
        bp.backgroundProperty().bind(Theme.dialogBackgroundProperty());
        bp.borderProperty().bind(Theme.dialogBorderProperty());

        I18n i18n = getI18n();
        i18n.translateText(nameLabel, "Name");
        i18n.translateText(dateLabel, "Date");
        nameTextField.textProperty().bindBidirectional(pm.nameProperty());
        dateTextField.textProperty().bindBidirectional(pm.dateProperty(), DateFormatter.LOCAL_DATE_STRING_CONVERTER);
        i18n.translateText(submitButton, "Clone").onActionProperty().bind(pm.onSubmitProperty());

        stackPane = new StackPane();
        // Now that the grid pane doesn't take all space, we center it (if shown in a border pane which is very probable)
        DialogUtil.showModalNodeInGoldLayout(bp, stackPane);
    }

    @Override
    protected Node assemblyViewNodes() {
        return stackPane;
    }
}
