package naga.fx.spi.viewer.base;

import naga.fx.scene.Node;
import naga.fx.scene.control.Labeled;
import naga.fx.scene.paint.Paint;
import naga.fx.scene.text.Font;
import naga.fx.scene.text.TextAlignment;

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

    void updateTextFill(Paint textFill);
}
