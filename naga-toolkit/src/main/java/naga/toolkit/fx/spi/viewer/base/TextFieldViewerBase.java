package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.Slider;
import naga.toolkit.fx.scene.control.TextField;

/**
 * @author Bruno Salmon
 */
public class TextFieldViewerBase
        <N extends TextField, NV extends TextFieldViewerBase<N, NV, NM>, NM extends TextFieldViewerMixin<N, NV, NM>>

        extends TextInputControlViewerBase<N, NV, NM> {

}
