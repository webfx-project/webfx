package mongoose.activities.monitor;

import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.charts.Chart;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.layouts.HBox;

/**
 * @author Bruno Salmon
 */
public class MonitorActivity extends PresentationActivity<MonitorViewModel, MonitorPresentationModel> {

    public MonitorActivity() {
        super(MonitorPresentationModel::new); // Passing the presentation model factory
    }

    protected MonitorViewModel buildView(Toolkit toolkit) {
        // Chart
        Chart memChart = toolkit.createLineChart();
        // Buttons
        Button startButton = toolkit.createButton();
        Button stopButton = toolkit.createButton();
        // Arranging in boxes
        HBox hBox = toolkit.createHBox();
        hBox.getChildren().setAll(startButton, stopButton);
        // Building the content node and returning the view model
        return new MonitorViewModel(toolkit.createVPage()
                    .setCenter(memChart)
                    .setFooter(hBox),
                toolkit.createTable(),     // systemTable
                toolkit.createTextField(), // freeMemField
                toolkit.createTextField(), // totalMemField
                memChart, startButton, stopButton);
    }

    protected void bindViewModelWithPresentationModel(MonitorViewModel vm, MonitorPresentationModel pm) {
        // Buttons
        vm.getStartButton().setText("Start");
        vm.getStopButton().setText("Stop");
        // Charts
        vm.getMemChart().displayResultSetProperty().bind(pm.chartDisplayResultSetProperty());
    }

    protected void bindPresentationModelWithLogic(MonitorPresentationModel pm) {
        createReactiveExpressionFilter("{class: 'Metrics', orderBy: 'date desc', limit: '500'}")
                .setExpressionColumns("['0 + id','memoryUsed','memoryTotal']")
                .setAutoRefresh(true)
                .displayResultSetInto(pm.chartDisplayResultSetProperty());
/*
        // Metrics
        Metrics metrics = Metrics.getInstance();
        metrics.setMemData(memData);
        metrics.start(false);    // snapshots of the system
        // Charts
        MemChartGenerator memChart = new MemChartGenerator();
        memChart.start();        // update the list of snapshots to display on chart
        pm.chartDisplayResultSetProperty().bind(memChart.memListProperty());
*/
    }
}
