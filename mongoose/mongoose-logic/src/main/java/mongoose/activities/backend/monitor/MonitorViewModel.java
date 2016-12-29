package mongoose.activities.backend.monitor;

import naga.framework.ui.presentation.AbstractViewModel;
import naga.fxdata.chart.Chart;
import naga.fx.scene.Node;

/**
 * @author Bruno Salmon
 */
class MonitorViewModel extends AbstractViewModel {

    private final Chart memoryChart;
    private final Chart cpuChart;

    MonitorViewModel(Node contentNode, Chart memoryChart, Chart cpuChart) {
        super(contentNode);
        this.memoryChart = memoryChart;
        this.cpuChart = cpuChart;
    }

    Chart getMemoryChart() {
        return memoryChart;
    }

    Chart getCpuChart() {
        return cpuChart;
    }

}
