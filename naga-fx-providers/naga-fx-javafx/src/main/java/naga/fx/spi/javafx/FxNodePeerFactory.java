package naga.fx.spi.javafx;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import naga.commons.util.function.Factory;
import naga.fx.scene.SceneRequester;
import naga.fx.spi.javafx.peer.*;
import naga.fx.spi.peer.NodePeer;
import naga.fx.spi.peer.base.NodePeerFactoryImpl;
import naga.fxdata.chart.*;
import naga.fxdata.control.DataGrid;
import naga.fxdata.control.HtmlText;

/**
 * @author Bruno Salmon
 */
public class FxNodePeerFactory extends NodePeerFactoryImpl {

    public final static FxNodePeerFactory SINGLETON = new FxNodePeerFactory();

    protected FxNodePeerFactory() {
        registerNodePeerFactory(HtmlText.class, FxHtmlTextPeer::new);
        registerNodePeerFactory(DataGrid.class, FxDataGridPeer::new);
        registerNodePeerFactory(AreaChart.class, FxAreaChartPeer::new);
        registerNodePeerFactory(BarChart.class, FxBarChartPeer::new);
        registerNodePeerFactory(LineChart.class, FxLineChartPeer::new);
        registerNodePeerFactory(PieChart.class, FxPieChartPeer::new);
        registerNodePeerFactory(ScatterChart.class, FxScatterChartPeer::new);
    }

    @Override
    public <N extends Node, V extends NodePeer<N>> V createNodePeer(N node) {
        Factory<? extends NodePeer> factory = nodePeerFactories.get(node.getClass());
        if (factory != null)
            return (V) factory.create();
        return (V) new NodePeer<N>() {
            @Override
            public void bind(N node, SceneRequester sceneRequester) {
            }

            @Override
            public void unbind() {
            }

            @Override
            public boolean updateProperty(ObservableValue changedProperty) {
                return false;
            }

            @Override
            public boolean updateList(ObservableList changedList) {
                return false;
            }

            @Override
            public void requestFocus() {
                node.requestFocus();
            }
        };
    }

    @Override
    protected NodePeer<Region> createDefaultRegionPeer(Region node) {
        return null;
    }
}
