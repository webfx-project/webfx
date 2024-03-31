package dev.webfx.kit.mapper.peers.javafxgraphics;

import dev.webfx.platform.console.Console;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Region;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Bruno Salmon
 */
public final class NodePeerFactoryRegistry {

    private final static Map<Class<? extends Node>, Supplier<? extends NodePeer>> nodePeerFactories = new HashMap<>();
    private final static Map<Class<? extends Node>, Function<String, ? extends NodePeer>> customTagNodePeerFactories = new HashMap<>();
    private static Function<Region, NodePeer<Region>> defaultRegionFactory;
    private static Function<Group, NodePeer<Group>> defaultGroupFactory;

    public static <N extends Node, V extends NodePeer<? super N>> void registerNodePeerFactory(Class<N> nodeClass, Supplier<V> factory) {
        nodePeerFactories.put(nodeClass, factory);
    }

    public static <N extends Node, V extends NodePeer<? super N>> void registerCustomTagNodePeerFactory(Class<N> nodeClass, Function<String, V> factory) {
        customTagNodePeerFactories.put(nodeClass, factory);
    }

    public static void registerDefaultRegionPeerFactory(Function<Region, NodePeer<Region>> defaultRegionFactory) {
        NodePeerFactoryRegistry.defaultRegionFactory = defaultRegionFactory;
    }

    public static void registerDefaultGroupPeerFactory(Function<Group, NodePeer<Group>> defaultGroupFactory) {
        NodePeerFactoryRegistry.defaultGroupFactory = defaultGroupFactory;
    }

    public static String requestedCustomTag(Node node) {
        return (String) node.getProperties().get("webfx-htmlTag");
    }

    public static String classTag(Node node) {
        return "fx-" + node.getClass().getSimpleName().toLowerCase();
    }

    public static <N extends Node, V extends NodePeer<N>> V createNodePeer(N node) {
        Class<?> nodeClass = node.getClass();
        String customTag = requestedCustomTag(node);
        // Searching the peer factory associated with the node class
        Function<String, ? extends NodePeer> customTagFactory;
        if (customTag != null) {
            customTagFactory = customTagNodePeerFactories.get(nodeClass);
            if (customTagFactory != null)
                return (V) customTagFactory.apply(customTag);
        }
        Supplier<? extends NodePeer> factory = nodePeerFactories.get(nodeClass);
        if (factory != null)
            return (V) factory.get();
        // If not found, it can be because it's a derived class
        // For regions and groups, we delegate this search to their default factory
        if (node instanceof Region && defaultRegionFactory != null)
            return (V) defaultRegionFactory.apply((Region) node);
        if (node instanceof Group && defaultGroupFactory != null)
            return (V) defaultGroupFactory.apply((Group) node);
        // For other nodes, we search recursively in super classes
        while (nodeClass != null) {
            nodeClass = nodeClass.getSuperclass();
            if (customTag != null) {
                customTagFactory = customTagNodePeerFactories.get(nodeClass);
                if (customTagFactory != null)
                    return (V) customTagFactory.apply(customTag);
            }
            factory = nodePeerFactories.get(nodeClass);
            if (factory != null)
                return (V) factory.get();
        }
        // If still not found, we return null after logging the problem
        Console.log("WARNING: No NodePeer factory registered for " + node.getClass());
        return null;
    }
}
