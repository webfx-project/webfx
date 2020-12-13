package dev.webfx.kit.mapper.peers.javafxgraphics.base;

import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Region;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;

/**
 * @author Bruno Salmon
 */
public class RegionPeerBase
        <N extends Region, NB extends RegionPeerBase<N, NB, NM>, NM extends RegionPeerMixin<N, NB, NM>>

        extends NodePeerBase<N, NB, NM> {

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , node.backgroundProperty()
                , node.borderProperty()
                , node.paddingProperty()
                , node.widthProperty()
                , node.heightProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.backgroundProperty(), changedProperty, mixin::updateBackground)
                || updateProperty(node.borderProperty(), changedProperty, mixin::updateBorder)
                || updateProperty(node.paddingProperty(), changedProperty, mixin::updatePadding)
                || updateProperty(node.widthProperty(), changedProperty, mixin::updateWidth)
                || updateProperty(node.heightProperty(), changedProperty, mixin::updateHeight)
                ;
    }
}
