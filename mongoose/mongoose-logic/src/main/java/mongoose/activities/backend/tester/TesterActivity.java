package mongoose.activities.backend.tester;

import mongoose.activities.backend.tester.drive.Drive;
import mongoose.activities.backend.tester.drive.model.ConnectionChartGenerator;
import naga.framework.ui.presentation.PresentationActivity;
import naga.fxdata.chart.LineChart;
import naga.fx.scene.control.Button;
import naga.fx.scene.control.Slider;
import naga.fx.scene.layout.BorderPane;
import naga.fx.scene.layout.VBox;
import naga.fx.properties.conversion.ConvertedProperty;

/**
 * @author Bruno Salmon
 */
public class TesterActivity extends PresentationActivity<TesterViewModel, TesterPresentationModel> {
    private ConnectionChartGenerator connectionChartGenerator = new ConnectionChartGenerator();

    public TesterActivity() {
        super(TesterPresentationModel::new);
    }

    protected TesterViewModel buildView() {
        // Sliders
        Slider requestedSlider = new Slider();
        Slider startedSlider = new Slider();
        // Charts
        LineChart connectionsChart = new LineChart();
        // Arranging in boxes
        VBox vBox = new VBox();
        vBox.getChildren().setAll(requestedSlider, startedSlider);
        // Buttons
        Button saveTest = new Button("Save Test");
        // Building the UI components
        return new TesterViewModel(new BorderPane(connectionsChart, vBox, null, saveTest, null),
                saveTest, connectionsChart, requestedSlider, startedSlider);
    }

    protected void bindViewModelWithPresentationModel(TesterViewModel vm, TesterPresentationModel pm) {
        vm.getSaveTest().setOnMouseClicked(e -> {
            getHistory().push("/testSet");
//            Drive.getInstance().recordTestSet(getDataSourceModel(), pm.testNameProperty().getValue(), pm.testCommentProperty().getValue());
            connectionChartGenerator.reset();
//            pm.chartDisplayResultSetProperty().bind(connectionChartGenerator.connectionListProperty());
        });
        // Sliders
        vm.getStartedSlider().setMin(0d);
        vm.getStartedSlider().setMax(3000d);
        vm.getRequestSlider().setMin(0d);
        vm.getRequestSlider().setMax(3000d);
        pm.requestedConnectionsProperty().bind(ConvertedProperty.doubleToIntegerProperty(vm.getRequestSlider().valueProperty()));
        vm.getStartedSlider().valueProperty().bind(ConvertedProperty.integerToDoubleProperty(pm.startedConnectionsProperty()));
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
