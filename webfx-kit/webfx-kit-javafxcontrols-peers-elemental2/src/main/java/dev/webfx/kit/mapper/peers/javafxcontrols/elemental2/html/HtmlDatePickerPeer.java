package dev.webfx.kit.mapper.peers.javafxcontrols.elemental2.html;

import dev.webfx.kit.mapper.peers.javafxcontrols.base.TextFieldPeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.TextFieldPeerMixin;
import javafx.scene.control.DatePicker;

/**
 * @author Bruno Salmon
 */
public final class HtmlDatePickerPeer
        <N extends DatePicker, NB extends TextFieldPeerBase<N, NB, NM>, NM extends TextFieldPeerMixin<N, NB, NM>>

        extends HtmlTextFieldPeer<N, NB, NM> {

    public HtmlDatePickerPeer() {
        super("fx-datepicker");
    }

}
