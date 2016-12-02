package naga.toolkit.fx.spi.view;

import naga.toolkit.fx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface NodeViewFactory {

    <N extends Node, V extends NodeView<N>> V createNodeView(N nodeInterface);

}
