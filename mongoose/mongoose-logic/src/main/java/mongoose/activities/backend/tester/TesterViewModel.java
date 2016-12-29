package mongoose.activities.backend.tester;

import naga.framework.ui.presentation.AbstractViewModel;
import naga.fxdata.chart.Chart;
import naga.fx.scene.Node;
import naga.fx.scene.control.Button;
import naga.fx.scene.control.Slider;

/**
 * @author Bruno Salmon
 */
class TesterViewModel extends AbstractViewModel<Node> {

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
