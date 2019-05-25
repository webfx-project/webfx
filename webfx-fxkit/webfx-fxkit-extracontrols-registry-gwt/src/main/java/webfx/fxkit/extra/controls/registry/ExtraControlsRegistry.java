package webfx.fxkit.extra.controls.registry;

import webfx.fxkit.extra.controls.displaydata.chart.*;
import webfx.fxkit.extra.controls.displaydata.datagrid.DataGrid;
import webfx.fxkit.extra.controls.html.HtmlText;
import webfx.fxkit.extra.controls.html.HtmlTextEditor;
import webfx.fxkit.extra.controls.mapper.spi.impl.peer.gwt.html.*;
import webfx.fxkit.extra.controls.svg.SvgText;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.html.HtmlSvgTextPeer;

import static webfx.fxkit.javafxgraphics.mapper.spi.NodePeerFactoryRegistry.registerNodePeerFactory;

public class ExtraControlsRegistry {

    static {
/*
        registerSvgText();
        registerHtmlText();
        registerHtmlTextEditor();
        registerDataGrid();
        registerLineChart();
        registerAreaChart();
        registerBarChart();
        registerPieChart();
        registerScatterChart();
*/
    }

    public static void registerSvgText() {
        registerNodePeerFactory(SvgText.class, HtmlSvgTextPeer::new);
    }

    public static void registerHtmlText() {
        registerNodePeerFactory(HtmlText.class, HtmlHtmlTextPeer::new);
    }

    public static void registerHtmlTextEditor() {
        registerNodePeerFactory(HtmlTextEditor.class, HtmlHtmlTextEditorPeer::new);
    }

    public static void registerDataGrid() {
        registerNodePeerFactory(DataGrid.class, HtmlDataGridPeer::new);
    }

    public static void registerLineChart() {
        registerNodePeerFactory(LineChart.class, HtmlLineChartPeer::new);
    }

    public static void registerAreaChart() {
        registerNodePeerFactory(AreaChart.class, HtmlAreaChartPeer::new);
    }

    public static void registerBarChart() {
        registerNodePeerFactory(BarChart.class, HtmlBarChartPeer::new);
    }

    public static void registerPieChart() {
        registerNodePeerFactory(PieChart.class, HtmlPieChartPeer::new);
    }

    public static void registerScatterChart() {
        registerNodePeerFactory(ScatterChart.class, HtmlScatterChartPeer::new);
    }
}
