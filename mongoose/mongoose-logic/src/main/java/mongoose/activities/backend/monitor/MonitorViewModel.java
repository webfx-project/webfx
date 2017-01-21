package mongoose.activities.backend.monitor;

import naga.framework.ui.presentation.ViewModelBase;
import naga.fxdata.chart.Chart;
import javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
class MonitorViewModel extends ViewModelBase {

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
