package mongoose.backend.activities.event.clone.javafx;

import javafx.scene.control.DatePicker;
import mongoose.backend.activities.cloneevent.CloneEventPresentationModel;
import mongoose.backend.activities.cloneevent.CloneEventPresentationViewActivity;
import mongoose.shared.domainmodel.formatters.DateFormatter;

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
        datePicker.setConverter(DateFormatter.SINGLETON.toStringConverter());
    }

}
