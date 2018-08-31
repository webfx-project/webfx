package webfx.fx.spi.javafx;

import javafx.scene.layout.Region;
import webfx.fx.spi.javafx.peer.*;
import webfx.fx.spi.peer.NodePeer;
import webfx.fx.spi.peer.base.NodePeerFactoryImpl;
import webfx.fxdata.cell.collator.GridCollator;
import webfx.fxdata.chart.*;
import webfx.fxdata.control.DataGrid;
import webfx.fxdata.control.HtmlText;
import webfx.fxdata.control.HtmlTextEditor;

/**
 * @author Bruno Salmon
 */
public class FxNodePeerFactory extends NodePeerFactoryImpl {

    public final static FxNodePeerFactory SINGLETON = new FxNodePeerFactory();

    protected FxNodePeerFactory() {
        registerNodePeerFactory(HtmlText.class, FxHtmlTextPeer::new);
        registerNodePeerFactory(HtmlTextEditor.class, FxHtmlTextEditorPeer::new);
        registerNodePeerFactory(DataGrid.class, FxDataGridPeer::new);
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
