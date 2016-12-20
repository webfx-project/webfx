package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.ToggleButton;

/**
 * @author Bruno Salmon
 */
public interface ToggleButtonViewerMixin
        <N extends ToggleButton, NV extends ToggleButtonViewerBase<N, NV, NM>, NM extends ToggleButtonViewerMixin<N, NV, NM>>

        extends ButtonBaseViewerMixin<N, NV, NM> {

    void updateSelected(Boolean selected);
}
