package mongoose.activities.backend.monitor;

import naga.framework.ui.presentation.AbstractViewModel;
import naga.toolkit.fx.ext.chart.Chart;
import naga.toolkit.fx.scene.Node;

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
