package webfx.extras.visual.controls.charts.registry.spi.impl.javafx;

import webfx.extras.visual.controls.charts.*;
import webfx.extras.visual.controls.charts.peers.javafx.*;
import webfx.extras.visual.controls.charts.registry.spi.VisualChartsRegistryProvider;

import static webfx.kit.mapper.peers.javafxgraphics.NodePeerFactoryRegistry.registerNodePeerFactory;

public class JavaFxVisualChartsRegistryProvider implements VisualChartsRegistryProvider {

    public void registerLineChart() {
        registerNodePeerFactory(VisualLineChart.class, FxVisualLineChartPeer::new);
    }

    public void registerAreaChart() {
        registerNodePeerFactory(VisualAreaChart.class, FxVisualAreaChartPeer::new);
    }

    public void registerBarChart() {
        registerNodePeerFactory(VisualBarChart.class, FxVisualBarChartPeer::new);
    }

    public void registerPieChart() {
        registerNodePeerFactory(VisualPieChart.class, FxVisualPieChartPeer::new);
    }

    public void registerScatterChart() {
        registerNodePeerFactory(VisualScatterChart.class, FxVisualScatterChartPeer::new);
    }

}
