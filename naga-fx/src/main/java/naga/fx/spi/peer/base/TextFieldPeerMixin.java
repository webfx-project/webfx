package naga.fx.spi.peer.base;

import naga.fx.scene.control.TextField;

/**
 * @author Bruno Salmon
 */
public interface TextFieldPeerMixin
        <N extends TextField, NB extends TextFieldPeerBase<N, NB, NM>, NM extends TextFieldPeerMixin<N, NB, NM>>

        extends TextInputControlPeerMixin<N, NB, NM> {
}
