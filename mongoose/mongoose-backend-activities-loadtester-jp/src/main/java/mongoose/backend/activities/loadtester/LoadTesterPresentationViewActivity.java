package mongoose.backend.activities.loadtester;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import webfx.framework.client.activity.impl.elementals.presentation.view.impl.PresentationViewActivityImpl;
import webfx.extras.visual.controls.charts.VisualLineChart;

/**
 * @author Bruno Salmon
 */
final class LoadTesterPresentationViewActivity extends PresentationViewActivityImpl<LoadTesterPresentationModel> {

    private VisualLineChart connectionsChart;
    private Button saveTest;
    private VBox vBox;

    private final int MAX_CONNECTIONS = 3000;

    @Override
    protected void createViewNodes(LoadTesterPresentationModel pm) {
        Slider requestedSlider = new Slider();
        requestedSlider.setMin(0);
        requestedSlider.setMax(MAX_CONNECTIONS);
        Slider startedSlider = new Slider();
        startedSlider.setMin(0);
        startedSlider.setMax(MAX_CONNECTIONS);

        connectionsChart = new VisualLineChart();
        vBox = new VBox(requestedSlider, startedSlider);
        saveTest = new Button("Save Test");

        // Data binding
        pm.requestedConnectionsProperty().bind(requestedSlider.valueProperty());
        startedSlider.valueProperty().bind(pm.startedConnectionsProperty());
        connectionsChart.visualResultProperty().bind(pm.chartVisualResultProperty());
    }

    @Override
    protected Node assemblyViewNodes() {
        return new BorderPane(connectionsChart, vBox, null, saveTest, null);
    }
}
