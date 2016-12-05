package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.scene.control.Control;
import naga.toolkit.fx.spi.view.ControlView;

/**
 * @author Bruno Salmon
 */
public interface ControlViewMixin
        <N extends Control, NV extends ControlViewBase<N, NV, NM>, NM extends ControlViewMixin<N, NV, NM>>

        extends ControlView<N>,
        RegionViewMixin<N, NV, NM> {
}
