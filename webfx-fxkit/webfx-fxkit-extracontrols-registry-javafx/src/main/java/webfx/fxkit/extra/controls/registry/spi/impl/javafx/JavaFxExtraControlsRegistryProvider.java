package webfx.fxkit.extra.controls.registry.spi.impl.javafx;

import webfx.fxkit.extra.cell.collator.grid.GridCollator;
import webfx.fxkit.extra.controls.displaydata.chart.*;
import webfx.fxkit.extra.controls.displaydata.datagrid.DataGrid;
import webfx.fxkit.extra.controls.html.HtmlText;
import webfx.fxkit.extra.controls.html.HtmlTextEditor;
import webfx.fxkit.extra.controls.mapper.spi.impl.peer.javafx.*;
import webfx.fxkit.extra.controls.registry.spi.ExtraControlsRegistryProvider;

import static webfx.fxkit.javafxgraphics.mapper.spi.NodePeerFactoryRegistry.registerNodePeerFactory;

public class JavaFxExtraControlsRegistryProvider implements ExtraControlsRegistryProvider {

    static {
        registerNodePeerFactory(GridCollator.class, GridCollator.GridCollatorPeer::new);
    }

    public void registerSvgText() {
    }

    public void registerHtmlText() {
        registerNodePeerFactory(HtmlText.class, FxHtmlTextPeer::new);
    }

    public void registerHtmlTextEditor() {
        registerNodePeerFactory(HtmlTextEditor.class, FxHtmlTextEditorPeer::new);
    }

    public void registerDataGrid() {
        registerNodePeerFactory(DataGrid.class, FxDataGridPeer::new);
    }

    public void registerLineChart() {
        registerNodePeerFactory(LineChart.class, FxLineChartPeer::new);
    }

    public void registerAreaChart() {
        registerNodePeerFactory(AreaChart.class, FxAreaChartPeer::new);
    }

    public void registerBarChart() {
        registerNodePeerFactory(BarChart.class, FxBarChartPeer::new);
    }

    public void registerPieChart() {
        registerNodePeerFactory(PieChart.class, FxPieChartPeer::new);
    }

    public void registerScatterChart() {
        registerNodePeerFactory(ScatterChart.class, FxScatterChartPeer::new);
    }

}
