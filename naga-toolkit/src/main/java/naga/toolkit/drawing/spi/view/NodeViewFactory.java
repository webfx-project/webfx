package naga.toolkit.drawing.spi.view;

import naga.toolkit.drawing.shapes.Node;

/**
 * @author Bruno Salmon
 */
public interface NodeViewFactory {

    <N extends Node, V extends NodeView<N>> V createNodeView(N nodeInterface);

}
