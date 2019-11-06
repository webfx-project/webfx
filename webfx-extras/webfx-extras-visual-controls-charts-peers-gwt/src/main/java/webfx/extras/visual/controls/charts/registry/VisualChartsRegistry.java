package webfx.extras.visual.controls.charts.registry;

import webfx.extras.visual.controls.charts.*;
import webfx.extras.visual.controls.charts.peers.gwt.html.*;

import static webfx.kit.mapper.peers.javafxgraphics.NodePeerFactoryRegistry.registerNodePeerFactory;

public class VisualChartsRegistry {

    public static void registerLineChart() {
        registerNodePeerFactory(VisualLineChart.class, HtmlVisualLineChartPeer::new);
    }

    public static void registerAreaChart() {
        registerNodePeerFactory(VisualAreaChart.class, HtmlVisualAreaChartPeer::new);
    }

    public static void registerBarChart() {
        registerNodePeerFactory(VisualBarChart.class, HtmlVisualBarChartPeer::new);
    }

    public static void registerPieChart() {
        registerNodePeerFactory(VisualPieChart.class, HtmlVisualPieChartPeer::new);
    }

    public static void registerScatterChart() {
        registerNodePeerFactory(VisualScatterChart.class, HtmlVisualScatterChartPeer::new);
    }
}
