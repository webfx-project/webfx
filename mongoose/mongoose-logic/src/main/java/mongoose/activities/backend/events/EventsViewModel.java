package mongoose.activities.backend.events;

import mongoose.activities.shared.generic.GenericTableViewModel;
import naga.toolkit.fx.ext.control.DataGrid;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.control.CheckBox;
import naga.toolkit.fx.scene.control.TextField;

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
