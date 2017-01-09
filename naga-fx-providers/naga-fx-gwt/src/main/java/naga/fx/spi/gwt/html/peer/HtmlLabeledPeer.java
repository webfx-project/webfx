package naga.fx.spi.gwt.html.peer;

import elemental2.HTMLElement;
import naga.fx.spi.gwt.util.HtmlPaints;
import naga.fx.scene.Node;
import naga.fx.scene.control.Labeled;
import naga.fx.scene.paint.Paint;
import naga.fx.scene.text.Font;
import naga.fx.scene.text.TextAlignment;
import naga.fx.spi.peer.base.LabeledPeerBase;
import naga.fx.spi.peer.base.LabeledPeerMixin;

/**
 * @author Bruno Salmon
 */
abstract class HtmlLabeledPeer
        <N extends Labeled, NB extends LabeledPeerBase<N, NB, NM>, NM extends LabeledPeerMixin<N, NB, NM>>

        extends HtmlControlPeer<N, NB, NM>
        implements LabeledPeerMixin<N, NB, NM> {

    HtmlLabeledPeer(NB base, HTMLElement element) {
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
