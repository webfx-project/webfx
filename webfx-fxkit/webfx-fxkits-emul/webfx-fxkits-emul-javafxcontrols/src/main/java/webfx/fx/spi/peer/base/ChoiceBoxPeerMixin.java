package webfx.fx.spi.peer.base;

import emul.javafx.collections.ListChangeListener;
import emul.javafx.scene.control.ChoiceBox;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public interface ChoiceBoxPeerMixin
        <T, N extends ChoiceBox<T>, NB extends ChoiceBoxPeerBase<T, N, NB, NM>, NM extends ChoiceBoxPeerMixin<T, N, NB, NM>>

        extends ControlPeerMixin<N, NB, NM> {

    void updateItems(List<T> items, ListChangeListener.Change<T> change);

}
