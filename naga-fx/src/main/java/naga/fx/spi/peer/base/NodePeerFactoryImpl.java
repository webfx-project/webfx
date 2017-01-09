package naga.fx.spi.peer.base;

import naga.commons.util.function.Factory;
import naga.fx.scene.Node;
import naga.fx.scene.layout.Region;
import naga.fx.spi.peer.NodePeer;
import naga.fx.spi.peer.NodePeerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class NodePeerFactoryImpl implements NodePeerFactory {

    private Map<Class<? extends Node>, Factory<? extends NodePeer>> nodePeerFactories = new HashMap<>();

    protected <N extends Node, V extends NodePeer<? super N>> void registerNodePeerFactory(Class<N> nodeClass, Factory<V> factory) {
        nodePeerFactories.put(nodeClass, factory);
    }

    @Override
    public <N extends Node, V extends NodePeer<N>> V createNodePeer(N node) {
        Factory<? extends NodePeer> factory = nodePeerFactories.get(node.getClass());
        if (factory != null)
            return (V) factory.create();
        if (node instanceof Region)
            return (V) createDefaultRegionPeer((Region) node);
        System.out.println("WARNING: No NodePeer factory registered for " + ((Node) node).getClass() + " in " + getClass());
        return null;
    }

    protected abstract NodePeer<Region> createDefaultRegionPeer(Region node);
}
