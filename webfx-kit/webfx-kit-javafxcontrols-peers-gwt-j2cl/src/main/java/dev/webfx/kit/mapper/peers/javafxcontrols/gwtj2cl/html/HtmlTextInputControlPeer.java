package dev.webfx.kit.mapper.peers.javafxcontrols.gwtj2cl.html;

import dev.webfx.kit.mapper.peers.javafxcontrols.base.TextInputControlPeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.TextInputControlPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.shared.HtmlSvgNodePeer;
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
import javafx.scene.control.skin.ToolkitTextBox;
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

    static {
        // We listen to the global selection changes on the document and eventually forward them to the matching peer
        DomGlobal.document.addEventListener("selectionchange", e -> {
            if (e.target instanceof HTMLInputElement inputElement && HtmlSvgNodePeer.getPeerFromElementOrParents(inputElement, true) instanceof HtmlTextInputControlPeer<?, ?, ?> textInputPeer) {
                textInputPeer.onSelectionChanged(inputElement, textInputPeer);
            }
        });
    }

    private boolean scheduledReapplying;
    private boolean syncingAnchorPropertyFromPeer;
    private boolean syncingCaretPositionPropertyFromPeer;

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

    private void onSelectionChanged(HTMLInputElement inputElement, HtmlTextInputControlPeer<?, ?, ?> originalPeer) {
        // Forwarding to the embedding TextField in the case of a ToolkitTextBox
        if (getNode() instanceof ToolkitTextBox ttb && ttb.getEmbeddingTextField().getNodePeer() instanceof HtmlTextInputControlPeer<?, ?, ?> textInputPeer) {
            textInputPeer.onSelectionChanged(inputElement, originalPeer);
            return;
        }
        // If we scheduled a selection reapplying, we ignore the later changes until the selection has actually been reapplied.
        // Ex: the first time the user switches from the password field to the clear text field in WebFX Stack UILoginView,
        // we reapply the selection of the password to the text field, but this may fail because of the focus change.
        // This case is detected in selectRange() which schedules a selection reapplying, while the browser sets meanwhile
        // the selection to the end of the text field. But we ignore that (not synced to JavaFX) until the original
        // selection has actually been reapplied to the text field.
        if (scheduledReapplying)
            return;
        boolean selectionBackward = "backward".equalsIgnoreCase(inputElement.selectionDirection);
        int selectionStart = inputElement.selectionStart;
        int selectionEnd = inputElement.selectionEnd;
        if (!String.valueOf(selectionStart).equals("null")) {
            int newAnchorPosition = selectionBackward ? selectionEnd : selectionStart;
            if (newAnchorPosition != getNode().getAnchor()) {
                syncingAnchorPropertyFromPeer = originalPeer.syncingAnchorPropertyFromPeer = true;
                getNode().anchorProperty().set(newAnchorPosition);
            }
            int newCaretPosition = selectionBackward ? selectionStart : selectionEnd;
            if (newCaretPosition != getNode().getCaretPosition()) {
                syncingCaretPositionPropertyFromPeer = originalPeer.syncingCaretPositionPropertyFromPeer = true;
                getNode().caretPositionProperty().set(newCaretPosition);
            }
        }
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
        FXProperties.runNowAndOnPropertyChange(this::onSceneChanged, node.sceneProperty());
    }

    private static final Set<Scene> FOCUS_LISTENER_SCENES = new HashSet<>();

    private void onSceneChanged(Scene scene) {
        // When adding the TextInputControl to the scene graph, we ensure that there is a focus listener associated with
        // that scene that will listen to the focus changes and call updatePromptText() when this happens. This is to
        // reproduce the JavaFX behavior where the prompt text is not displayed on focused nodes (as opposed to HTML).
        if (scene != null && !FOCUS_LISTENER_SCENES.contains(scene)) {
            FOCUS_LISTENER_SCENES.add(scene);
            scene.focusOwnerProperty().addListener((observable, oldFocusOwner, newFocusOwner) -> {
                updatePromptText(oldFocusOwner);
                updatePromptText(newFocusOwner);
            });
        }
    }

    private static void updatePromptText(Node node) {
        if (node instanceof TextInputControl textInputControl && node.getNodePeer() instanceof HtmlTextInputControlPeer<?, ?, ?> htmlTextInputControlPeer) {
            htmlTextInputControlPeer.updatePromptText((textInputControl.getPromptText()));
        }
    }

    @Override
    public void selectRange(int anchor, int caretPosition) {
        Element focusableElement = getHtmlFocusableElement();
        if (focusableElement instanceof HTMLInputElement inputElement) {
            boolean selectionForward = anchor < caretPosition;
            int selectionStart = selectionForward ? anchor : caretPosition;
            int selectionEnd = selectionForward ? caretPosition : anchor;
            if (selectionStart != inputElement.selectionStart || selectionEnd != inputElement.selectionEnd) {
                try { // some types such as 'number' do not support selection range
                    inputElement.setSelectionRange(selectionStart, selectionEnd); // may raise an exception if not supported
                    if (selectionForward && "backward".equalsIgnoreCase(inputElement.selectionDirection))
                        inputElement.selectionDirection = "forward";
                    // Note: the previous selection request may be ignored if it happens during a focus request.
                    // So let's double-check if the selection has been applied
                    if (inputElement.selectionStart != selectionStart || inputElement.selectionEnd != selectionEnd) {
                        scheduledReapplying = true;
                        // If not, we reapply the selection request later, after the focus request should have been completed
                        UiScheduler.scheduleInAnimationFrame(() -> {
                            inputElement.setSelectionRange(selectionStart, selectionEnd);
                            scheduledReapplying = false;
                        }, 1);
                    }
                } catch (Exception ignored) {
                    // happens with types not supporting selection range, there's nothing we can do about it 🤷
                }
            }
        }
    }

    @Override
    public void updateAnchorPosition(Number anchorPosition) {
        if (syncingAnchorPropertyFromPeer)
            syncingAnchorPropertyFromPeer = false;
        else
            selectRange(anchorPosition.intValue(), getNode().getCaretPosition());
    }

    @Override
    public void updateCaretPosition(Number caretPosition) {
        if (syncingCaretPositionPropertyFromPeer)
            syncingCaretPositionPropertyFromPeer = false;
        else
            selectRange(getNode().getAnchor(), caretPosition.intValue());
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
        // We set an attribute called "data-text-empty" to "true" or "false" to tell if the text input is empty or not.
        // This is used, for example, in Modality CSS with input[type="password"][data-text-empty="false"] { ... }
        // to increase the size of the dots for passwords (otherwise they are tiny). But we don't want this big font
        // size to be applied to the prompt text (i.e., the HTML placeholder) which should be displayed in the normal
        // font size otherwise (when the password input is empty).
        Element focusableElement = getHtmlFocusableElement();
        if (focusableElement != null) {
            String initialValue = Strings.isEmpty(safeText) ? "true" : "false";
            setElementAttribute(focusableElement, "data-text-empty", initialValue);
        }
    }

    @Override
    public void updatePromptText(String promptText) {
        String placeholder = Strings.toSafeString(promptText);
        // In JavaFX, the prompt text is not displayed when the text input has the focus (as opposed to HTML).
        // So we reproduce this behavior here, unless the application code requests not to do so (using
        // webfx-keepHtmlPlaceholder property).
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
        setElementAttribute(getElement(), "readonly", Booleans.isFalse(editable) ? "true" : null);
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
