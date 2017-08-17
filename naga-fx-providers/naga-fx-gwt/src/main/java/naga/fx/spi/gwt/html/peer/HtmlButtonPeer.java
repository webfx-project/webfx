package naga.fx.spi.gwt.html.peer;

import elemental2.dom.Event;
import elemental2.dom.HTMLElement;
import emul.javafx.scene.Node;
import emul.javafx.scene.control.Button;
import emul.javafx.scene.text.TextAlignment;
import naga.fx.spi.gwt.shared.HtmlSvgNodePeer;
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

    @Override
    protected void onClickElement(Event e) {
        super.onClickElement(e);
        // Ugly patch to propagate the click on the graphic (ex: a radio button) as some browsers don't do it (ex: FireFox and IE)
        Node graphic = getNode().getGraphic();
        if (graphic != null)
            HtmlSvgNodePeer.toElement(graphic, graphic.getScene()).click();
    }

    @Override
    protected void updateHtmlContent() {
        super.updateHtmlContent();
        // Ugly patch to simulate JavaFx button default style which is to have left alignment when a graphic is present, centered text otherwise
        updateTextAlignment(getNode().getGraphic() != null ? TextAlignment.LEFT : TextAlignment.CENTER);
    }
}
