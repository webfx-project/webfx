package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.scene.SceneRequester;
import naga.toolkit.fx.scene.layout.Region;

/**
 * @author Bruno Salmon
 */
public class RegionViewerBase
        <N extends Region, NV extends RegionViewerBase<N, NV, NM>, NM extends RegionViewerMixin<N, NV, NM>>
        extends NodeViewerBase<N, NV, NM> {

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
