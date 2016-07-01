package mongoose.activities.tester;

import naga.core.spi.toolkit.controls.Button;
import naga.core.spi.toolkit.controls.Slider;
import naga.core.spi.toolkit.controls.Table;
import naga.core.spi.toolkit.node.GuiNode;
import naga.core.ui.presentation.ViewModel;

/**
 * @author Bruno Salmon
 */
public class TesterViewModel implements ViewModel {

    private final GuiNode contentNode;
    private final Table systemTable;
    private final Slider requestSlider ;
    private final Slider startedSlider ;
    private final Button<Integer> startButton ;
    private final Button<Integer> stopButton ;
    private final Button<Integer> exitButton ;

    public TesterViewModel(GuiNode contentNode,
                           Table systemTable,
                           Slider requestSlider,
                           Slider startedSlider,
                           Button<Integer> startButton,
                           Button<Integer> stopButton,
                           Button<Integer> exitButton) {
        this.contentNode = contentNode;
        this.systemTable = systemTable;
        this.requestSlider = requestSlider;
        this.startedSlider = startedSlider;
        this.startButton = startButton;
        this.stopButton = stopButton;
        this.exitButton = exitButton;
    }

    @Override
    public GuiNode getContentNode() {
        return contentNode;
    }

    public Table getSystemTable() {
        return systemTable;
    }

    public Slider getRequestSlider() {
        return requestSlider;
    }

    public Slider getStartedSlider() {
        return startedSlider;
    }

    public Button<Integer> getStartButton() {
        return startButton;
    }

    public Button<Integer> getStopButton() {
        return stopButton;
    }

    public Button<Integer> getExitButton() {
        return exitButton;
    }
}
