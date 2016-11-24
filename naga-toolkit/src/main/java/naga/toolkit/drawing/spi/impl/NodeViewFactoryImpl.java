package naga.toolkit.drawing.spi.impl;

import naga.commons.util.function.Factory;
import naga.toolkit.drawing.scene.Node;
import naga.toolkit.drawing.spi.view.NodeView;
import naga.toolkit.drawing.spi.view.NodeViewFactory;
import naga.toolkit.drawing.spi.view.UnimplementedNodeView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class NodeViewFactoryImpl implements NodeViewFactory {

    private Map<Class<? extends Node>, Factory<? extends NodeView>> nodeViewFactories = new HashMap<>();

    public <N extends Node, V extends NodeView<? super N>> void registerNodeViewFactory(Class<N> nodeClass, Factory<V> factory) {
        nodeViewFactories.put(nodeClass, factory);
    }

    @Override
    public <N extends Node, V extends NodeView<N>> V createNodeView(N nodeInterface) {
        Factory<? extends NodeView> factory = nodeViewFactories.get(nodeInterface.getClass());
        if (factory != null)
            return (V) factory.create();
        System.out.println("WARNING: No NodeView factory registered for " + nodeInterface.getClass() + " in " + getClass());
        return (V) new UnimplementedNodeView();
    }
}
