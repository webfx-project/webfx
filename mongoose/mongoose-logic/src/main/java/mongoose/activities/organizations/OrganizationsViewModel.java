package mongoose.activities.organizations;

import naga.framework.ui.presentation.ViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.Table;

/**
 * @author Bruno Salmon
 */
public class OrganizationsViewModel implements ViewModel {

    private final GuiNode contentNode;
    private final SearchBox searchBox;
    private final Table table;
    private final CheckBox limitCheckBox;

    public OrganizationsViewModel(GuiNode contentNode, SearchBox searchBox, Table table, CheckBox limitCheckBox) {
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
