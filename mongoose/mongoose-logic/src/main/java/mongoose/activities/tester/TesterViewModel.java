package mongoose.activities.tester;

import naga.framework.ui.presentation.ViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.charts.Chart;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.controls.Slider;
import naga.toolkit.spi.nodes.gauges.Gauge;

/**
 * @author Bruno Salmon
 */
public class TesterViewModel implements ViewModel {

    private final GuiNode contentNode;
    private final Button saveTest;
    private final Chart connectionsChart;
    private final Slider requestSlider ;
    private final Gauge startedSlider ;

    public TesterViewModel(GuiNode contentNode,
                           Button saveTest,
                           Chart connectionsChart,
                           Slider requestSlider,
                           Gauge startedSlider) {
        this.contentNode = contentNode;
        this.saveTest = saveTest;
        this.connectionsChart = connectionsChart;
        this.requestSlider = requestSlider;
        this.startedSlider = startedSlider;
    }

    @Override
    public GuiNode getContentNode() {
        return contentNode;
    }

    public Button getSaveTest() {
        return saveTest;
    }

    public Chart getConnectionsChart() {
        return connectionsChart;
    }

    public Slider getRequestSlider() {
        return requestSlider;
    }

    public Gauge getStartedSlider() {
        return startedSlider;
    }
}
