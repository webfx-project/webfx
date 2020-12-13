package dev.webfx.kit.mapper.peers.javafxcontrols.gwt.html;

import elemental2.dom.HTMLElement;
import javafx.scene.control.DatePicker;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.TextFieldPeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.TextFieldPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlDatePickerPeer
        <N extends DatePicker, NB extends TextFieldPeerBase<N, NB, NM>, NM extends TextFieldPeerMixin<N, NB, NM>>

        extends HtmlTextFieldPeer<N, NB, NM> {

    public HtmlDatePickerPeer() {
        super();
    }

    public HtmlDatePickerPeer(NB base, HTMLElement element) {
        super(base, element);
    }

}
