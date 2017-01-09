package naga.fx.spi.viewer.base;

import naga.commons.util.function.Factory;
import naga.fx.scene.Node;
import naga.fx.scene.layout.Region;
import naga.fx.spi.viewer.NodeViewer;
import naga.fx.spi.viewer.NodeViewerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class NodeViewerFactoryImpl implements NodeViewerFactory {

    private Map<Class<? extends Node>, Factory<? extends NodeViewer>> nodeViewerFactories = new HashMap<>();

    protected <N extends Node, V extends NodeViewer<? super N>> void registerNodeViewerFactory(Class<N> nodeClass, Factory<V> factory) {
        nodeViewerFactories.put(nodeClass, factory);
    }

    @Override
    public <N extends Node, V extends NodeViewer<N>> V createNodeViewer(N node) {
        Factory<? extends NodeViewer> factory = nodeViewerFactories.get(node.getClass());
        if (factory != null)
            return (V) factory.create();
        if (node instanceof Region)
            return (V) createDefaultRegionViewer((Region) node);
        System.out.println("WARNING: No NodeViewer factory registered for " + ((Node) node).getClass() + " in " + getClass());
        return null;
    }

    protected abstract NodeViewer<Region> createDefaultRegionViewer(Region node);
}
