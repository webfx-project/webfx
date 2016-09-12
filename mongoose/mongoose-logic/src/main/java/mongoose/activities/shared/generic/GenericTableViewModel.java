package mongoose.activities.shared.generic;

import naga.framework.ui.presentation.AbstractViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.Table;

/**
 * @author Bruno Salmon
 */
public class GenericTableViewModel extends AbstractViewModel {

    private final SearchBox searchBox;
    private final Table table;
    private final CheckBox limitCheckBox;

    protected GenericTableViewModel(GuiNode contentNode, SearchBox searchBox, Table table, CheckBox limitCheckBox) {
        super(contentNode);
        this.searchBox = searchBox;
        this.table = table;
        this.limitCheckBox = limitCheckBox;
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
