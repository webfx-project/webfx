package naga.fx.spi.swing.peer;

import emul.javafx.scene.control.Button;
import naga.fx.spi.peer.base.ButtonPeerBase;
import naga.fx.spi.peer.base.ButtonPeerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingButtonPeer
        <N extends Button, NB extends ButtonPeerBase<N, NB, NM>, NM extends ButtonPeerMixin<N, NB, NM>>

        extends SwingButtonBasePeer<N, NB, NM>
        implements ButtonPeerMixin<N, NB, NM> {

    public SwingButtonPeer() {
        this((NB) new ButtonPeerBase(), new JButton());
    }

    public SwingButtonPeer(NB base, AbstractButton swingButtonBase) {
        super(base, swingButtonBase);
    }
}
