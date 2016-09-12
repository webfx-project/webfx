package mongoose.activities.organizations;

import mongoose.activities.shared.GenericTableViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.Table;

/**
 * @author Bruno Salmon
 */
class OrganizationsViewModel extends GenericTableViewModel {

    private final CheckBox withEventsCheckBox;

    public OrganizationsViewModel(GuiNode contentNode, SearchBox searchBox, Table table, CheckBox limitCheckBox, CheckBox withEventsCheckBox) {
        super(contentNode, searchBox, table, limitCheckBox);
        this.withEventsCheckBox = withEventsCheckBox;
    }

    CheckBox getWithEventsCheckBox() {
        return withEventsCheckBox;
    }
}
