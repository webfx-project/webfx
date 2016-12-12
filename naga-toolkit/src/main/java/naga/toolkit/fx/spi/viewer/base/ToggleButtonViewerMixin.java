package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.ToggleButton;
import naga.toolkit.fx.spi.viewer.ToggleButtonViewer;

/**
 * @author Bruno Salmon
 */
public interface ToggleButtonViewerMixin
        <N extends ToggleButton, NV extends ToggleButtonViewerBase<N, NV, NM>, NM extends ToggleButtonViewerMixin<N, NV, NM>>
        extends ButtonBaseViewerMixin<N, NV, NM>,
        ToggleButtonViewer<N> {

    void updateSelected(Boolean selected);
}
