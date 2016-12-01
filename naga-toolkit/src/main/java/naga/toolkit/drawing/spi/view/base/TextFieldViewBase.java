package naga.toolkit.drawing.spi.view.base;

import javafx.beans.property.Property;
import naga.toolkit.drawing.scene.control.TextField;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.TextFieldView;

/**
 * @author Bruno Salmon
 */
public class TextFieldViewBase
        extends TextInputControlViewBase<TextField, TextFieldViewBase, TextFieldViewMixin>
        implements TextFieldView {

    @Override
    public void bind(TextField button, DrawingRequester drawingRequester) {
        super.bind(button, drawingRequester);
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        return super.updateProperty(changedProperty);
    }
}
