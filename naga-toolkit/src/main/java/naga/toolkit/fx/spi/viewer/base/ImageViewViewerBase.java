package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.scene.image.ImageView;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.viewer.ImageViewViewer;

/**
 * @author Bruno Salmon
 */
public class ImageViewViewerBase
        extends NodeViewerBase<ImageView, ImageViewViewerBase, ImageViewViewerMixin>
        implements ImageViewViewer {

    @Override
    public void bind(ImageView node, DrawingRequester drawingRequester) {
        super.bind(node, drawingRequester);
        requestUpdateOnPropertiesChange(drawingRequester,
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
