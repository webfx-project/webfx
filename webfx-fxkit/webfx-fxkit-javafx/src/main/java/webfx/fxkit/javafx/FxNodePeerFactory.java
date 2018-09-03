package webfx.fxkit.javafx;

import javafx.scene.layout.Region;
import webfx.fxkit.javafx.peer.*;
import webfx.fxkits.core.spi.peer.NodePeer;
import webfx.fxkits.core.spi.peer.base.NodePeerFactoryImpl;
import webfx.fxkits.extra.cell.collator.GridCollator;
import webfx.fxkits.extra.chart.*;
import webfx.fxkits.extra.control.DataGrid;
import webfx.fxkits.extra.control.HtmlText;
import webfx.fxkits.extra.control.HtmlTextEditor;

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
