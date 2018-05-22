package mongoose.activities.backend.javafx.event.clone;

import javafx.scene.control.DatePicker;
import mongoose.activities.backend.cloneevent.CloneEventPresentationModel;
import mongoose.activities.backend.cloneevent.CloneEventPresentationViewActivity;
import mongoose.domainmodel.formatters.DateFormatter;

/**
 * @author Bruno Salmon
 */
class FxCloneEventPresentationViewActivity extends CloneEventPresentationViewActivity {

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
