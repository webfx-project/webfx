package webfx.fxkits.core.mapper.spi.impl.peer;

import javafx.scene.control.CheckBox;

/**
 * @author Bruno Salmon
 */
public interface CheckBoxPeerMixin
        <N extends CheckBox, NB extends CheckBoxPeerBase<N, NB, NM>, NM extends CheckBoxPeerMixin<N, NB, NM>>

        extends ButtonBasePeerMixin<N, NB, NM> {

    void updateSelected(Boolean selected);
}
