package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.RadioButton;

/**
 * @author Bruno Salmon
 */
public interface RadioButtonViewerMixin
        <N extends RadioButton, NV extends RadioButtonViewerBase<N, NV, NM>, NM extends RadioButtonViewerMixin<N, NV, NM>>

        extends ToggleButtonViewerMixin<N, NV, NM> {
}
