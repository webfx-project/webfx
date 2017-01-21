package mongoose.activities.shared.generic;

import naga.framework.ui.presentation.ViewModelBase;
import naga.fxdata.control.DataGrid;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

/**
 * @author Bruno Salmon
 */
public class GenericTableViewModel extends ViewModelBase {

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
