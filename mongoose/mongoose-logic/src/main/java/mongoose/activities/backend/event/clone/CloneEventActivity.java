package mongoose.activities.backend.event.clone;

import mongoose.activities.shared.generic.EventDependentActivity;
import mongoose.domainmodel.format.DateFormatter;
import mongoose.entities.Event;
import naga.framework.ui.i18n.I18n;
import naga.platform.services.update.UpdateArgument;
import naga.platform.spi.Platform;
import naga.toolkit.fx.geometry.HPos;
import naga.toolkit.fx.geometry.Insets;
import naga.toolkit.fx.geometry.VPos;
import naga.toolkit.fx.properties.Properties;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.scene.control.Label;
import naga.toolkit.fx.scene.control.TextField;
import naga.toolkit.fx.scene.layout.*;
import naga.toolkit.fx.scene.paint.Color;

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
        GridPane gp = new GridPane();
        gp.add(nameLabel, 0, 0);
        gp.add(nameTextField, 1, 0);
        gp.add(dateLabel, 0, 1);
        gp.add(dateTextField, 1, 1);
        gp.add(submitButton, 1, 2, 1 , 3);
        gp.setHgap(10);
        gp.setVgap(10);
        GridPane.setHalignment(nameLabel, HPos.RIGHT);
        GridPane.setHalignment(dateLabel, HPos.RIGHT);
        GridPane.setHalignment(submitButton, HPos.RIGHT);
        nameTextField.setMinWidth(150d);
        dateTextField.setMinWidth(150d);
        ColumnConstraints cc1 = new ColumnConstraints();
        cc1.setHgrow(Priority.NEVER);
        ColumnConstraints cc2 = new ColumnConstraints();
        cc2.setHgrow(Priority.ALWAYS);
        gp.getColumnConstraints().setAll(cc1, cc2);
        // Setting max width/height to pref width/height (otherwise the grid pane takes all space with cells in top left corner)
        gp.setMaxWidth(USE_PREF_SIZE);
        gp.setMaxHeight(USE_PREF_SIZE);
        gp.setInsets(new Insets(50, 50, 50, 50));

        BorderPane bp = new BorderPane(gp);
        bp.setBackground(new Background(new BackgroundFill(Color.YELLOWGREEN, new CornerRadii(10), null)));
        bp.setBorder(new Border(new BorderStroke(Color.ORANGERED, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THICK)));
        bp.setMaxWidth(USE_PREF_SIZE);
        bp.setMaxHeight(USE_PREF_SIZE);

        // Now that the grid pane doesn't take all space, we center it (if shown in a border pane which is very probable)
        GridPane goldPane = new GridPane();
        RowConstraints rc1 = new RowConstraints();
        RowConstraints rc2 = new RowConstraints();
        RowConstraints rc3 = new RowConstraints();
        goldPane.getRowConstraints().setAll(rc1, rc2, rc3);
        ColumnConstraints cc = new ColumnConstraints();
        cc.setHgrow(Priority.ALWAYS);
        goldPane.getColumnConstraints().setAll(cc);
        goldPane.add(bp, 0, 1);
        goldPane.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        goldPane.heightProperty().addListener((observable, oldValue, newHeight) -> {
            double h2 = bp.getHeight();
            double remainingHeight = newHeight - h2;
            double h1 = 100 * remainingHeight / 261;
            double h3 = remainingHeight - h1;
            rc1.setPrefHeight(h1);
            rc3.setPrefHeight(h3);
        });

        GridPane.setHalignment(bp, HPos.CENTER);
        GridPane.setValignment(bp, VPos.CENTER);
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
