package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.Control;

/**
 * @author Bruno Salmon
 */
public interface ControlViewerMixin
        <N extends Control, NB extends ControlViewerBase<N, NB, NM>, NM extends ControlViewerMixin<N, NB, NM>>

        extends RegionViewerMixin<N, NB, NM> {
}
