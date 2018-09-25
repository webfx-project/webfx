package mongooses.core.backend.activities.bookings;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import mongooses.core.backend.operations.cloneevent.RouteToCloneEventRequest;
import mongooses.core.sharedends.activities.generic.table.GenericTablePresentationViewActivity;
import mongooses.core.backend.operations.bookings.RouteToNewBackendBookingRequest;
import webfx.framework.operation.action.OperationActionFactoryMixin;

import static webfx.framework.ui.layouts.LayoutUtil.setHGrowable;
import static webfx.framework.ui.layouts.LayoutUtil.setUnmanagedWhenInvisible;

/**
 * @author Bruno Salmon
 */
final class BookingsPresentationViewActivity extends GenericTablePresentationViewActivity<BookingsPresentationModel> implements OperationActionFactoryMixin {

    private HBox hBox;

    @Override
    protected void createViewNodes(BookingsPresentationModel pm) {
        super.createViewNodes(pm);

        Button newBookingButton = newButton(newAction(() -> new RouteToNewBackendBookingRequest(pm.getEventId(), getHistory())));
        Button cloneEventButton = newButton(newAction(() -> new RouteToCloneEventRequest(pm.getEventId(), getHistory())));

        hBox = new HBox(setUnmanagedWhenInvisible(newBookingButton), setHGrowable(searchBox), setUnmanagedWhenInvisible(cloneEventButton));
        }

    @Override
    protected Node assemblyViewNodes() {
        return new BorderPane(table, hBox, null, limitCheckBox, null);
    }
}
