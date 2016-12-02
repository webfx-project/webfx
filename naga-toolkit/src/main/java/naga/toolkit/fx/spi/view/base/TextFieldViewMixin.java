package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.scene.control.TextField;
import naga.toolkit.fx.spi.view.TextFieldView;

/**
 * @author Bruno Salmon
 */
public interface TextFieldViewMixin
        extends TextFieldView,
        TextInputControlViewMixin<TextField, TextFieldViewBase, TextFieldViewMixin> {
}
