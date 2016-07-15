package mongoose.activities.monitor;

import naga.framework.ui.presentation.ViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.charts.Chart;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.controls.Table;
import naga.toolkit.spi.nodes.controls.TextField;

/**
 * @author Bruno Salmon
 */
public class MonitorViewModel implements ViewModel {

    private final GuiNode contentNode;
    private final Table systemTable;
    private final TextField freeMemField;
    private final TextField totalMemField;
    private final Chart memChart;
    private final Button startButton ;
    private final Button stopButton ;

    public MonitorViewModel(GuiNode contentNode,
                            Table systemTable,
                            TextField freeMemField,
                            TextField totalMemField,
                            Chart memChart,
                            Button startButton,
                            Button stopButton) {
        this.contentNode = contentNode;
        this.systemTable = systemTable;
        this.freeMemField = freeMemField;
        this.totalMemField = totalMemField;
        this.memChart = memChart;
        this.startButton = startButton;
        this.stopButton = stopButton;
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

    public Chart getMemChart() {
        return memChart;
    }

    public Button<Integer> getStartButton() {
        return startButton;
    }

    public Button<Integer> getStopButton() {
        return stopButton;
    }

}
