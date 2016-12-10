package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.TextField;
import naga.toolkit.fx.spi.viewer.TextFieldViewer;

/**
 * @author Bruno Salmon
 */
public interface TextFieldViewerMixin
        extends TextFieldViewer,
        TextInputControlViewerMixin<TextField, TextFieldViewerBase, TextFieldViewerMixin> {
}
