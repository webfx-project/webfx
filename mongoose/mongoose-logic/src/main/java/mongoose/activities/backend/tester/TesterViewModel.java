package mongoose.activities.backend.tester;

import naga.framework.ui.presentation.ViewModelBase;
import naga.fxdata.chart.Chart;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;

/**
 * @author Bruno Salmon
 */
class TesterViewModel extends ViewModelBase<Node> {

    private final Button saveTest;
    private final Chart connectionsChart;
    private final Slider requestSlider ;
    private final Slider startedSlider ;

    TesterViewModel(Node contentNode, Button saveTest, Chart connectionsChart, Slider requestSlider, Slider startedSlider) {
        super(contentNode);
        this.saveTest = saveTest;
        this.connectionsChart = connectionsChart;
        this.requestSlider = requestSlider;
        this.startedSlider = startedSlider;
    }

    Button getSaveTest() {
        return saveTest;
    }

    Chart getConnectionsChart() {
        return connectionsChart;
    }

    Slider getRequestSlider() {
        return requestSlider;
    }

    Slider getStartedSlider() {
        return startedSlider;
    }
}
