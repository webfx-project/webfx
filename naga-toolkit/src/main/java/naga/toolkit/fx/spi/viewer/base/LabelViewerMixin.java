package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.Label;

/**
 * @author Bruno Salmon
 */
public interface LabelViewerMixin
        <N extends Label, NB extends LabelViewerBase<N, NB, NM>, NM extends LabelViewerMixin<N, NB, NM>>

        extends LabeledViewerMixin<N, NB, NM> {

}
