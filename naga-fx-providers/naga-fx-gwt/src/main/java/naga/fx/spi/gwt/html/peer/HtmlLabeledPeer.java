package naga.fx.spi.gwt.html.peer;

import elemental2.dom.HTMLElement;
import naga.fx.spi.gwt.util.HtmlPaints;
import emul.javafx.scene.Node;
import emul.javafx.scene.control.Labeled;
import emul.javafx.scene.paint.Paint;
import emul.javafx.scene.text.Font;
import emul.javafx.scene.text.TextAlignment;
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
