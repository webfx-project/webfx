package naga.fx.spi.peer.base;

import javafx.beans.value.ObservableValue;
import naga.fx.scene.SceneRequester;
import naga.fx.scene.layout.Region;

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
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.widthProperty(), changedProperty, mixin::updateWidth)
                || updateProperty(node.heightProperty(), changedProperty, mixin::updateHeight)
                || updateProperty(node.backgroundProperty(), changedProperty, mixin::updateBackground)
                || updateProperty(node.borderProperty(), changedProperty, mixin::updateBorder)
                ;
    }
}
