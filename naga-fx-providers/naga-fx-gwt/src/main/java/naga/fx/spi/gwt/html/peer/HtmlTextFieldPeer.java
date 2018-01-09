package naga.fx.spi.gwt.html.peer;

import elemental2.dom.HTMLElement;
import emul.com.sun.javafx.scene.control.skin.ToolkitTextBox;
import emul.javafx.geometry.Pos;
import emul.javafx.scene.Node;
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
        this(HtmlUtil.createSpanElement()/*HtmlUtil.createTextInput()*/);
    }

    public HtmlTextFieldPeer(HTMLElement element) {
        this((NB) new TextFieldPeerBase(), element);
    }

    public HtmlTextFieldPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
        Node n = node;
        if (n instanceof ToolkitTextBox)
            n = ((ToolkitTextBox) node).getEmbeddingTextField();
        if (n instanceof PasswordField) // Done here as there is no specific HtmlPasswordFieldPeer
            HtmlUtil.setAttribute(getElement(), "type", "password");
    }

    @Override
    public void updateAlignment(Pos alignment) {
        setElementStyleAttribute("text-align", toCssTextAlignment(alignment));
    }

    public static HtmlTextFieldPeer createHtmlTextBoxPeer() {
        return new HtmlTextFieldPeer(HtmlUtil.createTextInput());
    }
}
