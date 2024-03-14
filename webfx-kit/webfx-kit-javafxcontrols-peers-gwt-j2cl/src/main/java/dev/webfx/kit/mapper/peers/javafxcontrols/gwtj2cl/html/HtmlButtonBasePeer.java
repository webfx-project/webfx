package dev.webfx.kit.mapper.peers.javafxcontrols.gwtj2cl.html;

import elemental2.dom.HTMLElement;
import javafx.scene.control.ButtonBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.ButtonBasePeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.ButtonBasePeerMixin;

/**
 * @author Bruno Salmon
 */
abstract class HtmlButtonBasePeer
        <N extends ButtonBase, NB extends ButtonBasePeerBase<N, NB, NM>, NM extends ButtonBasePeerMixin<N, NB, NM>>

        extends HtmlLabeledPeer<N, NB, NM>
        implements ButtonBasePeerMixin<N, NB, NM> {

    HtmlButtonBasePeer(NB base, HTMLElement element) {
        super(base, element);
        // Line below was a good idea but commented as this prevents the validation decoration to be displayed
        //element.style.overflow = "hidden"; // to clip the content to the button area
    }
}
