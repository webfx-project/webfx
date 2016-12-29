package mongoose.activities.backend.event.bookings;

import mongoose.activities.shared.generic.GenericTableViewModel;
import naga.fxdata.control.DataGrid;
import naga.fx.scene.Node;
import naga.fx.scene.control.Button;
import naga.fx.scene.control.CheckBox;
import naga.fx.scene.control.TextField;

/**
 * @author Bruno Salmon
 */
class BookingsViewModel extends GenericTableViewModel {

    private final Button newBookingButton;
    private final Button cloneEventButton;

    BookingsViewModel(Node contentNode, TextField searchBox, DataGrid table, CheckBox limitCheckBox, Button newBookingButton, Button cloneEventButton) {
        super(contentNode, searchBox, table, limitCheckBox);
        this.newBookingButton = newBookingButton;
        this.cloneEventButton = cloneEventButton;
    }

    Button getNewBookingButton() {
        return newBookingButton;
    }

    Button getCloneEventButton() {
        return cloneEventButton;
    }
}
