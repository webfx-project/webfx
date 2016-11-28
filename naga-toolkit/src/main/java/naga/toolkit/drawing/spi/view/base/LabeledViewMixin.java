package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.scene.control.Labeled;
import naga.toolkit.drawing.spi.view.ControlView;

/**
 * @author Bruno Salmon
 */
public interface LabeledViewMixin
        <N extends Labeled, NV extends LabeledViewBase<N, NV, NM>, NM extends LabeledViewMixin<N, NV, NM>>

        extends ControlView<N>,
        ControlViewMixin<N, NV, NM> {

    void updateText(String text);

    //void updateImage(Image image);

}
