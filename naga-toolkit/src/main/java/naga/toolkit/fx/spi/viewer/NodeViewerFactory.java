package naga.toolkit.fx.spi.viewer;

import naga.toolkit.fx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface NodeViewerFactory {

    <N extends Node, V extends NodeViewer<N>> V createNodeViewer(N nodeInterface);

}
