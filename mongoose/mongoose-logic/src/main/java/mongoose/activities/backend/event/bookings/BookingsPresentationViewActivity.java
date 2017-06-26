package mongoose.activities.backend.event.bookings;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import mongoose.activities.shared.generic.table.GenericTablePresentationViewActivity;

import static naga.framework.ui.controls.LayoutUtil.setHGrowable;

/**
 * @author Bruno Salmon
 */
public class BookingsPresentationViewActivity extends GenericTablePresentationViewActivity<BookingsPresentationModel> {

    private HBox hBox;

    @Override
    protected void createViewNodes(BookingsPresentationModel pm) {
        super.createViewNodes(pm);

        Button newBookingButton = newButton("NewBooking");
        Button cloneEventButton = newButton("CloneEvent");

        hBox = new HBox(newBookingButton, setHGrowable(searchBox), cloneEventButton);

        newBookingButton.onActionProperty().bind(pm.onNewBookingProperty());
        cloneEventButton.onActionProperty().bind(pm.onCloneEventProperty());
    }

    @Override
    protected Node assemblyViewNodes() {
        return new BorderPane(table, hBox, null, limitCheckBox, null);
    }
}
