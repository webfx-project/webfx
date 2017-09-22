package naga.fx.spi.javafx;

import javafx.scene.layout.Region;
import naga.fx.spi.javafx.peer.*;
import naga.fx.spi.peer.NodePeer;
import naga.fx.spi.peer.base.NodePeerFactoryImpl;
import naga.fxdata.cell.collator.GridCollator;
import naga.fxdata.chart.*;
import naga.fxdata.control.HtmlText;
import naga.fxdata.control.HtmlTextEditor;
import naga.fxdata.control.ToolkitDataGrid;

/**
 * @author Bruno Salmon
 */
public class FxNodePeerFactory extends NodePeerFactoryImpl {

    public final static FxNodePeerFactory SINGLETON = new FxNodePeerFactory();

    protected FxNodePeerFactory() {
        registerNodePeerFactory(HtmlText.class, FxHtmlTextPeer::new);
        registerNodePeerFactory(HtmlTextEditor.class, FxHtmlTextEditorPeer::new);
        registerNodePeerFactory(ToolkitDataGrid.class, FxDataGridPeer::new);
        registerNodePeerFactory(AreaChart.class, FxAreaChartPeer::new);
        registerNodePeerFactory(BarChart.class, FxBarChartPeer::new);
        registerNodePeerFactory(LineChart.class, FxLineChartPeer::new);
        registerNodePeerFactory(PieChart.class, FxPieChartPeer::new);
        registerNodePeerFactory(ScatterChart.class, FxScatterChartPeer::new);
        registerNodePeerFactory(GridCollator.class, GridCollator.GridCollatorPeer::new);
    }

    @Override
    protected NodePeer<Region> createDefaultRegionPeer(Region node) {
        return null;
    }
}
