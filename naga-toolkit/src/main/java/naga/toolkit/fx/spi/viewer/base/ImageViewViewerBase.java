package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.scene.Group;
import naga.toolkit.fx.scene.SceneRequester;
import naga.toolkit.fx.scene.image.ImageView;

/**
 * @author Bruno Salmon
 */
public class ImageViewViewerBase
        <N extends ImageView, NB extends ImageViewViewerBase<N, NB, NM>, NM extends ImageViewViewerMixin<N, NB, NM>>

        extends NodeViewerBase<N, NB, NM> {

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
