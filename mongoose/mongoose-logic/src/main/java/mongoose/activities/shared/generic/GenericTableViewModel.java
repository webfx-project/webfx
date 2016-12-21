package mongoose.activities.shared.generic;

import naga.framework.ui.presentation.AbstractViewModel;
import naga.toolkit.fxdata.control.DataGrid;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.control.CheckBox;
import naga.toolkit.fx.scene.control.TextField;

/**
 * @author Bruno Salmon
 */
public class GenericTableViewModel extends AbstractViewModel {

    private final TextField searchBox;
    private final DataGrid table;
    private final CheckBox limitCheckBox;

    protected GenericTableViewModel(Node contentNode, TextField searchBox, DataGrid table, CheckBox limitCheckBox) {
        super(contentNode);
        this.searchBox = searchBox;
        this.table = table;
        this.limitCheckBox = limitCheckBox;
    }

    public TextField getSearchBox() {
        return searchBox;
    }

    public DataGrid getTable() {
        return table;
    }

    public CheckBox getLimitCheckBox() {
        return limitCheckBox;
    }
}
