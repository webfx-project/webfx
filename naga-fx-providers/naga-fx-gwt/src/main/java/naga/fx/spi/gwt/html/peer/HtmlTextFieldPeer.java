package naga.fx.spi.gwt.html.peer;

import elemental2.dom.HTMLElement;
import emul.javafx.geometry.Insets;
import emul.javafx.geometry.Pos;
import emul.javafx.scene.control.PasswordField;
import emul.javafx.scene.control.TextField;
import naga.fx.scene.SceneRequester;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.peer.base.TextFieldPeerBase;
import naga.fx.spi.peer.base.TextFieldPeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlTextFieldPeer
        <N extends TextField, NB extends TextFieldPeerBase<N, NB, NM>, NM extends TextFieldPeerMixin<N, NB, NM>>

        extends HtmlTextInputControlPeer<N, NB, NM>
        implements TextFieldPeerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlTextFieldPeer() {
        this((NB) new TextFieldPeerBase(), HtmlUtil.createTextInput());
    }

    public HtmlTextFieldPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        if (node instanceof PasswordField) // Done here as there is no specific HtmlPasswordFieldPeer
            HtmlUtil.setAttribute(getElement(), "type", "password");
        super.bind(node, sceneRequester);
    }

    @Override
    public void updatePadding(Insets padding) {
    }

    @Override
    public void updateAlignment(Pos alignment) {
        setElementStyleAttribute("text-align", toCssTextAlignment(alignment));
    }
}
