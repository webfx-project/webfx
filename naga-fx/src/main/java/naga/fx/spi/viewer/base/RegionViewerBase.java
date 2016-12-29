package naga.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.fx.scene.SceneRequester;
import naga.fx.scene.layout.Region;

/**
 * @author Bruno Salmon
 */
public class RegionViewerBase
        <N extends Region, NB extends RegionViewerBase<N, NB, NM>, NM extends RegionViewerMixin<N, NB, NM>>

        extends NodeViewerBase<N, NB, NM> {

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
