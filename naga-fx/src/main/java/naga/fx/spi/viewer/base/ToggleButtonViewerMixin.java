package naga.fx.spi.viewer.base;

import naga.fx.scene.control.ToggleButton;

/**
 * @author Bruno Salmon
 */
public interface ToggleButtonViewerMixin
        <N extends ToggleButton, NB extends ToggleButtonViewerBase<N, NB, NM>, NM extends ToggleButtonViewerMixin<N, NB, NM>>

        extends ButtonBaseViewerMixin<N, NB, NM> {

    void updateSelected(Boolean selected);
}
