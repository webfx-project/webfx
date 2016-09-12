package mongoose.activities.backend.monitor;

import naga.framework.ui.presentation.AbstractViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.charts.Chart;
import naga.toolkit.spi.nodes.controls.Button;

/**
 * @author Bruno Salmon
 */
class MonitorViewModel extends AbstractViewModel {

    private final Chart memChart;
    private final Chart sysChart;
    private final Button startButton ;
    private final Button stopButton ;

    MonitorViewModel(GuiNode contentNode,
                            Chart memChart,
                            Chart sysChart,
                            Button startButton,
                            Button stopButton) {
        super(contentNode);
        this.memChart = memChart;
        this.sysChart = sysChart;
        this.startButton = startButton;
        this.stopButton = stopButton;
    }

    Chart getMemChart() {
        return memChart;
    }

    Chart getSysChart() {
        return sysChart;
    }

    Button getStartButton() {
        return startButton;
    }

    Button getStopButton() {
        return stopButton;
    }

}
