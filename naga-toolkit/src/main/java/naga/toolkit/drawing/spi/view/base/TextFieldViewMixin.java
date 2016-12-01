package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.scene.control.TextField;
import naga.toolkit.drawing.spi.view.TextFieldView;

/**
 * @author Bruno Salmon
 */
public interface TextFieldViewMixin
        extends TextFieldView,
        TextInputControlViewMixin<TextField, TextFieldViewBase, TextFieldViewMixin> {
}
