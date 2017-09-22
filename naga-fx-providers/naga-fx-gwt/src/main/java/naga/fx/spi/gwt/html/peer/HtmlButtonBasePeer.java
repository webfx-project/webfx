package naga.fx.spi.gwt.html.peer;

import elemental2.dom.Event;
import elemental2.dom.HTMLElement;
import emul.javafx.event.ActionEvent;
import emul.javafx.scene.control.ButtonBase;
import naga.fx.spi.peer.base.ButtonBasePeerBase;
import naga.fx.spi.peer.base.ButtonBasePeerMixin;

/**
 * @author Bruno Salmon
 */
abstract class HtmlButtonBasePeer
        <N extends ButtonBase, NB extends ButtonBasePeerBase<N, NB, NM>, NM extends ButtonBasePeerMixin<N, NB, NM>>

        extends HtmlLabeledPeer<N, NB, NM>
        implements ButtonBasePeerMixin<N, NB, NM> {

    HtmlButtonBasePeer(NB base, HTMLElement element) {
        super(base, element);
        element.style.overflow = "hidden"; // hiding button content overflow
    }

    @Override
    protected emul.javafx.event.Event toFxClickEvent(Event e) {
        N node = getNode();
        return new ActionEvent(node, node);
    }
}
