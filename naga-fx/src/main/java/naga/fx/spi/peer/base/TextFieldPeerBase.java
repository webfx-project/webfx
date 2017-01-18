package naga.fx.spi.peer.base;

import javafx.scene.control.TextField;

/**
 * @author Bruno Salmon
 */
public class TextFieldPeerBase
        <N extends TextField, NB extends TextFieldPeerBase<N, NB, NM>, NM extends TextFieldPeerMixin<N, NB, NM>>

        extends TextInputControlPeerBase<N, NB, NM> {

}
