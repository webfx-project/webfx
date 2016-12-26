package mongoose.activities.backend.event.clone;

import mongoose.activities.shared.generic.EventDependentActivity;
import mongoose.domainmodel.format.DateFormatter;
import mongoose.entities.Event;
import naga.framework.ui.i18n.I18n;
import naga.platform.services.update.UpdateArgument;
import naga.platform.spi.Platform;
import naga.toolkit.fx.geometry.HPos;
import naga.toolkit.fx.geometry.Pos;
import naga.toolkit.fx.properties.Properties;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.scene.control.Label;
import naga.toolkit.fx.scene.control.TextField;
import naga.toolkit.fx.scene.layout.BorderPane;
import naga.toolkit.fx.scene.layout.ColumnConstraints;
import naga.toolkit.fx.scene.layout.GridPane;
import naga.toolkit.fx.scene.layout.Priority;

import java.time.LocalDate;

import static naga.toolkit.fx.scene.layout.PreferenceResizableNode.USE_PREF_SIZE;

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
        GridPane gridPane = new GridPane();
        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameTextField, 1, 0);
        gridPane.add(dateLabel, 0, 1);
        gridPane.add(dateTextField, 1, 1);
        gridPane.add(submitButton, 1, 2, 1 , 3);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        GridPane.setHalignment(nameLabel, HPos.RIGHT);
        GridPane.setHalignment(dateLabel, HPos.RIGHT);
        GridPane.setHalignment(submitButton, HPos.RIGHT);
        nameTextField.setMinWidth(150d);
        dateTextField.setMinWidth(150d);
        ColumnConstraints cc1 = new ColumnConstraints();
        cc1.setHgrow(Priority.NEVER);
        ColumnConstraints cc2 = new ColumnConstraints();
        cc2.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().setAll(cc1, cc2);
        // Setting max width/height to pref width/height (otherwise the grid pane takes all space with cells in top left corner)
        gridPane.setMaxWidth(USE_PREF_SIZE);
        gridPane.setMaxHeight(USE_PREF_SIZE);
        // Now that the grid pane doesn't take all space, we center it (if shown in a border pane which is very probable)
        BorderPane.setAlignment(gridPane, Pos.CENTER);
        return new CloneEventViewModel(gridPane, nameLabel, dateLabel, nameTextField, dateTextField, submitButton);
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
