package webfx.kit.mapper.peers.javafxgraphics;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import webfx.platform.shared.util.function.Factory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public final class NodePeerFactoryRegistry {

    private final static Map<Class<? extends Node>, Factory<? extends NodePeer>> nodePeerFactories = new HashMap<>();
    private static Function<Region, NodePeer<Region>> defaultRegionFactory;
    private static Function<Group, NodePeer<Group>> defaultGroupFactory;

    public static <N extends Node, V extends NodePeer<? super N>> void registerNodePeerFactory(Class<N> nodeClass, Factory<V> factory) {
        nodePeerFactories.put(nodeClass, factory);
    }

    public static void registerDefaultRegionPeerFactory(Function<Region, NodePeer<Region>> defaultRegionFactory) {
        NodePeerFactoryRegistry.defaultRegionFactory = defaultRegionFactory;
    }

    public static void registerDefaultGroupPeerFactory(Function<Group, NodePeer<Group>> defaultGroupFactory) {
        NodePeerFactoryRegistry.defaultGroupFactory = defaultGroupFactory;
    }

    public static <N extends Node, V extends NodePeer<N>> V createNodePeer(N node) {
        Factory<? extends NodePeer> factory = nodePeerFactories.get(node.getClass());
        if (factory != null)
            return (V) factory.create();
        if (node instanceof Region && defaultRegionFactory != null)
            return (V) defaultRegionFactory.apply((Region) node);
        if (node instanceof Group && defaultGroupFactory != null)
            return (V) defaultGroupFactory.apply((Group) node);
        System.out.println("WARNING: No NodePeer factory registered for " + node.getClass());
        return null;
    }
}
