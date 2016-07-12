package mongoose.activities.monitor;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.monitor.metrics.Metrics;
import mongoose.activities.monitor.metrics.model.MemChartGenerator;
import mongoose.activities.monitor.metrics.model.SysBeanFX;
import mongoose.activities.tester.drive.model.ConnectionData;
import mongoose.activities.tester.drive.model.ConnectionsChartData;
import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.charts.LineChart;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.controls.Slider;
import naga.toolkit.spi.nodes.controls.Table;
import naga.toolkit.spi.nodes.controls.TextField;
import naga.toolkit.spi.nodes.gauges.Gauge;
import naga.toolkit.spi.nodes.layouts.HBox;
import naga.toolkit.spi.nodes.layouts.VBox;

/**
 * @author Bruno Salmon
 */
public class MonitorActivity extends PresentationActivity<MonitorViewModel, MonitorPresentationModel> {

    public MonitorActivity() {
        super(MonitorPresentationModel::new);
    }

    private final ObjectProperty<SysBeanFX> sbfx = new SimpleObjectProperty<>();
    private ObjectProperty<ConnectionData> connectionsToDisplay = new SimpleObjectProperty<>(new ConnectionsChartData());

    protected MonitorViewModel buildView(Toolkit toolkit) {
        // TextFields
        Table systemTable = toolkit.createTable();
        TextField freeMemField = toolkit.createTextField();
        TextField totalMemField = toolkit.createTextField();
        // Sliders
        Slider requestedSlider = toolkit.createSlider();
        Gauge startedSlider = toolkit.createGauge();
        // Charts
        LineChart memChart = toolkit.createLineChart();
        // Buttons
        Button startButton = toolkit.createButton();
        Button stopButton = toolkit.createButton();
        // Arranging in boxes
        HBox hBox = toolkit.createHBox();
        hBox.getChildren().setAll(startButton, stopButton);
        VBox vBox = toolkit.createVBox();
        vBox.getChildren().setAll(requestedSlider, startedSlider);
        // Building the UI components
        return new MonitorViewModel(toolkit.createVPage()
                    .setHeader(vBox)
                    .setCenter(memChart)
                    .setFooter(hBox),
                systemTable,
                freeMemField,
                totalMemField,
                memChart,
                startButton,
                stopButton);
    }

    protected void bindViewModelWithPresentationModel(MonitorViewModel vm, MonitorPresentationModel pm) {
        // Sliders
        // Buttons
        vm.getStartButton().setText("Start");
        vm.getStopButton().setText("Stop");
        // Charts
        vm.getMemChart().displayResultSetProperty().bind(pm.chartDisplayResultSetProperty());
    }

    protected void bindPresentationModelWithLogic(MonitorPresentationModel pm) {
        // Metrics
        Metrics metrics = Metrics.getInstance();
        metrics.setSbfx(sbfx);
        metrics.start(false);    // snapshots of the system
        // Charts
        MemChartGenerator memChart = new MemChartGenerator();
        memChart.start();        // update the list of snapshots to display on chart
        pm.chartDisplayResultSetProperty().bind(memChart.memListProperty());
    }
}
