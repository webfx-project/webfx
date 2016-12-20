package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.TextField;

/**
 * @author Bruno Salmon
 */
public class TextFieldViewerBase
        <N extends TextField, NB extends TextFieldViewerBase<N, NB, NM>, NM extends TextFieldViewerMixin<N, NB, NM>>

        extends TextInputControlViewerBase<N, NB, NM> {

}
