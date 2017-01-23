package mongoose.activities.backend.monitor;

import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import naga.framework.activity.presentation.view.impl.PresentationViewActivityImpl;
import naga.fxdata.chart.Chart;
import naga.fxdata.chart.LineChart;

/**
 * @author Bruno Salmon
 */
public class MonitorPresentationViewActivity extends PresentationViewActivityImpl<MonitorPresentationModel> {

    private Chart memoryChart;
    private Chart cpuChart;

    @Override
    protected void createViewNodes(MonitorPresentationModel pm) {
        memoryChart = new LineChart();
        cpuChart = new LineChart();
        memoryChart.setMaxWidth(Double.MAX_VALUE);
        memoryChart.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(memoryChart, Priority.ALWAYS);
        VBox.setVgrow(cpuChart, Priority.ALWAYS);

        memoryChart.displayResultSetProperty().bind(pm.memoryDisplayResultSetProperty());
        cpuChart.displayResultSetProperty().bind(pm.cpuDisplayResultSetProperty());

    }

    @Override
    protected Node assemblyViewNodes() {
        return new VBox(memoryChart, cpuChart);
    }
}
