package javafx.scene.control.skin;

import javafx.scene.control.TextField;

/**
 * Text field skin.
 *
 * (empty as we rely on the target toolkit for now)
 */
public class TextFieldSkin extends TextInputControlSkin<TextField> {

    /**
     * This group contains the text, caret, and selection rectangle.
     * It is clipped. The textNode, selectionHighlightPath, and
     * caret are each translated individually when horizontal
     * translation is needed to keep the caretPosition visible.
     */
    private final ToolkitTextBox textGroup; // WebFX change

    /**
     * Create a new TextFieldSkin.
     * @param textField not null
     */
    public TextFieldSkin(final TextField textField) {
        super(textField);
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

