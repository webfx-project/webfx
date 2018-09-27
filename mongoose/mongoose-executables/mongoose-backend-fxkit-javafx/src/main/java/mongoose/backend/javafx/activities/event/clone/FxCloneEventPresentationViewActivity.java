package mongoose.backend.javafx.activities.event.clone;

import javafx.scene.control.DatePicker;
import mongooses.core.backend.activities.cloneevent.CloneEventPresentationModel;
import mongooses.core.backend.activities.cloneevent.CloneEventPresentationViewActivity;
import mongooses.core.shared.domainmodel.formatters.DateFormatter;

/**
 * @author Bruno Salmon
 */
final class FxCloneEventPresentationViewActivity extends CloneEventPresentationViewActivity {

    @Override
    protected void createViewNodes(CloneEventPresentationModel pm) {
        super.createViewNodes(pm);
        DatePicker datePicker = new DatePicker();
        datePicker.setPrefWidth(150d);
        gp.getChildren().remove(dateTextField);
        gp.add(datePicker, 1, 1);
        datePicker.valueProperty().bindBidirectional(pm.dateProperty());
        datePicker.setConverter(DateFormatter.LOCAL_DATE_STRING_CONVERTER);
    }

}
