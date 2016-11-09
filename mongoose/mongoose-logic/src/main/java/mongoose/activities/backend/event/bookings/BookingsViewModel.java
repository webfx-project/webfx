package mongoose.activities.backend.event.bookings;

import mongoose.activities.shared.generic.GenericTableViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.Table;

/**
 * @author Bruno Salmon
 */
class BookingsViewModel extends GenericTableViewModel {

    private final Button newBookingButton;

    public BookingsViewModel(GuiNode contentNode, SearchBox searchBox, Table table, CheckBox limitCheckBox, Button newBookingButton) {
        super(contentNode, searchBox, table, limitCheckBox);
        this.newBookingButton = newBookingButton;
    }

    Button getNewBookingButton() {
        return newBookingButton;
    }
}
