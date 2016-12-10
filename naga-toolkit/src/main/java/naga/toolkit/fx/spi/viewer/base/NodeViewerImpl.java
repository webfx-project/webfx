package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.Node;

/**
 * @author Bruno Salmon
 */
public abstract class NodeViewerImpl
        <N extends Node, NV extends NodeViewerBase<N, NV, NM>, NM extends NodeViewerMixin<N, NV, NM>>
        implements NodeViewerMixin<N, NV, NM> {

    private final NV base;

    public NodeViewerImpl(NV base) {
        this.base = base;
        base.setMixin((NM) this);
    }

    @Override
    public NV getNodeViewerBase() {
        return base;
    }

    public N getNode() {
        return base.getNode();
    }
}
