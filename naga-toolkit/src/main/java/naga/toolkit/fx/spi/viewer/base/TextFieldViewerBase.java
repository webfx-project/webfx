package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.control.TextField;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.viewer.TextFieldViewer;

/**
 * @author Bruno Salmon
 */
public class TextFieldViewerBase
        extends TextInputControlViewerBase<TextField, TextFieldViewerBase, TextFieldViewerMixin>
        implements TextFieldViewer {

    @Override
    public void bind(TextField button, DrawingRequester drawingRequester) {
        super.bind(button, drawingRequester);
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        return super.updateProperty(changedProperty);
    }
}
