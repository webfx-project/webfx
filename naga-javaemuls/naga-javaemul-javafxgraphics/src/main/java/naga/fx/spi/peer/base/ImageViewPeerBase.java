package naga.fx.spi.peer.base;

import emul.javafx.beans.value.ObservableValue;
import naga.fx.scene.SceneRequester;
import emul.javafx.scene.image.Image;
import emul.javafx.scene.image.ImageView;

/**
 * @author Bruno Salmon
 */
public class ImageViewPeerBase
        <N extends ImageView, NB extends ImageViewPeerBase<N, NB, NM>, NM extends ImageViewPeerMixin<N, NB, NM>>

        extends NodePeerBase<N, NB, NM> {

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
