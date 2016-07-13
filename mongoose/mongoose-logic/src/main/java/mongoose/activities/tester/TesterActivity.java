package mongoose.activities.tester;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.tester.drive.Drive;
import mongoose.activities.tester.drive.model.ConnectionChartGenerator;
import mongoose.activities.tester.drive.model.ConnectionData;
import mongoose.activities.tester.drive.model.ConnectionsChartData;
import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.charts.LineChart;
import naga.toolkit.spi.nodes.controls.Slider;
import naga.toolkit.spi.nodes.gauges.Gauge;
import naga.toolkit.spi.nodes.layouts.VBox;

/**
 * @author Bruno Salmon
 */
public class TesterActivity extends PresentationActivity<TesterViewModel, TesterPresentationModel> {

    public TesterActivity() {
        super(TesterPresentationModel::new);
    }

    private ObjectProperty<ConnectionData> connectionsToDisplay = new SimpleObjectProperty<>(new ConnectionsChartData());

    protected TesterViewModel buildView(Toolkit toolkit) {
        // Sliders
        Slider requestedSlider = toolkit.createSlider();
        Gauge startedSlider = toolkit.createGauge();
        // Charts
        LineChart connectionsChart = toolkit.createLineChart();
        // Arranging in boxes
        VBox vBox = toolkit.createVBox();
        vBox.getChildren().setAll(requestedSlider, startedSlider);
        // Building the UI components
        return new TesterViewModel(toolkit.createVPage()
                    .setHeader(vBox)
                    .setCenter(connectionsChart),
                connectionsChart,
                requestedSlider,
                startedSlider);
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
        // Charts
        vm.getConnectionsChart().displayResultSetProperty().bind(pm.chartDisplayResultSetProperty());
    }

    protected void bindPresentationModelWithLogic(TesterPresentationModel pm) {
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
}
