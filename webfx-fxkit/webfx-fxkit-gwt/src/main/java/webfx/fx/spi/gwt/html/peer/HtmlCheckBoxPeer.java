package webfx.fx.spi.gwt.html.peer;

import elemental2.dom.HTMLElement;
import emul.javafx.scene.control.CheckBox;
import webfx.fx.spi.gwt.util.HtmlUtil;
import webfx.fx.spi.peer.base.CheckBoxPeerBase;
import webfx.fx.spi.peer.base.CheckBoxPeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlCheckBoxPeer
        <N extends CheckBox, NB extends CheckBoxPeerBase<N, NB, NM>, NM extends CheckBoxPeerMixin<N, NB, NM>>

        extends HtmlButtonBasePeer<N, NB, NM>
        implements CheckBoxPeerMixin<N, NB, NM> {

    public HtmlCheckBoxPeer() {
        this((NB) new CheckBoxPeerBase(), HtmlUtil.createSpanElement());
    }

    public HtmlCheckBoxPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void updateSelected(Boolean selected) {
        // Nothing to do as everything is managed by the skin
    }
}
