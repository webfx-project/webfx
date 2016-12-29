package naga.fx.spi.viewer.base;

import naga.fx.scene.control.Label;

/**
 * @author Bruno Salmon
 */
public class LabelViewerBase
        <N extends Label, NB extends LabelViewerBase<N, NB, NM>, NM extends LabelViewerMixin<N, NB, NM>>

        extends LabeledViewerBase<N, NB, NM> {
}
