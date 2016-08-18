package mongoose.activities.tester;

import mongoose.activities.tester.drive.Drive;
import mongoose.activities.tester.drive.model.ConnectionChartGenerator;
import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.charts.LineChart;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.controls.Slider;
import naga.toolkit.spi.nodes.gauges.Gauge;
import naga.toolkit.spi.nodes.layouts.VBox;

/**
 * @author Bruno Salmon
 */
public class TesterActivity extends PresentationActivity<TesterViewModel, TesterPresentationModel> {
    ConnectionChartGenerator connectionChartGenerator = new ConnectionChartGenerator();

    public TesterActivity() {
        super(TesterPresentationModel::new);
    }

    protected TesterViewModel buildView(Toolkit toolkit) {
        // Sliders
        Slider requestedSlider = toolkit.createSlider();
        Gauge startedSlider = toolkit.createGauge();
        // Charts
        LineChart connectionsChart = toolkit.createLineChart();
        // Arranging in boxes
        VBox vBox = toolkit.createVBox();
        vBox.getChildren().setAll(requestedSlider, startedSlider);
        // Buttons
        Button saveTest = toolkit.createButton();
        saveTest.setText("Save Test");
        // Building the UI components
        return new TesterViewModel(toolkit.createVPage()
                    .setHeader(vBox)
                    .setCenter(connectionsChart)
                    .setFooter(saveTest),
                saveTest,
                connectionsChart,
                requestedSlider,
                startedSlider);
    }

    protected void bindViewModelWithPresentationModel(TesterViewModel vm, TesterPresentationModel pm) {
        vm.getSaveTest().actionEventObservable().subscribe(actionEvent -> {
            getHistory().push("/testSet");
//            Drive.getInstance().recordTestSet(getDataSourceModel(), pm.testNameProperty().getValue(), pm.testCommentProperty().getValue());
            connectionChartGenerator.reset();
//            pm.chartDisplayResultSetProperty().bind(connectionChartGenerator.connectionListProperty());
        });
        // Sliders
        vm.getStartedSlider().setMin(0);
        vm.getStartedSlider().setMax(3000);
        vm.getRequestSlider().setMin(0);
        vm.getRequestSlider().setMax(3000);
        pm.requestedConnectionsProperty().bind(vm.getRequestSlider().valueProperty());
        vm.getStartedSlider().valueProperty().bind(pm.startedConnectionsProperty());
//        DisplayResultSet rs = new DisplayResultSet(6, new Object[]{"Europe", "NA", "Asia", "SA", "Oceania", "Africa", 1757, 597, 159, 127, 103, 21}, new DisplayColumn[]{new DisplayColumn("Continent", PrimType.STRING), new DisplayColumn("Nb", PrimType.INTEGER)});
//        chart.setDisplayResultSet(rs);
        // Charts
        vm.getConnectionsChart().displayResultSetProperty().bind(pm.chartDisplayResultSetProperty());
    }

    protected void bindPresentationModelWithLogic(TesterPresentationModel pm) {
        // Drive
        Drive.getInstance().start(true);
        Drive.getInstance().requestedConnectionCountProperty().bind(pm.requestedConnectionsProperty());
        pm.startedConnectionsProperty().bind(Drive.getInstance().startedConnectionCountProperty());
        // Charts
        connectionChartGenerator.start();
        pm.chartDisplayResultSetProperty().bind(connectionChartGenerator.connectionListProperty());
    }
}
