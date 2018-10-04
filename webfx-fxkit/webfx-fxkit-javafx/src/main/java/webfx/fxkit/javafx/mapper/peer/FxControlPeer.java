package webfx.fxkit.javafx.mapper.peer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Skin;
import javafx.scene.control.Control;
import webfx.fxkit.mapper.spi.impl.peer.ControlPeerBase;
import webfx.fxkit.mapper.spi.impl.peer.ControlPeerMixin;

/**
 * @author Bruno Salmon
 */
abstract class FxControlPeer
        <FxN extends javafx.scene.control.Control, N extends Control, NB extends ControlPeerBase<N, NB, NM>, NM extends ControlPeerMixin<N, NB, NM>>

        extends FxRegionPeer<FxN, N, NB, NM> {

    FxControlPeer(NB base) {
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
