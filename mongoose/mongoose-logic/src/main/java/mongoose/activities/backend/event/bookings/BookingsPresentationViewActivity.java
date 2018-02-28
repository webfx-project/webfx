package mongoose.activities.backend.event.bookings;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import mongoose.activities.backend.event.clone.CloneEventRoutingRequest;
import mongoose.activities.shared.generic.table.GenericTablePresentationViewActivity;
import naga.framework.operation.OperationActionProducer;

import static naga.framework.ui.layouts.LayoutUtil.setHGrowable;

/**
 * @author Bruno Salmon
 */
class BookingsPresentationViewActivity extends GenericTablePresentationViewActivity<BookingsPresentationModel> implements OperationActionProducer {

    private HBox hBox;

    @Override
    protected void createViewNodes(BookingsPresentationModel pm) {
        super.createViewNodes(pm);

        Button newBookingButton = newButton(newAction(() -> new NewBackendBookingRoutingRequest(pm.getEventId(), getHistory())));
        Button cloneEventButton = newButton(newAction(() -> new CloneEventRoutingRequest(pm.getEventId(), getHistory())));

        hBox = new HBox(newBookingButton, setHGrowable(searchBox), cloneEventButton);
        }

    @Override
    protected Node assemblyViewNodes() {
        return new BorderPane(table, hBox, null, limitCheckBox, null);
    }
}
