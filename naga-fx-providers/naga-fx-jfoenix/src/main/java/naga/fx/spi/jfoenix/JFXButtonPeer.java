package naga.fx.spi.jfoenix;

import com.jfoenix.controls.JFXButton;
import naga.fx.spi.javafx.peer.FxButtonPeer;
import naga.fx.scene.control.Button;
import naga.fx.spi.peer.base.ButtonPeerBase;
import naga.fx.spi.peer.base.ButtonPeerMixin;

/**
 * @author Bruno Salmon
 */
public class JFXButtonPeer
        <FxN extends JFXButton, N extends Button, NB extends ButtonPeerBase<N, NB, NM>, NM extends ButtonPeerMixin<N, NB, NM>>

        extends FxButtonPeer<FxN, N, NB, NM> {

    @Override
    protected FxN createFxNode() {
        return (FxN) new JFXButton();
    }
}
