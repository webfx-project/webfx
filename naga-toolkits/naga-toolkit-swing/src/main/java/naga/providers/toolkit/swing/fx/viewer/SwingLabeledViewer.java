package naga.providers.toolkit.swing.fx.viewer;

import naga.providers.toolkit.swing.util.SwingFonts;
import naga.toolkit.fx.scene.control.Labeled;
import naga.toolkit.fx.scene.text.Font;
import naga.toolkit.fx.scene.text.TextAlignment;
import naga.toolkit.fx.spi.viewer.base.LabeledViewerBase;
import naga.toolkit.fx.spi.viewer.base.LabeledViewerMixin;

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
}
