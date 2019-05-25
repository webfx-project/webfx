package webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.gwt.html;

import elemental2.dom.HTMLElement;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.ButtonPeerBase;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.ButtonPeerMixin;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.html.NoWrapWhiteSpacePeer;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.util.HtmlUtil;

/**
 * @author Bruno Salmon
 */
public final class HtmlButtonPeer
        <N extends Button, NB extends ButtonPeerBase<N, NB, NM>, NM extends ButtonPeerMixin<N, NB, NM>>

        extends HtmlButtonBasePeer<N, NB, NM>
        implements ButtonPeerMixin<N, NB, NM>, NoWrapWhiteSpacePeer {

    public HtmlButtonPeer() {
        this((NB) new ButtonPeerBase(), HtmlUtil.createButtonElement());
    }

    public HtmlButtonPeer(NB base, HTMLElement element) {
        super(base, element);
        prepareDomForAdditionalSkinChildren();
    }

    @Override
    public void updateGraphic(Node graphic) {
        super.updateGraphic(graphic);
        // Restoring pointer events (were disabled by prepareDomForAdditionalSkinChildren()) in case the graphic is clickable (ex: radio button)
        if (graphic instanceof ButtonBase)
            HtmlUtil.setStyleAttribute(getChildrenContainer(), "pointer-events", "auto");

    }
}
