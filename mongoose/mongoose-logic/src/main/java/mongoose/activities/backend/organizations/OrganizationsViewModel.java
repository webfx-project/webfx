package mongoose.activities.backend.organizations;

import mongoose.activities.shared.generic.GenericTableViewModel;
import naga.toolkit.fxdata.control.DataGrid;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.control.CheckBox;
import naga.toolkit.fx.scene.control.TextField;

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
