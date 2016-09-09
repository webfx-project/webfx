package mongoose.activities.event.bookings;

import naga.framework.ui.presentation.AbstractViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.Table;

/**
 * @author Bruno Salmon
 */
class BookingsViewModel extends AbstractViewModel {

    private final SearchBox searchBox;
    private final Table table;
    private final CheckBox limitCheckBox;

    BookingsViewModel(GuiNode contentNode, SearchBox searchBox, Table table, CheckBox limitCheckBox) {
        super(contentNode);
        this.searchBox = searchBox;
        this.table = table;
        this.limitCheckBox = limitCheckBox;
    }

    SearchBox getSearchBox() {
        return searchBox;
    }

    Table getTable() {
        return table;
    }

    CheckBox getLimitCheckBox() {
        return limitCheckBox;
    }
}
