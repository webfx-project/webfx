package mongoose.activities.backend.event.bookings;

import mongoose.activities.shared.generic.GenericTableViewModel;
import naga.toolkit.fxdata.control.DataGrid;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.scene.control.CheckBox;
import naga.toolkit.fx.scene.control.TextField;

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
