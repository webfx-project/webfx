package mongoose.activities.backend.event.clone;

import mongoose.activities.shared.generic.EventDependentActivity;
import mongoose.domainmodel.format.DateFormatter;
import mongoose.entities.Event;
import naga.framework.ui.i18n.I18n;
import naga.platform.services.update.UpdateArgument;
import naga.platform.spi.Platform;
import naga.fx.geometry.HPos;
import naga.fx.geometry.Insets;
import naga.fx.geometry.Pos;
import naga.fx.properties.Properties;
import naga.fx.scene.control.Button;
import naga.fx.scene.control.Label;
import naga.fx.scene.control.TextField;
import naga.fx.scene.layout.*;
import naga.fx.scene.paint.Color;

import java.time.LocalDate;

import static naga.fx.scene.layout.PreferenceResizableNode.USE_PREF_SIZE;

/**
 * @author Bruno Salmon
 */
public class CloneEventActivity extends EventDependentActivity<CloneEventViewModel, CloneEventPresentationModel> {

    public CloneEventActivity() {
        super(CloneEventPresentationModel::new);
    }

    @Override
    protected CloneEventViewModel buildView() {
        Label nameLabel = new Label(), dateLabel = new Label();
        TextField nameTextField = new TextField(), dateTextField = new TextField();
        Button submitButton = new Button();
        GridPane gp = new GridPane();
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
        gp.setInsets(new Insets(50, 50, 50, 50));

        nameLabel.setTextFill(Color.WHITE);
        dateLabel.setTextFill(Color.WHITE);

/*
        Font font = Font.getDefault();
        nameLabel.setFont(font);
        dateLabel.setFont(font);
        nameTextField.setFont(font);
        dateTextField.setFont(font);
        submitButton.setFont(font);
*/

        BorderPane bp = new BorderPane(gp);
        bp.setBackground(new Background(new BackgroundFill(Color.grayRgb(42), new CornerRadii(10), null)));
        bp.setBorder(new Border(new BorderStroke(Color.rgb(237, 162, 57), BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THICK)));
        bp.setMaxWidth(USE_PREF_SIZE);
        bp.setMaxHeight(USE_PREF_SIZE);

        // Now that the grid pane doesn't take all space, we center it (if shown in a border pane which is very probable)
        GridPane goldPane = new GridPane();
        goldPane.setBackground(new Background(new BackgroundFill(Color.web("#101214"), null, null)));
        goldPane.setAlignment(Pos.TOP_CENTER);
        RowConstraints rc = new RowConstraints();
        rc.prefHeightProperty().bind(Properties.combine(goldPane.heightProperty(), bp.heightProperty(),
                (gpHeight, bpHeight) -> (gpHeight - bpHeight) * 100 / 261));
        Properties.runOnceOnPropertiesChange((p) -> goldPane.layout(), goldPane.heightProperty());
        goldPane.getRowConstraints().add(rc);
        goldPane.add(bp, 0, 1);

        return new CloneEventViewModel(goldPane, nameLabel, dateLabel, nameTextField, dateTextField, submitButton);
    }

    @Override
    protected void bindViewModelWithPresentationModel(CloneEventViewModel vm, CloneEventPresentationModel pm) {
        I18n i18n = getI18n();
        i18n.translateText(vm.getNameLabel(), "Name");
        i18n.translateText(vm.getDateLabel(), "Date");
        vm.getNameTextField().textProperty().bindBidirectional(pm.nameProperty());
        vm.getDateTextField().textProperty().bindBidirectional(pm.dateProperty());
        i18n.translateText(vm.getSubmitButton(), "Submit").setOnMouseClicked(event -> {
            String date = pm.getDate();
            int p;
            int dayOfMonth = Integer.parseInt(date.substring(0, p = date.indexOf('/')));
            int month = Integer.parseInt(date.substring(p + 1, p = date.indexOf('/', p + 1)));
            int year = Integer.parseInt(date.substring(p + 1));
            LocalDate startDate = LocalDate.of(year, month, dayOfMonth);
            Platform.getUpdateService().executeUpdate(new UpdateArgument("select copy_event(?,?,?)", new Object[]{getEventId(), pm.getName(), startDate}, true, getDataSourceModel().getId())).setHandler(ar -> {
                if (ar.succeeded())
                    getHistory().push("/event/" + ar.result().getGeneratedKeys()[0] + "/bookings");
            });
        });
    }

    @Override
    protected void bindPresentationModelWithLogic(CloneEventPresentationModel pm) {
        // Load and display fees groups now but also on event change
        Properties.runNowAndOnPropertiesChange(property -> {
            pm.setName(null);
            pm.setDate(null);
            onEventOptions().setHandler(ar -> {
                if (ar.succeeded()) {
                    Event event = getEvent();
                    pm.setName(event.getName());
                    pm.setDate(DateFormatter.SINGLETON.format(event.getStartDate()).toString());
                }
            });
        }, pm.eventIdProperty());
    }
}
