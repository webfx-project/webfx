package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.scene.SceneRequester;
import naga.toolkit.fx.scene.layout.Region;

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
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.widthProperty(), changedProperty, mixin::updateWidth)
                || updateProperty(node.heightProperty(), changedProperty, mixin::updateHeight)
                ;
    }
}
