package mongoose.activities.backend.event.bookings;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import mongoose.activities.shared.generic.table.GenericTablePresentationViewActivity;
import mongoose.activities.shared.logic.ui.theme.Theme;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public class BookingsPresentationViewActivity extends GenericTablePresentationViewActivity<BookingsPresentationModel> {

    private HBox hBox;

    @Override
    protected void createViewNodes(BookingsPresentationModel pm) {
        super.createViewNodes(pm);

        I18n i18n = getI18n();
        Button newBookingButton = i18n.translateText(new Button(), "NewBooking");
        Button cloneEventButton = i18n.translateText(new Button(), "CloneEvent");

        hBox = new HBox(newBookingButton, searchBox, cloneEventButton);
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        hBox.setPrefWidth(Double.MAX_VALUE);
        hBox.setMaxWidth(Double.MAX_VALUE);

        newBookingButton.textFillProperty().bind(Theme.mainTextFillProperty());
        cloneEventButton.textFillProperty().bind(Theme.mainTextFillProperty());

        newBookingButton.onActionProperty().bind(pm.onNewBookingProperty());
        cloneEventButton.onActionProperty().bind(pm.onCloneEventProperty());
    }

    @Override
    protected Node assemblyViewNodes() {
        return new BorderPane(table, hBox, null, limitCheckBox, null);
    }
}
