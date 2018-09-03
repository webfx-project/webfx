package webfx.fxkits.core.spi.peer.base;

import javafx.beans.value.ObservableValue;
import webfx.fxkits.core.scene.SceneRequester;
import javafx.scene.layout.Region;

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
                , node.widthProperty()
                , node.heightProperty()
                , node.backgroundProperty()
                , node.borderProperty()
                , node.paddingProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.widthProperty(), changedProperty, p -> mixin.updateWidth(p.doubleValue()))
                || updateProperty(node.heightProperty(), changedProperty, p -> mixin.updateHeight(p.doubleValue()))
                || updateProperty(node.backgroundProperty(), changedProperty, mixin::updateBackground)
                || updateProperty(node.borderProperty(), changedProperty, mixin::updateBorder)
                || updateProperty(node.paddingProperty(), changedProperty, mixin::updatePadding)
                ;
    }
}
