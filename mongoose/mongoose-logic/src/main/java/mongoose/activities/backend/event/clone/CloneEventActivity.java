package mongoose.activities.backend.event.clone;

import mongoose.activities.shared.generic.EventDependentActivity;
import mongoose.domainmodel.format.DateFormatter;
import mongoose.entities.Event;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.update.UpdateArgument;
import naga.platform.spi.Platform;
import naga.toolkit.fx.properties.Properties;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.scene.control.TextField;
import naga.toolkit.fx.scene.layout.BorderPane;
import naga.toolkit.fx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static java.time.temporal.ChronoField.*;

/**
 * @author Bruno Salmon
 */
public class CloneEventActivity extends EventDependentActivity<CloneEventViewModel, CloneEventPresentationModel> {

    public CloneEventActivity() {
        super(CloneEventPresentationModel::new);
    }

    @Override
    protected CloneEventViewModel buildView() {
        TextField nameTextField = new TextField();
        TextField dateTextField = new TextField();
        Button submitButton = new Button();
        return new CloneEventViewModel(new BorderPane(new VBox(nameTextField, dateTextField), null, null, submitButton, null),
                nameTextField, dateTextField, submitButton);
    }

    @Override
    protected void bindViewModelWithPresentationModel(CloneEventViewModel vm, CloneEventPresentationModel pm) {
        vm.getNameTextField().textProperty().bindBidirectional(pm.nameProperty());
        vm.getDateTextField().textProperty().bindBidirectional(pm.dateProperty());
        getI18n().translateText(vm.getSubmitButton(), "Submit").setOnMouseClicked(event -> {
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
