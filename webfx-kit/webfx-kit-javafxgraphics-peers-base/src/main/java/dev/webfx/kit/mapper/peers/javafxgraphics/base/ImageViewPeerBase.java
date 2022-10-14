package dev.webfx.kit.mapper.peers.javafxgraphics.base;

import javafx.beans.value.ObservableValue;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
                || updateProperty(node.xProperty(), changedProperty, p -> mixin.updateX(p.doubleValue()))
                || updateProperty(node.yProperty(), changedProperty, p -> mixin.updateY(p.doubleValue()))
                || updateProperty(node.fitWidthProperty(), changedProperty, p -> mixin.updateFitWidth(p.doubleValue()))
                || updateProperty(node.fitHeightProperty(), changedProperty, p -> mixin.updateFitHeight(p.doubleValue()))
                || updateProperty(node.preserveRatioProperty(), changedProperty, p -> mixin.updatePreserveRatio(p))
                ;
    }

    protected void updateImage(Image image) {
        mixin.updateImage(image);
    }
}
