package dev.webfx.kit.mapper.peers.javafxcontrols.gwtj2cl.html;

import dev.webfx.kit.util.aria.AriaRole;
import elemental2.dom.CSSProperties;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLInputElement;
import javafx.scene.control.RadioButton;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.RadioButtonPeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.RadioButtonPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.NoWrapWhiteSpacePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.layoutmeasurable.HtmlLayoutMeasurableNoGrow;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.HtmlUtil;
import dev.webfx.platform.util.Booleans;

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
        prepareDomForAdditionalSkinChildren("fx-radiobutton");
        radioButtonElement = HtmlUtil.createRadioButton();
        CSSStyleDeclaration style = element.style;
        style.margin = CSSProperties.MarginUnionType.of("0");
        style.padding = CSSProperties.PaddingUnionType.of("0");
        radioButtonElement.style.verticalAlign = "middle";
        radioButtonElement.style.margin = CSSProperties.MarginUnionType.of("0 5px 0 0");
    }

    @Override
    protected AriaRole getAriaRoleDefault() {
        return AriaRole.RADIO;
    }

    @Override
    protected Boolean isAriaSelectedDefault() {
        return getNode().isSelected();
    }

    @Override
    public void updateSelected(Boolean selected) {
        radioButtonElement.checked = selected;
        updateAriaSelectedAndTabindex(getNodeProperties());
    }

    @Override
    public void updateDisabled(Boolean disabled) {
        setElementAttribute(radioButtonElement, "disabled", Booleans.isTrue(disabled) ? "disabled" : null);
        super.updateDisabled(disabled);
    }

    @Override
    protected void updateHtmlContent() {
        super.updateHtmlContent();
        HtmlUtil.appendFirstChild(getElement(), radioButtonElement);
    }
}
