package dev.webfx.kit.mapper.peers.javafxgraphics.base;

import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.shape.Path;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;

/**
 * @author Bruno Salmon
 */
public class PathPeerBase
        <N extends Path, NB extends PathPeerBase<N, NB, NM>, NM extends PathPeerMixin<N, NB, NM>>

        extends ShapePeerBase<N, NB, NM> {

    @Override
    public void bind(N p, SceneRequester sceneRequester) {
        super.bind(p, sceneRequester);
        requestUpdateOnListChange(sceneRequester, p.getElements());
        requestUpdateOnPropertiesChange(sceneRequester
                , p.fillRuleProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        N c = node;
        return super.updateProperty(changedProperty)
                || updateProperty(c.fillRuleProperty(), changedProperty, p -> mixin.updateFillRule(p))
                ;
    }

    @Override
    public boolean updateList(ObservableList list, ListChangeListener.Change change) {
        return super.updateList(list, change) ||
                updateList2(node.getElements(), list, change, mixin::updateElements)
                ;
    }

}
