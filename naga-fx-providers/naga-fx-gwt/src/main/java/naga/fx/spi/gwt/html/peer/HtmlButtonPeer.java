package naga.fx.spi.gwt.html.peer;

import elemental2.dom.Event;
import elemental2.dom.HTMLElement;
import emul.javafx.scene.Node;
import emul.javafx.scene.control.Button;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.peer.base.ButtonPeerBase;
import naga.fx.spi.peer.base.ButtonPeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlButtonPeer
        <N extends Button, NB extends ButtonPeerBase<N, NB, NM>, NM extends ButtonPeerMixin<N, NB, NM>>

        extends HtmlButtonBasePeer<N, NB, NM>
        implements ButtonPeerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlButtonPeer() {
        this((NB) new ButtonPeerBase(), HtmlUtil.createButtonElement());
    }

    public HtmlButtonPeer(NB base, HTMLElement element) {
        super(base, element);
        prepareDomForAdditionalSkinChildren();
    }

    @Override
    public double maxHeight(double width) {
        return prefHeight(width);
    }

    private final HtmlLayoutCache cache = new HtmlLayoutCache();
    @Override
    public HtmlLayoutCache getCache() {
        return cache;
    }

    private boolean graphicClickPropagation;
    @Override
    protected boolean onClickElement(Event e) {
        if (!graphicClickPropagation) {
            super.onClickElement(e);
            // Ugly patch to propagate the click on the graphic (ex: a radio button) as some browsers don't do it (ex: FireFox and IE)
            Node graphic = getNode().getGraphic();
            if (graphic != null) {
                graphicClickPropagation = true;
                toContainerElement(graphic, graphic.getScene()).click();
                graphicClickPropagation = false;
            }
        }
        return true;
    }
}
