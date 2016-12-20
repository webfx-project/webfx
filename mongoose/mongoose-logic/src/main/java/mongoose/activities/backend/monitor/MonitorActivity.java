package mongoose.activities.backend.monitor;

import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.fx.ext.chart.Chart;
import naga.toolkit.fx.ext.chart.LineChart;
import naga.toolkit.fx.scene.layout.VBox;

/**
 * @author Bruno Salmon
 */
public class MonitorActivity extends PresentationActivity<MonitorViewModel, MonitorPresentationModel> {

    public MonitorActivity() {
        super(MonitorPresentationModel::new); // Passing the presentation model factory
    }

    protected MonitorViewModel buildView() {
        // Charts
        Chart memoryChart = new LineChart(), cpuChart = new LineChart();
        memoryChart.setMaxWidth(Double.MAX_VALUE);
        // Building the content node and returning the view model
        return new MonitorViewModel(new VBox(memoryChart, cpuChart),
                memoryChart, cpuChart);
    }

    protected void bindViewModelWithPresentationModel(MonitorViewModel vm, MonitorPresentationModel pm) {
        // Charts
        vm.getMemoryChart().displayResultSetProperty().bind(pm.memoryDisplayResultSetProperty());
        vm.getCpuChart().displayResultSetProperty().bind(pm.cpuDisplayResultSetProperty());
    }

    protected void bindPresentationModelWithLogic(MonitorPresentationModel pm) {
        createReactiveExpressionFilter("{class: 'Metrics', orderBy: 'date desc', limit: '500'}")
                .setExpressionColumns("['0 + id','memoryUsed','memoryTotal']")
                .displayResultSetInto(pm.memoryDisplayResultSetProperty())
                .nextDisplay()
                .setExpressionColumns("['0 + id','systemLoadAverage','processCpuLoad']")
                //.combine("{columns: '0 + id,systemLoadAverage,processCpuLoad'}")
                .displayResultSetInto(pm.cpuDisplayResultSetProperty())
                .setAutoRefresh(true)
                .start();
    }
}
