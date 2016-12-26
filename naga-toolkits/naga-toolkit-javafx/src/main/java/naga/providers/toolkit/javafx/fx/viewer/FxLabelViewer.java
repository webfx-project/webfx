package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.scene.control.Label;
import naga.toolkit.fx.spi.viewer.base.LabelViewerBase;
import naga.toolkit.fx.spi.viewer.base.LabelViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxLabelViewer
        <FxN extends javafx.scene.control.Label, N extends Label, NB extends LabelViewerBase<N, NB, NM>, NM extends LabelViewerMixin<N, NB, NM>>

        extends FxLabeledViewer<FxN, N, NB, NM>
        implements LabelViewerMixin<N, NB, NM>, FxLayoutMeasurable {

    public FxLabelViewer() {
        this((NB) new LabelViewerBase());
    }

    FxLabelViewer(NB base) {
        super(base);
    }

    @Override
    protected FxN createFxNode() {
        return (FxN) new javafx.scene.control.Label();
    }

}
