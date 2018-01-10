package naga.framework.ui.controls.material;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import naga.framework.ui.controls.BackgroundUtil;

/**
 * @author Bruno Salmon
 */
public class MaterialInputLine {

    private final static Color LINE_FOCUSED_COLOR = Color.valueOf("#4059A9");
    private final static Color LINE_UNFOCUSED_COLOR = Color.grayRgb(77);
    private final static Background FOCUSED_BACKGROUND = BackgroundUtil.newBackground(LINE_FOCUSED_COLOR);
    private final static Background UNFOCUSED_BACKGROUND = BackgroundUtil.newBackground(LINE_UNFOCUSED_COLOR);
    private final static Border DASHED_BORDER = new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.DASHED, null, new BorderWidths(1)));

    private final TextInputControl textField;
    private final MaterialAnimation materialAnimation;

    private final StackPane line = new StackPane();
    private final StackPane focusedLine = new StackPane();
    private final Scale focusedLineScale = new Scale();

    public MaterialInputLine(TextInputControl textInputControl, ObservableList<Node> skinChildren) {
        this(textInputControl, skinChildren, new MaterialAnimation());
    }

    public MaterialInputLine(TextInputControl textInputControl, ObservableList<Node> skinChildren, MaterialAnimation materialAnimation) {
        this.textField = textInputControl;
        this.materialAnimation = materialAnimation;

        initLineProperties(line);
        initLineProperties(focusedLine);
        materialAnimation.runNowAndOnPropertiesChange(p -> {
            updateLineProperties(line);
            updateLineProperties(focusedLine);
            animateFocusedLine();
        }, textField.focusedProperty(), textField.editableProperty());

        skinChildren.addAll(line, focusedLine);
    }

    private void initLineProperties(StackPane line) {
        boolean isFocusedLine = line == focusedLine;
        line.setPrefHeight(isFocusedLine ? 2d : 1d);
        line.setLayoutY(isFocusedLine ? 0d : 1d); // translate = prefHeight + init_translation
        line.setManaged(false);
        if (isFocusedLine) {
            line.setBackground(FOCUSED_BACKGROUND);
            line.getTransforms().add(focusedLineScale);
        }
    }

    private void updateLineProperties(StackPane line) {
        boolean editable = textField.isEditable();
        if (line == this.line) {
            line.setBorder(editable ? null : DASHED_BORDER);
            line.setBackground(editable ? UNFOCUSED_BACKGROUND : BackgroundUtil.TRANSPARENT_BACKGROUND);
        } else if (!editable || !textField.isFocused())
            focusedLineScale.setX(0d);
    }

    private void animateFocusedLine() {
        if (textField.isFocused() && textField.isEditable() && focusedLineScale.getX() < 1)
            materialAnimation.playEaseOut(focusedLineScale.xProperty(), 1d);
    }

    public void layoutChildren(double x, double y, double w, double h) {
        line.resizeRelocate(x, h, w, 1);
        focusedLine.resizeRelocate(x, h - 1, w, 2);
        focusedLineScale.setPivotX(w / 2);
    }

}
