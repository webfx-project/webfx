package naga.fx.spi.viewer.base;

import naga.fx.scene.Node;

/**
 * @author Bruno Salmon
 */
public abstract class NodeViewerImpl
        <N extends Node, NB extends NodeViewerBase<N, NB, NM>, NM extends NodeViewerMixin<N, NB, NM>>
        implements NodeViewerMixin<N, NB, NM> {

    private final NB base;

    public NodeViewerImpl(NB base) {
        this.base = base;
        base.setMixin((NM) this);
    }

    @Override
    public NB getNodeViewerBase() {
        return base;
    }

    public N getNode() {
        return base.getNode();
    }
}
