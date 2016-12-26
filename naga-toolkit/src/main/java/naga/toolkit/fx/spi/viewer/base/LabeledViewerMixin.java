package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.control.Labeled;
import naga.toolkit.fx.scene.text.Font;
import naga.toolkit.fx.scene.text.TextAlignment;

/**
 * @author Bruno Salmon
 */
public interface LabeledViewerMixin
        <N extends Labeled, NB extends LabeledViewerBase<N, NB, NM>, NM extends LabeledViewerMixin<N, NB, NM>>

        extends ControlViewerMixin<N, NB, NM> {

    void updateText(String text);

    void updateGraphic(Node graphic);

    void updateFont(Font font);

    void updateTextAlignment(TextAlignment textAlignment);
}
