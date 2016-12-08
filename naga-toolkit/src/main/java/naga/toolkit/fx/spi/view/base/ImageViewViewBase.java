package naga.toolkit.fx.spi.view.base;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.image.ImageView;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.view.ImageViewView;

/**
 * @author Bruno Salmon
 */
public class ImageViewViewBase
        extends NodeViewBase<ImageView, ImageViewViewBase, ImageViewViewMixin>
        implements ImageViewView {

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
