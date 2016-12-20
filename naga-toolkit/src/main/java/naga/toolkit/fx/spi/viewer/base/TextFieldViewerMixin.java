package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.TextField;

/**
 * @author Bruno Salmon
 */
public interface TextFieldViewerMixin
        <N extends TextField, NV extends TextFieldViewerBase<N, NV, NM>, NM extends TextFieldViewerMixin<N, NV, NM>>

        extends TextInputControlViewerMixin<N, NV, NM> {
}
