package mongoose.activities.monitor;

import naga.core.spi.toolkit.controls.Slider;
import naga.core.spi.toolkit.node.GuiNode;
import naga.core.ui.presentation.ViewModel;

/**
 * @author Bruno Salmon
 */
public class MonitorViewModel implements ViewModel {

    private final GuiNode contentNode;
    private final Slider requestSlider ;
    private final Slider startedSlider ;

    public MonitorViewModel(GuiNode contentNode, Slider requestSlider, Slider startedSlider) {
        this.contentNode = contentNode;
        this.requestSlider = requestSlider;
        this.startedSlider = startedSlider;
    }

    @Override
    public GuiNode getContentNode() {
        return contentNode;
    }

    public Slider getRequestSlider() {
        return requestSlider;
    }

    public Slider getStartedSlider() {
        return startedSlider;
    }
}
