package naga.fx.spi.gwt.html.viewer;

import elemental2.HTMLElement;
import naga.fx.spi.gwt.util.HtmlPaints;
import naga.fx.scene.Node;
import naga.fx.scene.control.Labeled;
import naga.fx.scene.paint.Paint;
import naga.fx.scene.text.Font;
import naga.fx.scene.text.TextAlignment;
import naga.fx.spi.viewer.base.LabeledViewerBase;
import naga.fx.spi.viewer.base.LabeledViewerMixin;

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
