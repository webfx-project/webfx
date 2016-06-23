package mongoose.activities.monitor;

import naga.core.spi.toolkit.GuiNode;
import naga.core.ui.presentation.ViewModel;

/**
 * @author Bruno Salmon
 */
public class MonitorViewModel implements ViewModel {

    private final GuiNode contentNode;

    public MonitorViewModel(GuiNode contentNode) {
        this.contentNode = contentNode;
    }

    @Override
    public GuiNode getContentNode() {
        return contentNode;
    }

}
