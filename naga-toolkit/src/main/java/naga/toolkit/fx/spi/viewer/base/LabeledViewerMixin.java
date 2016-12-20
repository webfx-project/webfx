package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.control.Labeled;

/**
 * @author Bruno Salmon
 */
public interface LabeledViewerMixin
        <N extends Labeled, NV extends LabeledViewerBase<N, NV, NM>, NM extends LabeledViewerMixin<N, NV, NM>>

        extends ControlViewerMixin<N, NV, NM> {

    void updateText(String text);

    void updateGraphic(Node graphic);

}
