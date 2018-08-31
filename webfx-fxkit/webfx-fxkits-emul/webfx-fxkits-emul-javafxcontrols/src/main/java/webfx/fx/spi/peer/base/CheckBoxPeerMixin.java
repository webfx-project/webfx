package webfx.fx.spi.peer.base;

import emul.javafx.scene.control.CheckBox;

/**
 * @author Bruno Salmon
 */
public interface CheckBoxPeerMixin
        <N extends CheckBox, NB extends CheckBoxPeerBase<N, NB, NM>, NM extends CheckBoxPeerMixin<N, NB, NM>>

        extends ButtonBasePeerMixin<N, NB, NM> {

    void updateSelected(Boolean selected);
}
