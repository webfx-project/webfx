package dev.webfx.kit.mapper.peers.javafxcontrols.gwtj2cl.html;

import dev.webfx.kit.mapper.peers.javafxcontrols.base.TextFieldPeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.TextFieldPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.layoutmeasurable.HtmlLayoutMeasurable;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.shared.HtmlSvgNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.HtmlUtil;
import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLInputElement;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.TextFieldSkin;
import javafx.scene.control.skin.ToolkitTextBox;
import javafx.scene.text.Font;

/**
 * @author Bruno Salmon
 */
public class HtmlTextFieldPeer
        <N extends TextField, NB extends TextFieldPeerBase<N, NB, NM>, NM extends TextFieldPeerMixin<N, NB, NM>>

        extends HtmlTextInputControlPeer<N, NB, NM>
        implements TextFieldPeerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlTextFieldPeer() {
        this(HtmlUtil.createSpanElement());
    }

    public HtmlTextFieldPeer(HTMLElement element) {
        this(element, "fx-textfield");
    }

    public HtmlTextFieldPeer(String tagName) {
        this(HtmlUtil.createSpanElement(), tagName);
    }

    public HtmlTextFieldPeer(HTMLElement element, String tagName) {
        this((NB) new TextFieldPeerBase(), element, tagName);
    }

    public HtmlTextFieldPeer(NB base, HTMLElement element, String tagName) {
        super(base, element, tagName);
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
    public void updateFont(Font font) {
        Element focusableElement = getHtmlFocusableElement();
        if (focusableElement != null)
            setFontAttributes(font, focusableElement);
    }

    @Override
    protected Element getHtmlFocusableElement() {
        Node n = getNode();
        Skin skin = getNode().getSkin();
        if (skin instanceof TextFieldSkin) {
            ObservableList<Node> children = ((TextFieldSkin) skin).getChildren();
            if (!children.isEmpty())
                n = children.get(0); // Should be the ToolkitTextBox
        }
        HtmlSvgNodePeer nodePeer = n == null ? null : (HtmlSvgNodePeer) n.getNodePeer();
        return nodePeer == null ? null : nodePeer.getElement();
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
