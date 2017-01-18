package mongoose.activities.backend.organizations;

import mongoose.activities.shared.generic.GenericTableViewModel;
import naga.fxdata.control.DataGrid;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

/**
 * @author Bruno Salmon
 */
public class OrganizationsViewModel extends GenericTableViewModel {

    private final CheckBox withEventsCheckBox;

    public OrganizationsViewModel(Node contentNode, TextField searchBox, DataGrid table, CheckBox limitCheckBox, CheckBox withEventsCheckBox) {
        super(contentNode, searchBox, table, limitCheckBox);
        this.withEventsCheckBox = withEventsCheckBox;
    }

    CheckBox getWithEventsCheckBox() {
        return withEventsCheckBox;
    }
}
