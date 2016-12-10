package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.text.Text;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.viewer.TextViewer;

/**
 * @author Bruno Salmon
 */
public class TextViewerBase
        extends ShapeViewerBase<Text, TextViewerBase, TextViewerMixin>
        implements TextViewer {

    @Override
    public void bind(Text t, DrawingRequester drawingRequester) {
        super.bind(t, drawingRequester);
        requestUpdateOnPropertiesChange(drawingRequester,
                t.textProperty(),
                t.textOriginProperty(),
                t.wrappingWidthProperty(),
                t.textAlignmentProperty(),
                t.fontProperty());
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        Text ts = node;
        return super.updateProperty(changedProperty)
                || updateProperty(ts.textProperty(), changedProperty, mixin::updateText)
                || updateProperty(ts.xProperty(), changedProperty, mixin::updateX)
                || updateProperty(ts.yProperty(), changedProperty, mixin::updateY)
                || updateProperty(ts.wrappingWidthProperty(), changedProperty, mixin::updateWrappingWidth)
                || updateProperty(ts.textAlignmentProperty(), changedProperty, mixin::updateTextAlignment)
                || updateProperty(ts.textOriginProperty(), changedProperty, mixin::updateTextOrigin)
                || updateProperty(ts.fontProperty(), changedProperty, mixin::updateFont);
    }
}
