package mongoose.activities.tester;

import naga.framework.ui.presentation.ViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.charts.Chart;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.controls.Slider;
import naga.toolkit.spi.nodes.controls.TextField;
import naga.toolkit.spi.nodes.gauges.Gauge;

/**
 * @author Bruno Salmon
 */
public class TesterViewModel implements ViewModel {

    private final GuiNode contentNode;
    private final TextField testName;
    private final TextField testComment;
    private final Button createTest;
    private final Chart connectionsChart;
    private final Slider requestSlider ;
    private final Gauge startedSlider ;

    public TesterViewModel(GuiNode contentNode,
                           TextField testName,
                           TextField testComment,
                           Button createTest,
                           Chart connectionsChart,
                           Slider requestSlider,
                           Gauge startedSlider) {
        this.contentNode = contentNode;
        this.testName = testName;
        this.testComment = testComment;
        this.createTest = createTest;
        this.connectionsChart = connectionsChart;
        this.requestSlider = requestSlider;
        this.startedSlider = startedSlider;
    }

    @Override
    public GuiNode getContentNode() {
        return contentNode;
    }

    public TextField getTestName() {
        return testName;
    }

    public TextField getTestComment() {
        return testComment;
    }

    public Button getCreateTest() {
        return createTest;
    }

    public Chart getConnectionsChart() {
        return connectionsChart;
    }

    public Slider getRequestSlider() {
        return requestSlider;
    }

    public Gauge getStartedSlider() {
        return startedSlider;
    }
}
