package mongoose.backend.activities.cloneevent;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import mongoose.client.activity.themes.Theme;
import mongoose.shared.domainmodel.formatters.DateFormatter;
import webfx.framework.client.activity.impl.elementals.presentation.view.impl.PresentationViewActivityImpl;
import webfx.framework.client.ui.controls.button.ButtonFactoryMixin;
import webfx.framework.client.ui.controls.dialog.DialogUtil;

import static javafx.scene.layout.Region.USE_PREF_SIZE;

/**
 * @author Bruno Salmon
 */
public class CloneEventPresentationViewActivity
        extends PresentationViewActivityImpl<CloneEventPresentationModel>
        implements ButtonFactoryMixin {

    protected GridPane gp;
    protected TextField dateTextField;

    private StackPane stackPane;

    @Override
    protected void createViewNodes(CloneEventPresentationModel pm) {
        Label nameLabel = newLabel("Name"), dateLabel = newLabel("Date");
        TextField nameTextField = newTextField();
        dateTextField = newTextField();
        Button submitButton = newButton("Clone");
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

        nameTextField.textProperty().bindBidirectional(pm.nameProperty());
        dateTextField.textProperty().bindBidirectional(pm.dateProperty(), DateFormatter.SINGLETON.toStringConverter());
        submitButton.onActionProperty().bind(pm.onSubmitProperty());

        stackPane = new StackPane();
        // Now that the grid pane doesn't take all space, we center it (if shown in a border pane which is very probable)
        DialogUtil.showModalNodeInGoldLayout(bp, stackPane);
    }

    @Override
    protected Node assemblyViewNodes() {
        return stackPane;
    }
}
