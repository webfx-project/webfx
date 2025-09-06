package javafx.scene.control;

import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasFontProperty;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasPromptTextProperty;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasTextProperty;
import javafx.beans.property.*;
import javafx.event.Event;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;

/**
 * @author Bruno Salmon
 */
public abstract class TextInputControl extends Control implements
        HasFontProperty,
        HasTextProperty,
        HasPromptTextProperty {

    private final Property<Font> fontProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Font> fontProperty() {
        return fontProperty;
    }

    private final StringProperty textProperty = new SimpleStringProperty();
    @Override
    public StringProperty textProperty() {
        return textProperty;
    }

    private final StringProperty promptProperty = new SimpleStringProperty();
    @Override
    public StringProperty promptTextProperty() {
        return promptProperty;
    }

    /**
     * Indicates whether this TextInputControl can be edited by the user.
     */
    private final BooleanProperty editable = new SimpleBooleanProperty(this, "editable", true);

    public final boolean isEditable() { return editable.getValue(); }
    public final void setEditable(boolean value) { editable.setValue(value); }
    public final BooleanProperty editableProperty() { return editable; }

    /**
     * Clears the text.
     */
    public void clear() {
        //deselect();
        if (!textProperty.isBound()) {
            setText("");
        }
    }

    public final int getLength() { return getText().length(); }

    /**
     * Selects all text in the text input.
     */
    public void selectAll() {
        selectRange(0, getLength());
    }

    /**
     * Moves the caret to before the first char of the text. This function
     * also has the effect of clearing the selection.
     */
    public void home() {
        // user wants to go to start
        selectRange(0, 0);
    }

    /**
     * Moves the caret to after the last char of the text. This function
     * also has the effect of clearing the selection.
     */
    public void end() {
        // user wants to go to end
        final int textLength = getLength();
        if (textLength > 0) {
            selectRange(textLength, textLength);
        }
    }

    /**
     * Positions the anchor and caretPosition explicitly.
     * @param anchor the anchor
     * @param caretPosition the caretPosition
     */
    public void selectRange(int anchor, int caretPosition) {
        NodePeer peer = getOrCreateAndBindNodePeer();
        if (peer instanceof SelectableTextInputControlPeer)
            ((SelectableTextInputControlPeer) peer).selectRange(anchor, caretPosition);
    }

    public interface SelectableTextInputControlPeer {

        void selectRange(int anchor, int caretPosition);

    }

    /**
     * The <code>anchor</code> of the text selection.
     * The <code>anchor</code> and <code>caretPosition</code> make up the selection
     * range. Selection must always be specified in terms of begin &lt;= end, but
     * <code>anchor</code> may be less than, equal to, or greater than the
     * <code>caretPosition</code>. Depending on how the user selects text,
     * the anchor might represent the lower or upper bound of the selection.
     */
    private IntegerProperty anchor = new SimpleIntegerProperty(this, "anchor", 0);
    public final int getAnchor() { return anchor.get(); }
    public final ReadOnlyIntegerProperty anchorProperty() { return anchor; }

    /**
     * The current position of the caret within the text.
     * The <code>anchor</code> and <code>caretPosition</code> make up the selection
     * range. Selection must always be specified in terms of begin &lt;= end, but
     * <code>anchor</code> may be less than, equal to, or greater than the
     * <code>caretPosition</code>. Depending on how the user selects text,
     * the caretPosition might represent the lower or upper bound of the selection.
     */
    private IntegerProperty caretPosition = new SimpleIntegerProperty(this, "caretPosition", 0);
    public final int getCaretPosition() { return caretPosition.get(); }
    public final ReadOnlyIntegerProperty caretPositionProperty() { return caretPosition; }

    /**
     * Positions the caret to the position indicated by {@code pos}. This
     * function will also clear the selection.
     * @param pos the position
     */
    public void positionCaret(int pos) {
        final int p = pos; // Utils.clamp(0, pos, getLength());
        selectRange(p, p);
    }

    {
        // Although the HTML peer entirely manages the key events, we consume them in JavaFX to not propagate
        // these events to further JavaFX controls.
        addEventHandler(KeyEvent.ANY, e -> {
            if (isFocused()) {
                // Exception is made for accelerators such as Enter or ESC, as they should be passed beyond this control
                switch (e.getCode()) {
                    case ENTER:
                    case ESCAPE:
                        return;
                }
                // Otherwise, we stop the propagation in JavaFX
                e.consume();
                // But we still ask WebFX to propagate them to the peer.
                Event.setPropagateToPeerEvent(e); // See WebFX comments on Event class for more explanation.
            }
        });
    }
}
