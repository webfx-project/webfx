package naga.fx.spi.javafx.fx.viewer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Skin;
import naga.fx.scene.control.Control;
import naga.fx.spi.viewer.base.ControlViewerBase;
import naga.fx.spi.viewer.base.ControlViewerMixin;

/**
 * @author Bruno Salmon
 */
abstract class FxControlViewer
        <FxN extends javafx.scene.control.Control, N extends Control, NB extends ControlViewerBase<N, NB, NM>, NM extends ControlViewerMixin<N, NB, NM>>

        extends FxRegionViewer<FxN, N, NB, NM> {

    FxControlViewer(NB base) {
        super(base);
    }

    @Override
    protected void onFxNodeCreated() {
        Skin<?> skin = getFxNode().getSkin();
        if (skin == null)
            getFxNode().skinProperty().addListener(new ChangeListener<Skin<?>>() {
                @Override
                public void changed(ObservableValue<? extends Skin<?>> observableValue, Skin<?> skin, Skin<?> skin2) {
                    getFxNode().skinProperty().removeListener(this);
                    getNode().requestLayout();
                }
            });
    }
}
