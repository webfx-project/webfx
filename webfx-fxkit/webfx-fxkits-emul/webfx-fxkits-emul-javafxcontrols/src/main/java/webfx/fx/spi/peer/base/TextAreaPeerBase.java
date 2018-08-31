package webfx.fx.spi.peer.base;

import emul.javafx.scene.control.TextArea;

/**
 * @author Bruno Salmon
 */
public class TextAreaPeerBase
        <N extends TextArea, NB extends TextAreaPeerBase<N, NB, NM>, NM extends TextAreaPeerMixin<N, NB, NM>>

        extends TextInputControlPeerBase<N, NB, NM> {

}
