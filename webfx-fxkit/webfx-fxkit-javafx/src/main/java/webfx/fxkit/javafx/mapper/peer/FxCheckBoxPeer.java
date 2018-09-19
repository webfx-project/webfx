package webfx.fxkit.javafx.mapper.peer;

import javafx.scene.control.CheckBox;
import webfx.fxkits.core.mapper.spi.impl.peer.CheckBoxPeerMixin;
import webfx.fxkits.core.mapper.spi.impl.peer.CheckBoxPeerBase;

/**
 * @author Bruno Salmon
 */
public class FxCheckBoxPeer
        <FxN extends javafx.scene.control.CheckBox, N extends CheckBox, NB extends CheckBoxPeerBase<N, NB, NM>, NM extends CheckBoxPeerMixin<N, NB, NM>>

        extends FxButtonBasePeer<FxN, N, NB, NM>
        implements CheckBoxPeerMixin<N, NB, NM>, FxLayoutMeasurable {

    public FxCheckBoxPeer() {
        super((NB) new CheckBoxPeerBase());
    }

    @Override
    protected FxN createFxNode() {
        javafx.scene.control.CheckBox checkBox = new javafx.scene.control.CheckBox();
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> getNode().setSelected(newValue));
        return (FxN) checkBox;
    }

    @Override
    public void updateSelected(Boolean selected) {
        getFxNode().setSelected(selected);
    }
}
