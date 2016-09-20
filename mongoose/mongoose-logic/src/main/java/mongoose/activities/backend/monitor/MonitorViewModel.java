package mongoose.activities.backend.monitor;

import naga.framework.ui.presentation.AbstractViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.charts.Chart;

/**
 * @author Bruno Salmon
 */
class MonitorViewModel extends AbstractViewModel {

    private final Chart memoryChart;
    private final Chart cpuChart;

    public MonitorViewModel(GuiNode contentNode, Chart memoryChart, Chart cpuChart) {
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
