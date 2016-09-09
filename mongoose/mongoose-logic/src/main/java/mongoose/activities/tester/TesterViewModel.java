package mongoose.activities.tester;

import naga.framework.ui.presentation.AbstractViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.charts.Chart;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.controls.Slider;
import naga.toolkit.spi.nodes.gauges.Gauge;

/**
 * @author Bruno Salmon
 */
class TesterViewModel extends AbstractViewModel {

    private final Button saveTest;
    private final Chart connectionsChart;
    private final Slider requestSlider ;
    private final Gauge startedSlider ;

    TesterViewModel(GuiNode contentNode,
                           Button saveTest,
                           Chart connectionsChart,
                           Slider requestSlider,
                           Gauge startedSlider) {
        super(contentNode);
        this.saveTest = saveTest;
        this.connectionsChart = connectionsChart;
        this.requestSlider = requestSlider;
        this.startedSlider = startedSlider;
    }

    Button getSaveTest() {
        return saveTest;
    }

    Chart getConnectionsChart() {
        return connectionsChart;
    }

    Slider getRequestSlider() {
        return requestSlider;
    }

    Gauge getStartedSlider() {
        return startedSlider;
    }
}
