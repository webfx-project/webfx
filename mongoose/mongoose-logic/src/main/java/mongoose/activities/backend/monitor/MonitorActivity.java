package mongoose.activities.backend.monitor;

import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.charts.Chart;

/**
 * @author Bruno Salmon
 */
public class MonitorActivity extends PresentationActivity<MonitorViewModel, MonitorPresentationModel> {

    public MonitorActivity() {
        super(MonitorPresentationModel::new); // Passing the presentation model factory
    }

    protected MonitorViewModel buildView(Toolkit toolkit) {
        // Charts
        Chart memoryChart = toolkit.createLineChart();
        Chart cpuChart = toolkit.createLineChart();
        // Building the content node and returning the view model
        return new MonitorViewModel(toolkit.createVPage().setCenter(toolkit.createVBox(memoryChart, cpuChart)),
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
                .displayResultSetInto(pm.cpuDisplayResultSetProperty())
                .setAutoRefresh(true)
                .start();
    }
}
