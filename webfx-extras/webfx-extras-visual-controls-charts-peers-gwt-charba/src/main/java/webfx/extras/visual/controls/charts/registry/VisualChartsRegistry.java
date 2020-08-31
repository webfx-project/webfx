package webfx.extras.visual.controls.charts.registry;

import webfx.extras.visual.controls.charts.*;
import webfx.extras.visual.controls.charts.peers.gwt.charba.*;

import static webfx.kit.mapper.peers.javafxgraphics.NodePeerFactoryRegistry.registerNodePeerFactory;

public class VisualChartsRegistry {

    public static void registerLineChart() {
        registerNodePeerFactory(VisualLineChart.class, GwtCharbaVisualLineChartPeer::new);
    }

    public static void registerAreaChart() {
        registerNodePeerFactory(VisualAreaChart.class, GwtCharbaVisualAreaChartPeer::new);
    }

    public static void registerBarChart() {
        registerNodePeerFactory(VisualBarChart.class, GwtCharbaVisualBarChartPeer::new);
    }

    public static void registerPieChart() {
        registerNodePeerFactory(VisualPieChart.class, GwtCharbaVisualPieChartPeer::new);
    }

    public static void registerScatterChart() {
        registerNodePeerFactory(VisualScatterChart.class, GwtCharbaVisualScatterChartPeer::new);
    }
}
