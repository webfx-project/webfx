package naga.fx.spi.peer.base;

import emul.javafx.scene.control.TextField;
import emul.javafx.geometry.Pos;

/**
 * @author Bruno Salmon
 */
public interface TextFieldPeerMixin
        <N extends TextField, NB extends TextFieldPeerBase<N, NB, NM>, NM extends TextFieldPeerMixin<N, NB, NM>>

        extends TextInputControlPeerMixin<N, NB, NM> {

    void updateAlignment(Pos alignment);
}
