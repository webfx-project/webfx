package mongoose.activities.events;

import naga.framework.ui.presentation.AbstractViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.Table;

/**
 * @author Bruno Salmon
 */
class EventsViewModel extends AbstractViewModel {

    private final SearchBox searchBox;
    private final Table table;
    private final CheckBox withBookingsCheckBox;
    private final CheckBox limitCheckBox;

    EventsViewModel(GuiNode contentNode, SearchBox searchBox, Table table, CheckBox withBookingsCheckBox, CheckBox limitCheckBox) {
        super(contentNode);
        this.searchBox = searchBox;
        this.table = table;
        this.withBookingsCheckBox = withBookingsCheckBox;
        this.limitCheckBox = limitCheckBox;
    }

    SearchBox getSearchBox() {
        return searchBox;
    }

    Table getTable() {
        return table;
    }

    CheckBox getWithBookingsCheckBox() {
        return withBookingsCheckBox;
    }

    CheckBox getLimitCheckBox() {
        return limitCheckBox;
    }
}
