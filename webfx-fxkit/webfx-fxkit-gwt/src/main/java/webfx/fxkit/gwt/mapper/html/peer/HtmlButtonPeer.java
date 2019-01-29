package webfx.fxkit.gwt.mapper.html.peer;

import elemental2.dom.HTMLElement;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import webfx.fxkit.gwt.mapper.util.HtmlUtil;
import webfx.fxkit.mapper.spi.impl.peer.ButtonPeerBase;
import webfx.fxkit.mapper.spi.impl.peer.ButtonPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlButtonPeer
        <N extends Button, NB extends ButtonPeerBase<N, NB, NM>, NM extends ButtonPeerMixin<N, NB, NM>>

        extends HtmlButtonBasePeer<N, NB, NM>
        implements ButtonPeerMixin<N, NB, NM> {

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
