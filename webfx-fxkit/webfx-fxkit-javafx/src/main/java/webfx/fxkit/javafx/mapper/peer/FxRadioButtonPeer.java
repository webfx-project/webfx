package webfx.fxkit.javafx.mapper.peer;

import javafx.scene.control.RadioButton;
import webfx.fxkits.core.mapper.spi.impl.peer.RadioButtonPeerBase;
import webfx.fxkits.core.mapper.spi.impl.peer.RadioButtonPeerMixin;

/**
 * @author Bruno Salmon
 */
public class FxRadioButtonPeer
        <FxN extends javafx.scene.control.RadioButton, N extends RadioButton, NB extends RadioButtonPeerBase<N, NB, NM>, NM extends RadioButtonPeerMixin<N, NB, NM>>

        extends FxButtonBasePeer<FxN, N, NB, NM>
        implements RadioButtonPeerMixin<N, NB, NM>, FxLayoutMeasurable {

    public FxRadioButtonPeer() {
        super((NB) new RadioButtonPeerBase());
    }

    @Override
    protected FxN createFxNode() {
        javafx.scene.control.RadioButton checkBox = new javafx.scene.control.RadioButton();
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> getNode().setSelected(newValue));
        return (FxN) checkBox;
    }

    @Override
    public void updateSelected(Boolean selected) {
        getFxNode().setSelected(selected);
    }
}
