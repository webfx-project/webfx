package naga.providers.toolkit.html.fx.html.viewer;

import elemental2.HTMLElement;
import naga.providers.toolkit.html.util.HtmlPaints;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.control.Labeled;
import naga.toolkit.fx.scene.paint.Paint;
import naga.toolkit.fx.scene.text.Font;
import naga.toolkit.fx.scene.text.TextAlignment;
import naga.toolkit.fx.spi.viewer.base.LabeledViewerBase;
import naga.toolkit.fx.spi.viewer.base.LabeledViewerMixin;

/**
 * @author Bruno Salmon
 */
abstract class HtmlLabeledViewer
        <N extends Labeled, NB extends LabeledViewerBase<N, NB, NM>, NM extends LabeledViewerMixin<N, NB, NM>>

        extends HtmlControlViewer<N, NB, NM>
        implements LabeledViewerMixin<N, NB, NM> {

    HtmlLabeledViewer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void updateText(String text) {
        setElementTextContent(text);
    }

    @Override
    public void updateGraphic(Node graphic) {
    }

    @Override
    public void updateFont(Font font) {
        setFontAttributes(font);
    }

    @Override
    public void updateTextAlignment(TextAlignment textAlignment) {
        setElementStyleAttribute("text-align", toCssTextAlignment(textAlignment));
    }

    @Override
    public void updateTextFill(Paint textFill) {
        getElement().style.color = HtmlPaints.toHtmlCssPaint(textFill);
    }
}
