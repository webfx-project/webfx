package mongoose.activities.tester;

import naga.toolkit.spi.nodes.charts.Chart;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.controls.Slider;
import naga.toolkit.spi.nodes.controls.Table;
import naga.toolkit.spi.nodes.controls.TextField;
import naga.toolkit.spi.nodes.gauges.Gauge;
import naga.toolkit.spi.nodes.GuiNode;
import naga.framework.ui.presentation.ViewModel;

/**
 * @author Bruno Salmon
 */
public class TesterViewModel implements ViewModel {

    private final GuiNode contentNode;
    private final Chart connectionsChart;
    private final Slider requestSlider ;
    private final Gauge startedSlider ;

    public TesterViewModel(GuiNode contentNode,
                           Chart connectionsChart,
                           Slider requestSlider,
                           Gauge startedSlider) {
        this.contentNode = contentNode;
        this.connectionsChart = connectionsChart;
        this.requestSlider = requestSlider;
        this.startedSlider = startedSlider;
    }

    @Override
    public GuiNode getContentNode() {
        return contentNode;
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
