package mongooses.loadtester.activities.loadtester;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import webfx.framework.activity.impl.elementals.presentation.view.impl.PresentationViewActivityImpl;
import webfx.fxkits.core.util.properties.conversion.ConvertedProperty;
import webfx.fxkits.extra.chart.LineChart;

/**
 * @author Bruno Salmon
 */
final class LoadTesterPresentationViewActivity extends PresentationViewActivityImpl<LoadTesterPresentationModel> {

    private LineChart connectionsChart;
    private Button saveTest;
    private VBox vBox;

    @Override
    protected void createViewNodes(LoadTesterPresentationModel pm) {
        Slider requestedSlider = new Slider();
        requestedSlider.setMin(0d);
        requestedSlider.setMax(3000d);
        Slider startedSlider = new Slider();
        startedSlider.setMin(0d);
        startedSlider.setMax(3000d);

        connectionsChart = new LineChart();
        vBox = new VBox(requestedSlider, startedSlider);
        saveTest = new Button("Save Test");

        // Data binding
        pm.requestedConnectionsProperty().bind(ConvertedProperty.doubleToIntegerProperty((Property) requestedSlider.valueProperty()));
        startedSlider.valueProperty().bind(ConvertedProperty.integerToDoubleProperty(pm.startedConnectionsProperty()));
        connectionsChart.displayResultProperty().bind(pm.chartDisplayResultProperty());
    }

    @Override
    protected Node assemblyViewNodes() {
        return new BorderPane(connectionsChart, vBox, null, saveTest, null);
    }
}
