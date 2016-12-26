package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.scene.SceneRequester;
import naga.toolkit.fx.scene.image.Image;
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
                node.imageProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.imageProperty(), changedProperty, this::updateImage)
                || updateProperty(node.xProperty(), changedProperty, mixin::updateX)
                || updateProperty(node.yProperty(), changedProperty, mixin::updateY)
                || updateProperty(node.fitWidthProperty(), changedProperty, mixin::updateFitWidth)
                || updateProperty(node.fitHeightProperty(), changedProperty, mixin::updateFitHeight)
                ;
    }

    protected void updateImage(Image image) {
        mixin.updateImage(image);
    }
}
