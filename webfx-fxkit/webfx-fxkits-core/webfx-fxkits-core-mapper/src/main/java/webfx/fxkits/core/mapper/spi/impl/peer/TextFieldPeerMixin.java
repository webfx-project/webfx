package webfx.fxkits.core.mapper.spi.impl.peer;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;

/**
 * @author Bruno Salmon
 */
public interface TextFieldPeerMixin
        <N extends TextField, NB extends TextFieldPeerBase<N, NB, NM>, NM extends TextFieldPeerMixin<N, NB, NM>>

        extends TextInputControlPeerMixin<N, NB, NM> {

    void updateAlignment(Pos alignment);
}
