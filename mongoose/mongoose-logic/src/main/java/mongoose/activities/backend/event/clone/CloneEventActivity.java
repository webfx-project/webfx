package mongoose.activities.backend.event.clone;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import mongoose.activities.shared.generic.EventDependentActivity;
import mongoose.activities.shared.theme.Theme;
import mongoose.domainmodel.format.DateFormatter;
import mongoose.entities.Event;
import naga.framework.ui.i18n.I18n;
import naga.fx.properties.Properties;
import naga.platform.services.update.UpdateArgument;
import naga.platform.spi.Platform;

import java.time.LocalDate;

import static javafx.scene.layout.Region.USE_PREF_SIZE;

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
        bp.setMaxWidth(USE_PREF_SIZE);
        bp.setMaxHeight(USE_PREF_SIZE);

        // Now that the grid pane doesn't take all space, we center it (if shown in a border pane which is very probable)
        GridPane goldPane = new GridPane();
        //goldPane.backgroundProperty().bind(Theme.mainBackgroundProperty());
        goldPane.setAlignment(Pos.TOP_CENTER);
        RowConstraints rc = new RowConstraints();
        rc.prefHeightProperty().bind(Properties.combine(goldPane.heightProperty(), bp.heightProperty(),
                (gpHeight, bpHeight) -> (gpHeight.doubleValue() - bpHeight.doubleValue()) / 2.61));
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
        vm.getDateTextField().textProperty().bindBidirectional(pm.dateProperty(), new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return (String) DateFormatter.SINGLETON.format(date);
            }

            @Override
            public LocalDate fromString(String date) {
                if (date == null)
                    return null;
                int p;
                int dayOfMonth = Integer.parseInt(date.substring(0, p = date.indexOf('/')));
                int month = Integer.parseInt(date.substring(p + 1, p = date.indexOf('/', p + 1)));
                int year = Integer.parseInt(date.substring(p + 1));
                return LocalDate.of(year, month, dayOfMonth);
            }
        });
        i18n.translateText(vm.getSubmitButton(), "Submit").setOnAction(event -> {
            LocalDate startDate = pm.getDate();
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
                    pm.setDate(event.getStartDate());
                }
            });
        }, pm.eventIdProperty());
    }
}
