package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.scene.control.Control;
import naga.toolkit.drawing.spi.view.ControlView;

/**
 * @author Bruno Salmon
 */
public interface ControlViewMixin
        <N extends Control, NV extends ControlViewBase<N, NV, NM>, NM extends ControlViewMixin<N, NV, NM>>

        extends ControlView<N>,
        NodeViewMixin<N, NV, NM> {
}
