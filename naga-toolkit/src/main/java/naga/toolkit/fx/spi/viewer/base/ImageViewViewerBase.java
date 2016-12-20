package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.scene.Group;
import naga.toolkit.fx.scene.SceneRequester;
import naga.toolkit.fx.scene.image.ImageView;

/**
 * @author Bruno Salmon
 */
public class ImageViewViewerBase
        <N extends ImageView, NV extends ImageViewViewerBase<N, NV, NM>, NM extends ImageViewViewerMixin<N, NV, NM>>

        extends NodeViewerBase<N, NV, NM> {

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester,
                node.xProperty(),
                node.yProperty(),
                node.fitWidthProperty(),
                node.fitHeightProperty(),
                node.imageUrlProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.imageUrlProperty(), changedProperty, mixin::updateImageUrl)
                || updateProperty(node.fitWidthProperty(), changedProperty, mixin::updateFitWidth)
                || updateProperty(node.fitHeightProperty(), changedProperty, mixin::updateFitHeight)
                ;
    }
}
