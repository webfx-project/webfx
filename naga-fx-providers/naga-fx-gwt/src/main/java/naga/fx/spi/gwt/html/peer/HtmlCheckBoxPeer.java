package naga.fx.spi.gwt.html.peer;

import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLInputElement;
import naga.fx.spi.gwt.util.HtmlUtil;
import emul.javafx.scene.control.CheckBox;
import naga.fx.spi.peer.base.CheckBoxPeerBase;
import naga.fx.spi.peer.base.CheckBoxPeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlCheckBoxPeer
        <N extends CheckBox, NB extends CheckBoxPeerBase<N, NB, NM>, NM extends CheckBoxPeerMixin<N, NB, NM>>

        extends HtmlButtonBasePeer<N, NB, NM>
        implements CheckBoxPeerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    private final HTMLInputElement checkBox;

    public HtmlCheckBoxPeer() {
        this((NB) new CheckBoxPeerBase(), HtmlUtil.createLabelElement());
    }

    public HtmlCheckBoxPeer(NB base, HTMLElement element) {
        super(base, element);
        checkBox = HtmlUtil.createCheckBox();
        checkBox.onclick = event -> {
            getNode().setSelected(checkBox.checked);
            return null;
        };
        HtmlUtil.setStyleAttribute(element, "margin-top", "0");
        HtmlUtil.setStyleAttribute(element, "margin-bottom", "0");
        HtmlUtil.setStyleAttribute(checkBox, "vertical-align", "middle");
        HtmlUtil.setStyleAttribute(checkBox, "margin", " 0 5px 0 0");
    }

    @Override
    public void updateSelected(Boolean selected) {
        checkBox.checked = selected;
    }

    @Override
    protected void updateHtmlContent() {
        super.updateHtmlContent();
        HtmlUtil.appendFirstChild(getElement(), checkBox);
    }
}
