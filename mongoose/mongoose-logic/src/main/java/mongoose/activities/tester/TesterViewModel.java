package mongoose.activities.tester;

import naga.toolkit.spi.nodes.charts.Chart;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.controls.Slider;
import naga.toolkit.spi.nodes.controls.Table;
import naga.toolkit.spi.nodes.controls.TextField;
import naga.toolkit.spi.nodes.gauges.Gauge;
import naga.toolkit.spi.nodes.GuiNode;
import naga.framework.ui.presentation.ViewModel;

/**
 * @author Bruno Salmon
 */
public class TesterViewModel implements ViewModel {

    private final GuiNode contentNode;
    private final Table systemTable;
    private final TextField freeMemField;
    private final TextField totalMemField;
    private final Chart connectionsChart;
    private final Slider requestSlider ;
    private final Gauge startedSlider ;
    private final Button startButton ;
    private final Button stopButton ;
    private final Button exitButton ;

    public TesterViewModel(GuiNode contentNode,
                           Table systemTable,
                           TextField freeMemField,
                           TextField totalMemField,
                           Chart connectionsChart,
                           Slider requestSlider,
                           Gauge startedSlider,
                           Button<Integer> startButton,
                           Button<Integer> stopButton,
                           Button<Integer> exitButton) {
        this.contentNode = contentNode;
        this.systemTable = systemTable;
        this.freeMemField = freeMemField;
        this.totalMemField = totalMemField;
        this.connectionsChart = connectionsChart;
        this.requestSlider = requestSlider;
        this.startedSlider = startedSlider;
        this.startButton = startButton;
        this.stopButton = stopButton;
        this.exitButton = exitButton;
    }

    @Override
    public GuiNode getContentNode() {
        return contentNode;
    }

    public Table getSystemTable() {
        return systemTable;
    }

    public TextField getFreeMemField() {
        return freeMemField;
    }

    public TextField getTotalMemField() {
        return totalMemField;
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

    public Button<Integer> getStartButton() {
        return startButton;
    }

    public Button<Integer> getStopButton() {
        return stopButton;
    }

    public Button<Integer> getExitButton() {
        return exitButton;
    }

}
