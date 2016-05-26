package mongoose.logic.organizations;

import naga.core.ngui.presentation.ViewModel;
import naga.core.spi.toolkit.GuiNode;
import naga.core.spi.toolkit.nodes.ActionButton;
import naga.core.spi.toolkit.nodes.CheckBox;
import naga.core.spi.toolkit.nodes.SearchBox;
import naga.core.spi.toolkit.nodes.Table;

/**
 * @author Bruno Salmon
 */
class OrganizationsViewModel implements ViewModel {

    private final GuiNode contentNode;
    private final SearchBox searchBox;
    private final Table table;
    private final CheckBox limitCheckBox;
    private final ActionButton testButton;

    OrganizationsViewModel(GuiNode contentNode, SearchBox searchBox, Table table, CheckBox limitCheckBox, ActionButton testButton) {
        this.contentNode = contentNode;
        this.searchBox = searchBox;
        this.table = table;
        this.limitCheckBox = limitCheckBox;
        this.testButton = testButton;
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

    ActionButton getTestButton() {
        return testButton;
    }
}
