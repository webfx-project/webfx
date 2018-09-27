package mongoose.backend.activities.monitor;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import webfx.framework.activity.impl.elementals.presentation.view.impl.PresentationViewActivityImpl;
import webfx.fxkits.extra.chart.Chart;
import webfx.fxkits.extra.chart.LineChart;

import static webfx.framework.ui.layouts.LayoutUtil.setVGrowable;

/**
 * @author Bruno Salmon
 */
final class MonitorPresentationViewActivity extends PresentationViewActivityImpl<MonitorPresentationModel> {

    private Chart memoryChart;
    private Chart cpuChart;

    @Override
    protected void createViewNodes(MonitorPresentationModel pm) {
        memoryChart = setVGrowable(new LineChart());
        cpuChart = setVGrowable(new LineChart());

        memoryChart.displayResultProperty().bind(pm.memoryDisplayResultProperty());
        cpuChart.displayResultProperty().bind(pm.cpuDisplayResultProperty());
    }

    @Override
    protected Node assemblyViewNodes() {
        return new VBox(memoryChart, cpuChart);
    }
}
