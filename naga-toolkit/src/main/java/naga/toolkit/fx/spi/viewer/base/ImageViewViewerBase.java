package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.property.Property;
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
                node.imageUrlProperty(),
                node.xProperty(),
                node.yProperty()
        );
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.imageUrlProperty(), changedProperty, mixin::updateImageUrl)
                ;
    }
}
