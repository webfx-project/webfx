package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.shapes.Node;

/**
 * @author Bruno Salmon
 */
public abstract class NodeViewImpl
        <N extends Node, NV extends NodeViewBase<N, NV, NM>, NM extends NodeViewMixin<N, NV, NM>>
        implements NodeViewMixin<N, NV, NM> {

    private final NV base;

    public NodeViewImpl(NV base) {
        this.base = base;
        base.setMixin((NM) this);
    }

    @Override
    public NV getNodeViewBase() {
        return base;
    }

    public N getNode() {
        return base.getNode();
    }
}
