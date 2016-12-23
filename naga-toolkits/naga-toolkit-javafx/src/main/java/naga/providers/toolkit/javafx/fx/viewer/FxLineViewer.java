package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.scene.shape.Line;
import naga.toolkit.fx.spi.viewer.base.LineViewerBase;
import naga.toolkit.fx.spi.viewer.base.LineViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxLineViewer
        <FxN extends javafx.scene.shape.Line, N extends Line, NB extends LineViewerBase<N, NB, NM>, NM extends LineViewerMixin<N, NB, NM>>

        extends FxShapeViewer<FxN, N, NB, NM>
        implements LineViewerMixin<N, NB, NM> {

    public FxLineViewer() {
        super((NB) new LineViewerBase());
    }

    @Override
    protected FxN createFxNode() {
        return (FxN) new javafx.scene.shape.Line();
    }

    @Override
    public void updateStartX(Double startX) {
        getFxNode().setStartX(startX);
    }

    @Override
    public void updateStartY(Double startY) {
        getFxNode().setStartY(startY);
    }

    @Override
    public void updateEndX(Double endX) {
        getFxNode().setEndX(endX);
    }

    @Override
    public void updateEndY(Double endY) {
        getFxNode().setEndY(endY);
    }
}
