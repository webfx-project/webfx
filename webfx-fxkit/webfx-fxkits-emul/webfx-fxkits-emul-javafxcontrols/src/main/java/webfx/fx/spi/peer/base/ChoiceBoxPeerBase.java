package webfx.fx.spi.peer.base;

import emul.javafx.collections.ListChangeListener;
import emul.javafx.collections.ObservableList;
import emul.javafx.scene.control.ChoiceBox;
import webfx.fx.scene.SceneRequester;

/**
 * @author Bruno Salmon
 */
public class ChoiceBoxPeerBase
        <T, N extends ChoiceBox<T>, NB extends ChoiceBoxPeerBase<T, N, NB, NM>, NM extends ChoiceBoxPeerMixin<T, N, NB, NM>>

        extends ControlPeerBase<N, NB, NM> {

    @Override
    public void bind(N choiceBox, SceneRequester sceneRequester) {
        super.bind(choiceBox, sceneRequester);
        requestUpdateOnListChange(sceneRequester, choiceBox.getItems());
    }

    @Override
    public boolean updateList(ObservableList list, ListChangeListener.Change change) {
        return super.updateList(list, change) ||
                updateList(node.getItems(), list, change, mixin::updateItems)
                ;
    }
}
