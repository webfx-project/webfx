package webfx.kit.mapper.peers.javafxcontrols.gwt.html;

import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLInputElement;
import javafx.scene.control.skin.TextFieldSkin;
import javafx.scene.control.skin.ToolkitTextBox;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import webfx.kit.mapper.peers.javafxcontrols.base.TextFieldPeerBase;
import webfx.kit.mapper.peers.javafxcontrols.base.TextFieldPeerMixin;
import webfx.kit.mapper.peers.javafxgraphics.gwt.html.layoutmeasurable.HtmlLayoutMeasurable;
import webfx.kit.mapper.peers.javafxgraphics.gwt.shared.HtmlSvgNodePeer;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import webfx.kit.mapper.peers.javafxgraphics.SceneRequester;

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
            n = ((ToolkitTextBox) n).getEmbeddingTextField();
        if (n instanceof PasswordField) // Done here as there is no specific HtmlPasswordFieldPeer
            HtmlUtil.setAttribute(getElement(), "type", "password");
    }

    @Override
    protected Element getFocusElement() {
        Node n = getNode();
        Skin skin = getNode().getSkin();
        if (skin instanceof TextFieldSkin)
            n = ((TextFieldSkin) skin).getChildren().get(0); // Should be the ToolkitTextBox
        return ((HtmlSvgNodePeer) n.getNodePeer()).getElement();
    }

    @Override
    public void updateAlignment(Pos alignment) {
        // For any reason, the correct alignment is overridden by this second call, so we prevent it TODO: investigate why
        if (getNode() instanceof ToolkitTextBox)
            return;
        setElementStyleAttribute("text-align", toCssTextAlignment(alignment));
    }

    public static HtmlTextFieldPeer createHtmlTextBoxPeer() {
        HTMLInputElement textInput = HtmlUtil.createTextInput();
        HtmlUtil.setStyleAttribute(textInput,"text-align","inherit"); // so that it inherits text-align attribute set on element
        return new HtmlTextFieldPeer(textInput);
    }
}
