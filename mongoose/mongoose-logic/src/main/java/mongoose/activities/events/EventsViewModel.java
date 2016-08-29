package mongoose.activities.events;

import naga.framework.ui.presentation.ViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.Table;

/**
 * @author Bruno Salmon
 */
public class EventsViewModel implements ViewModel {

    private final GuiNode contentNode;
    private final SearchBox searchBox;
    private final Table table;
    private final CheckBox withBookingsCheckBox;
    private final CheckBox limitCheckBox;

    public EventsViewModel(GuiNode contentNode, SearchBox searchBox, Table table, CheckBox withBookingsCheckBox, CheckBox limitCheckBox) {
        this.contentNode = contentNode;
        this.searchBox = searchBox;
        this.table = table;
        this.withBookingsCheckBox = withBookingsCheckBox;
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

    CheckBox getWithBookingsCheckBox() {
        return withBookingsCheckBox;
    }

    CheckBox getLimitCheckBox() {
        return limitCheckBox;
    }
}
