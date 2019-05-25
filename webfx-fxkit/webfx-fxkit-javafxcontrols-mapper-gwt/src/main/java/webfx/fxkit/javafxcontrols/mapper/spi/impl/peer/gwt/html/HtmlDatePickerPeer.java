package webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.gwt.html;

import elemental2.dom.HTMLElement;
import javafx.scene.control.DatePicker;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.TextFieldPeerBase;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.TextFieldPeerMixin;

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
