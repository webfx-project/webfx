package webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import webfx.fxkit.javafxgraphics.mapper.spi.SceneRequester;

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
                updateList2(node.getItems(), list, change, mixin::updateItems)
                ;
    }
}
