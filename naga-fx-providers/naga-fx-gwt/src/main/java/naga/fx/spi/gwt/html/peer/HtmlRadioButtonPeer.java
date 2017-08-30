package naga.fx.spi.gwt.html.peer;

import elemental2.dom.CSSProperties;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLInputElement;
import emul.javafx.scene.control.RadioButton;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.peer.base.RadioButtonPeerBase;
import naga.fx.spi.peer.base.RadioButtonPeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlRadioButtonPeer
        <N extends RadioButton, NB extends RadioButtonPeerBase<N, NB, NM>, NM extends RadioButtonPeerMixin<N, NB, NM>>

        extends HtmlButtonBasePeer<N, NB, NM>
        implements RadioButtonPeerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    private final HTMLInputElement radioButtonElement;

    public HtmlRadioButtonPeer() {
        this((NB) new RadioButtonPeerBase(), HtmlUtil.createLabelElement());
    }

    public HtmlRadioButtonPeer(NB base, HTMLElement element) {
        super(base, element);
        radioButtonElement = HtmlUtil.createRadioButton();
        radioButtonElement.onclick = event -> {
            getNode().setSelected(radioButtonElement.checked);
            return null;
        };
        CSSStyleDeclaration style = element.style;
        style.left = "0px";
        style.top = "0px";
        style.margin = CSSProperties.MarginUnionType.of("0");
        style.padding = CSSProperties.PaddingUnionType.of("0");
        radioButtonElement.style.verticalAlign = "middle";
        radioButtonElement.style.margin = CSSProperties.MarginUnionType.of("0 5px 0 0");
    }

    @Override
    public void updateSelected(Boolean selected) {
        radioButtonElement.checked = selected;
    }

    @Override
    protected void updateHtmlContent() {
        super.updateHtmlContent();
        HtmlUtil.appendFirstChild(getElement(), radioButtonElement);
    }
}
