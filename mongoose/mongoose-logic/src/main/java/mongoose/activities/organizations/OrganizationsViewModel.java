package mongoose.activities.organizations;

import naga.framework.ui.presentation.AbstractViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.Table;

/**
 * @author Bruno Salmon
 */
class OrganizationsViewModel extends AbstractViewModel {

    private final SearchBox searchBox;
    private final Table table;
    private final CheckBox withEventsCheckBox;
    private final CheckBox limitCheckBox;

    OrganizationsViewModel(GuiNode contentNode, SearchBox searchBox, Table table, CheckBox withEventsCheckBox, CheckBox limitCheckBox) {
        super(contentNode);
        this.searchBox = searchBox;
        this.table = table;
        this.withEventsCheckBox = withEventsCheckBox;
        this.limitCheckBox = limitCheckBox;
    }

    SearchBox getSearchBox() {
        return searchBox;
    }

    Table getTable() {
        return table;
    }

    CheckBox getWithEventsCheckBox() {
        return withEventsCheckBox;
    }

    CheckBox getLimitCheckBox() {
        return limitCheckBox;
    }
}
