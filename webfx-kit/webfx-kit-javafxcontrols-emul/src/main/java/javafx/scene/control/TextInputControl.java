package javafx.scene.control;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.text.Font;
import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasFontProperty;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasPromptTextProperty;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasTextProperty;

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
    private Property<Boolean> editable = new SimpleObjectProperty<>(this, "editable", true);
    public final boolean isEditable() { return editable.getValue(); }
    public final void setEditable(boolean value) { editable.setValue(value); }
    public final Property<Boolean> editableProperty() { return editable; }

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
}
