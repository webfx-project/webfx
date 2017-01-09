package naga.fx.spi.swing.peer;

import naga.fx.scene.control.Control;
import naga.fx.spi.peer.base.ControlPeerBase;
import naga.fx.spi.peer.base.ControlPeerMixin;

/**
 * @author Bruno Salmon
 */
public abstract class SwingControlPeer
        <N extends Control, NB extends ControlPeerBase<N, NB, NM>, NM extends ControlPeerMixin<N, NB, NM>>

        extends SwingRegionPeer<N, NB, NM>
        implements ControlPeerMixin<N, NB, NM> {


    SwingControlPeer(NB base) {
        super(base);
    }

}
