package mongoose.activities.backend.events;

import mongoose.activities.shared.generic.GenericTableViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.Table;

/**
 * @author Bruno Salmon
 */
class EventsViewModel extends GenericTableViewModel {

    private final CheckBox withBookingsCheckBox;

    public EventsViewModel(GuiNode contentNode, SearchBox searchBox, Table table, CheckBox limitCheckBox, CheckBox withBookingsCheckBox) {
        super(contentNode, searchBox, table, limitCheckBox);
        this.withBookingsCheckBox = withBookingsCheckBox;
    }

    CheckBox getWithBookingsCheckBox() {
        return withBookingsCheckBox;
    }
}
