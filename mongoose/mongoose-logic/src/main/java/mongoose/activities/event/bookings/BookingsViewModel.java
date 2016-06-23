package mongoose.activities.event.bookings;

import naga.core.spi.toolkit.node.GuiNode;
import naga.core.spi.toolkit.controls.CheckBox;
import naga.core.spi.toolkit.controls.SearchBox;
import naga.core.spi.toolkit.controls.Table;
import naga.core.ui.presentation.ViewModel;

/**
 * @author Bruno Salmon
 */
public class BookingsViewModel implements ViewModel {

    private final GuiNode contentNode;
    private final SearchBox searchBox;
    private final Table table;
    private final CheckBox limitCheckBox;

    public BookingsViewModel(GuiNode contentNode, SearchBox searchBox, Table table, CheckBox limitCheckBox) {
        this.contentNode = contentNode;
        this.searchBox = searchBox;
        this.table = table;
        this.limitCheckBox = limitCheckBox;
    }

    @Override
    public GuiNode getContentNode() {
        return contentNode;
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
