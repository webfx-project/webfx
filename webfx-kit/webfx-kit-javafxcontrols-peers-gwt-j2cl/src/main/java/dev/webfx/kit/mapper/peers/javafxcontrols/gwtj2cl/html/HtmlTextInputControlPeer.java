package dev.webfx.kit.mapper.peers.javafxcontrols.gwtj2cl.html;

import dev.webfx.kit.mapper.peers.javafxcontrols.base.TextInputControlPeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.TextInputControlPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import dev.webfx.kit.util.properties.FXProperties;
import dev.webfx.platform.uischeduler.UiScheduler;
import dev.webfx.platform.util.Booleans;
import dev.webfx.platform.util.Objects;
import dev.webfx.platform.util.Strings;
import elemental2.dom.*;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextInputControl;
import javafx.scene.text.Font;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Bruno Salmon
 */
public abstract class HtmlTextInputControlPeer
        <N extends TextInputControl, NB extends TextInputControlPeerBase<N, NB, NM>, NM extends TextInputControlPeerMixin<N, NB, NM>>

        extends HtmlControlPeer<N, NB, NM>
        implements TextInputControlPeerMixin<N, NB, NM>, TextInputControl.SelectableTextInputControlPeer {

    public HtmlTextInputControlPeer(NB base, HTMLElement textInputElement, String containerTag) {
        super(base, textInputElement);
        /* Commented as I can't remember the purpose, and it complexifies the DOM. TODO: remove if no side effect
        prepareDomForAdditionalSkinChildren(containerTag);
        // Restoring pointer events (were disabled by prepareDomForAdditionalSkinChildren()) in case the graphic is clickable (ex: radio button)
        HtmlUtil.setStyleAttribute(getChildrenContainer(), "pointer-events", "auto");
        */
        textInputElement.oninput = e -> {
            getNode().setText(getValue());
            return null;
        };
        textInputElement.onkeypress = e -> {
            if ("Enter".equals(((KeyboardEvent) e).key))
                getNode().fireEvent(new ActionEvent());
            return null;
        };
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
        FXProperties.runNowAndOnPropertyChange(this::onSceneChanged, node.sceneProperty());
    }

    private static final Set<Scene> FOCUS_LISTENER_SCENES = new HashSet<>();

    private void onSceneChanged(Scene scene) {
        // When adding the TextInputControl to the scene graph, we ensure that there is a focus listener associated to
        // that scene that will listen the focus changes, and call updatePromptText() when this happens. This is to
        // reproduce the JavaFX behaviour where the prompt text is not displayed on focused nodes (as opposed to HTML).
        if (scene != null && !FOCUS_LISTENER_SCENES.contains(scene)) {
            FOCUS_LISTENER_SCENES.add(scene);
            scene.focusOwnerProperty().addListener((observable, oldFocusOwner, newFocusOwner) -> {
                updatePromptText(oldFocusOwner);
                updatePromptText(newFocusOwner);
            });
        }
    }

    private static void updatePromptText(Node node) {
        if (node instanceof TextInputControl) {
            NodePeer nodePeer = node.getNodePeer();
            if (nodePeer instanceof HtmlTextInputControlPeer)
                ((HtmlTextInputControlPeer<?, ?, ?>) nodePeer).updatePromptText(((TextInputControl) node).getPromptText());
        }
    }

    @Override
    public void selectRange(int anchor, int caretPosition) {
        Element focusableElement = getHtmlFocusableElement();
        if (focusableElement instanceof HTMLInputElement) {
            HTMLInputElement inputElement = (HTMLInputElement) focusableElement;
            inputElement.setSelectionRange(anchor, caretPosition);
            // Note: There is a bug in Chrome: the previous selection request is ignored if it happens during a focus requested
            // So let's double-check if the selection has been applied
            if (inputElement.selectionStart != anchor || inputElement.selectionEnd != caretPosition)
                // If not, we reapply the selection request later, after the focus request should have been completed
                UiScheduler.scheduleInAnimationFrame(() -> inputElement.setSelectionRange(anchor, caretPosition), 1);
        }
    }

    @Override
    public void updateFont(Font font) {
        setFontAttributes(font);
    }

    @Override
    public void updateText(String text) {
        String safeText = Strings.toSafeString(text);
        if (!Objects.areEquals(getValue(), safeText)) // To avoid caret position reset
            setValue(safeText);
        // The "value" attribute (which normally refers to the initial value only of the text input) has no meaning for
        // WebFX (there is no mapping with JavaFX) but we update it here for HTML CSS styling purpose. We set it either
        // to "" or "not-empty". This is used for example in modality.css with input[type="password"]:not([value=""]) {
        // font-size: 36px; ... } to increase the size of the dots for passwords (otherwise they are tiny), but we don't
        // want this big font size to be applied to the prompt text (i.e. html placeholder) which should be displayed in
        // the normal font size otherwise (when the password input is empty).
        Element focusableElement = getHtmlFocusableElement();
        if (focusableElement != null) {
            String initialValue = Strings.isEmpty(safeText) ? "" : "not-empty";
            setElementAttribute(focusableElement, "value", initialValue);
        }
    }

    @Override
    public void updatePromptText(String promptText) {
        String placeholder = Strings.toSafeString(promptText);
        // In JavaFX, the prompt text is not displayed when the text input has the focus (as opposed to HTML).
        // So we reproduce this behaviour here, unless the application code requests not to do so (using )
        if (isJavaFxFocusOwner() && Booleans.isNotTrue(getNode().getProperties().get("webfx-keepHtmlPlaceholder")))
            placeholder = ""; // Clearing the placeholder on focused nodes.
        Element focusableElement = getHtmlFocusableElement();
        if (focusableElement instanceof HTMLInputElement)
            ((HTMLInputElement) focusableElement).placeholder = placeholder;
        else if (focusableElement instanceof HTMLTextAreaElement)
            setElementAttribute(focusableElement, "placeholder", placeholder);
    }

    @Override
    public void updateEditable(Boolean editable) {
        setElementAttribute(getElement(),"readonly", Booleans.isFalse(editable) ? "true" : null);
    }

    protected String getValue() {
        Element focusableElement = getHtmlFocusableElement();
        if (focusableElement instanceof HTMLInputElement)
            return ((HTMLInputElement) focusableElement).value;
        if (focusableElement instanceof HTMLTextAreaElement)
            return ((HTMLTextAreaElement) focusableElement).value;
        return null;
    }

    protected void setValue(String value) {
        Element focusableElement = getHtmlFocusableElement();
        if (focusableElement instanceof HTMLInputElement)
            ((HTMLInputElement) focusableElement).value = value;
        else if (focusableElement instanceof HTMLTextAreaElement)
            ((HTMLTextAreaElement) focusableElement).value = value;
    }

}
