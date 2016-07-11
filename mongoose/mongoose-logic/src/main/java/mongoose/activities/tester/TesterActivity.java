package mongoose.activities.tester;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.tester.drive.Drive;
import mongoose.activities.tester.drive.model.ChartData;
import mongoose.activities.tester.drive.model.ConnectionChartGenerator;
import mongoose.activities.tester.drive.model.ConnectionsChartData;
import mongoose.activities.monitor.metrics.Metrics;
import mongoose.activities.monitor.metrics.model.SysBeanFX;
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
public class TesterActivity extends PresentationActivity<TesterViewModel, TesterPresentationModel> {

    public TesterActivity() {
        super(TesterPresentationModel::new);
    }

    private final ObjectProperty<SysBeanFX> sbfx = new SimpleObjectProperty<>();
    private ObjectProperty<ChartData> connectionsToDisplay = new SimpleObjectProperty<>(new ConnectionsChartData());

    protected TesterViewModel buildView(Toolkit toolkit) {
        // TextFields
        Table systemTable = toolkit.createTable();
        TextField freeMemField = toolkit.createTextField();
        TextField totalMemField = toolkit.createTextField();
        // Sliders
        Slider requestedSlider = toolkit.createSlider();
        Gauge startedSlider = toolkit.createGauge();
        // Charts
        LineChart connectionsChart = toolkit.createLineChart();
        // Buttons
        Button startButton = toolkit.createButton();
        Button stopButton = toolkit.createButton();
        Button exitButton = toolkit.createButton();
        // Arranging in boxes
        HBox hBox = toolkit.createHBox();
        hBox.getChildren().setAll(startButton, stopButton, exitButton);
        VBox vBox = toolkit.createVBox();
        vBox.getChildren().setAll(requestedSlider, startedSlider);
        // Building the UI components
        return new TesterViewModel(toolkit.createVPage()
                    .setHeader(vBox)
                    .setCenter(connectionsChart)
                    .setFooter(hBox),
                systemTable,
                freeMemField,
                totalMemField,
                connectionsChart,
                requestedSlider,
                startedSlider,
                startButton,
                stopButton,
                exitButton);
    }

    protected void bindViewModelWithPresentationModel(TesterViewModel vm, TesterPresentationModel pm) {
        // Sliders
        vm.getStartedSlider().setMin(0);
        vm.getStartedSlider().setMax(3000);
        vm.getRequestSlider().setMin(0);
        vm.getRequestSlider().setMax(3000);
        pm.requestedConnectionsProperty().bind(vm.getRequestSlider().valueProperty());
        vm.getStartedSlider().valueProperty().bind(pm.startedConnectionsProperty());
//        DisplayResultSet rs = new DisplayResultSet(6, new Object[]{"Europe", "NA", "Asia", "SA", "Oceania", "Africa", 1757, 597, 159, 127, 103, 21}, new DisplayColumn[]{new DisplayColumn("Continent", PrimType.STRING), new DisplayColumn("Nb", PrimType.INTEGER)});
//        chart.setDisplayResultSet(rs);
        // Buttons
        vm.getStartButton().setText("Start");
        vm.getStopButton().setText("Stop");
        vm.getExitButton().setText("Exit");
        // Charts
        vm.getConnectionsChart().displayResultSetProperty().bind(pm.chartDisplayResultSetProperty());
    }

    protected void bindPresentationModelWithLogic(TesterPresentationModel pm) {
        // Metrics
        Metrics metrics = Metrics.getInstance();
        metrics.setSbfx(sbfx);
//        metrics.start(false);
        // Drive
        Drive drive = Drive.getInstance();
        drive.start(true);
        drive.requestedConnectionCountProperty().bind(pm.requestedConnectionsProperty());
        pm.startedConnectionsProperty().bind(drive.startedConnectionCountProperty());
        // Charts
        ConnectionChartGenerator connectionChart = new ConnectionChartGenerator();
        connectionChart.start();
        pm.chartDisplayResultSetProperty().bind(connectionChart.connectionListProperty());
    }

    public void setListeners (TesterViewModel vm) {
        // Metrics
//        sbfx.addListener((observable, oldValue, newValue) -> {
//            sysTableView.getTableView().getItems().add(newValue);
//            systemChartView.scrollSeries();
//            systemChartView.addValues(newValue);
//        });

        // Linking system table and fields
//        sysGridView.bind(sysBean);       // We bind the GridView on a SysBeanFX object driven by the selected item of the table
//        sysTableView.getTableView().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)-> {
//            sysBean.set(newValue);
//        });

        // Charts
//        connectionsToDisplay.addListener((observable, oldValue, newValue) -> {
//            int size = EventListenerImpl.getInstance().getConnectionList().size();
//            DisplayResultSet rs = ConnectionChartGenerator.createDisplayResultSet(size, EventListenerImpl.getInstance().getConnectionList());
//            vm.getConnectionsChart().setDisplayResultSet(rs);
//        });
    }
}
