package mongoose.logic.organizations;

import naga.core.ngui.presentation.UiModel;
import naga.core.spi.gui.GuiNode;
import naga.core.spi.gui.nodes.CheckBox;
import naga.core.spi.gui.nodes.SearchBox;
import naga.core.spi.gui.nodes.Table;

/**
 * @author Bruno Salmon
 */
public class OrganizationUiModel implements UiModel {

    private final GuiNode contentNode;
    private final SearchBox searchBox;
    private final Table table;
    private final CheckBox limitCheckBox;


    public OrganizationUiModel(GuiNode contentNode, SearchBox searchBox, Table table, CheckBox limitCheckBox) {
        this.contentNode = contentNode;
        this.searchBox = searchBox;
        this.table = table;
        this.limitCheckBox = limitCheckBox;
    }

    @Override
    public GuiNode getContentNode() {
        return contentNode;
    }

    public SearchBox getSearchBox() {
        return searchBox;
    }

    public Table getTable() {
        return table;
    }

    public CheckBox getLimitCheckBox() {
        return limitCheckBox;
    }
}
