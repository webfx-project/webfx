package naga.fx.spi.swing.fx.viewer;

import naga.fx.spi.swing.util.SwingFonts;
import naga.fx.spi.swing.util.SwingPaints;
import naga.fx.scene.control.Labeled;
import naga.fx.scene.paint.Paint;
import naga.fx.scene.text.Font;
import naga.fx.scene.text.TextAlignment;
import naga.fx.spi.viewer.base.LabeledViewerBase;
import naga.fx.spi.viewer.base.LabeledViewerMixin;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public abstract class SwingLabeledViewer
        <N extends Labeled, NB extends LabeledViewerBase<N, NB, NM>, NM extends LabeledViewerMixin<N, NB, NM>>

        extends SwingControlViewer<N, NB, NM>
        implements LabeledViewerMixin<N, NB, NM>, SwingEmbedComponentViewer<N> {


    SwingLabeledViewer(NB base) {
        super(base);
    }

    @Override
    public void updateFont(Font font) {
        getSwingComponent().setFont(SwingFonts.toSwingFont(font));
    }

    @Override
    public void updateTextAlignment(TextAlignment textAlignment) {
    }

    @Override
    public void updateTextFill(Paint textFill) {
        getSwingComponent().setForeground((Color) SwingPaints.toSwingPaint(textFill));
    }
}
