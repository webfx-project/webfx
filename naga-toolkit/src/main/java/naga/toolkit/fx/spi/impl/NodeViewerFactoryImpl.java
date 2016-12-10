package naga.toolkit.fx.spi.impl;

import naga.commons.util.function.Factory;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.spi.viewer.NodeViewer;
import naga.toolkit.fx.spi.viewer.NodeViewerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class NodeViewerFactoryImpl implements NodeViewerFactory {

    private Map<Class<? extends Node>, Factory<? extends NodeViewer>> nodeViewerFactories = new HashMap<>();

    public <N extends Node, V extends NodeViewer<? super N>> void registerNodeViewerFactory(Class<N> nodeClass, Factory<V> factory) {
        nodeViewerFactories.put(nodeClass, factory);
    }

    @Override
    public <N extends Node, V extends NodeViewer<N>> V createNodeViewer(N nodeInterface) {
        Factory<? extends NodeViewer> factory = nodeViewerFactories.get(nodeInterface.getClass());
        if (factory != null)
            return (V) factory.create();
        System.out.println("WARNING: No NodeViewer factory registered for " + nodeInterface.getClass() + " in " + getClass());
        return null;
    }
}
