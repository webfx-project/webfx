package emul.com.sun.javafx.scene.control.skin;

import emul.com.sun.javafx.scene.control.behaviour.TextFieldBehavior;
import emul.javafx.scene.control.TextField;

/**
 * Text field skin.
 *
 * (empty as we rely on the target toolkit for now)
 */
public class TextFieldSkin extends TextInputControlSkin<TextField, TextFieldBehavior> {

    /**
     * This group contains the text, caret, and selection rectangle.
     * It is clipped. The textNode, selectionHighlightPath, and
     * caret are each translated individually when horizontal
     * translation is needed to keep the caretPosition visible.
     */
    private final ToolkitTextBox textGroup;

    /**
     * Create a new TextFieldSkin.
     * @param textField not null
     */
    public TextFieldSkin(final TextField textField) {
        this(textField, /*(textField instanceof PasswordField)
                                     ? new PasswordFieldBehavior((PasswordField)textField)
                                     :*/ new TextFieldBehavior(textField));
    }

    public TextFieldSkin(final TextField textField, final TextFieldBehavior behavior) {
        super(textField, behavior);
        textGroup = new ToolkitTextBox(textField);
        getChildren().add(textGroup);
    }

    @Override protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    @Override protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + textGroup.prefHeight(width) + bottomInset;
    }

    @Override protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return getSkinnable().prefHeight(width);
    }

}

