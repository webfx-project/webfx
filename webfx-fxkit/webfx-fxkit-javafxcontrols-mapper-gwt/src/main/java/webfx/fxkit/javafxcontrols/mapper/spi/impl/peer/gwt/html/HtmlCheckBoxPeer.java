package webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.gwt.html;

import elemental2.dom.HTMLElement;
import javafx.scene.control.CheckBox;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.CheckBoxPeerBase;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.CheckBoxPeerMixin;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.util.HtmlUtil;

/**
 * @author Bruno Salmon
 */
public final class HtmlCheckBoxPeer
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
