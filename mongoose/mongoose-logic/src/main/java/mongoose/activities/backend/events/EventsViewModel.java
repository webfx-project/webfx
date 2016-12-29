package mongoose.activities.backend.events;

import mongoose.activities.shared.generic.GenericTableViewModel;
import naga.fxdata.control.DataGrid;
import naga.fx.scene.Node;
import naga.fx.scene.control.CheckBox;
import naga.fx.scene.control.TextField;

/**
 * @author Bruno Salmon
 */
class EventsViewModel extends GenericTableViewModel {

    private final CheckBox withBookingsCheckBox;

    EventsViewModel(Node contentNode, TextField searchBox, DataGrid table, CheckBox limitCheckBox, CheckBox withBookingsCheckBox) {
        super(contentNode, searchBox, table, limitCheckBox);
        this.withBookingsCheckBox = withBookingsCheckBox;
    }

    CheckBox getWithBookingsCheckBox() {
        return withBookingsCheckBox;
    }
}
