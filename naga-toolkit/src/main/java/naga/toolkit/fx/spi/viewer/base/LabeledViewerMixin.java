package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.Labeled;
import naga.toolkit.fx.spi.viewer.ControlViewer;

/**
 * @author Bruno Salmon
 */
public interface LabeledViewerMixin
        <N extends Labeled, NV extends LabeledViewerBase<N, NV, NM>, NM extends LabeledViewerMixin<N, NV, NM>>

        extends ControlViewer<N>,
        ControlViewerMixin<N, NV, NM> {

    void updateText(String text);

    //void updateImage(Image image);

}
