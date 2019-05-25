package webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.gwt.html;

import elemental2.dom.CSSProperties;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLInputElement;
import javafx.scene.control.RadioButton;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.RadioButtonPeerBase;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.RadioButtonPeerMixin;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.html.NoWrapWhiteSpacePeer;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.html.layoutmeasurable.HtmlLayoutMeasurableNoGrow;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.util.HtmlUtil;
import webfx.platform.shared.util.Booleans;

/**
 * @author Bruno Salmon
 */
public final class HtmlRadioButtonPeer
        <N extends RadioButton, NB extends RadioButtonPeerBase<N, NB, NM>, NM extends RadioButtonPeerMixin<N, NB, NM>>

        extends HtmlButtonBasePeer<N, NB, NM>
        implements RadioButtonPeerMixin<N, NB, NM>, HtmlLayoutMeasurableNoGrow, NoWrapWhiteSpacePeer {

    private final HTMLInputElement radioButtonElement;

    public HtmlRadioButtonPeer() {
        this((NB) new RadioButtonPeerBase(), HtmlUtil.createLabelElement());
    }

    public HtmlRadioButtonPeer(NB base, HTMLElement element) {
        super(base, element);
        prepareDomForAdditionalSkinChildren();
        radioButtonElement = HtmlUtil.createRadioButton();
        CSSStyleDeclaration style = element.style;
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
    public void updateDisabled(Boolean disabled) {
        setElementAttribute(radioButtonElement, "disabled", Booleans.isTrue(disabled) ? "disabled" : null);
    }

    @Override
    protected void updateHtmlContent() {
        super.updateHtmlContent();
        HtmlUtil.appendFirstChild(getElement(), radioButtonElement);
    }
}
