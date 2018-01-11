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
public class MaterialInputLineSkinPart implements HasMaterialAnimation {

    public final static double BOTTOM_PADDING_BELOW_INPUT = 8;

    private final static Color LINE_FOCUSED_COLOR = Color.valueOf("#4059A9");
    private final static Color LINE_UNFOCUSED_COLOR = Color.grayRgb(77);
    private final static Background FOCUSED_BACKGROUND = BackgroundUtil.newBackground(LINE_FOCUSED_COLOR);
    private final static Background UNFOCUSED_BACKGROUND = BackgroundUtil.newBackground(LINE_UNFOCUSED_COLOR);
    private final static Border DASHED_BORDER = new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.DASHED, null, new BorderWidths(1)));

    private final TextInputControl textField;
    private MaterialAnimation materialAnimation;

    private final StackPane line = new StackPane();
    private final StackPane focusedLine = new StackPane();
    private final Scale focusedLineScale = new Scale();

    public MaterialInputLineSkinPart(TextInputControl textInputControl, ObservableList<Node> skinChildren) {
        this.textField = textInputControl;
        initLineProperties(line);
        initLineProperties(focusedLine);
        skinChildren.addAll(line, focusedLine);
    }

    public MaterialAnimation getMaterialAnimation() {
        if (materialAnimation == null)
            setMaterialAnimation(new MaterialAnimation());
        return materialAnimation;
    }

    public void setMaterialAnimation(MaterialAnimation materialAnimation) {
        this.materialAnimation = materialAnimation;
        materialAnimation.runNowAndOnPropertiesChange(p -> {
            updateLineProperties(line);
            updateLineProperties(focusedLine);
            animateFocusedLine();
        }, textField.focusedProperty(), textField.editableProperty());
    }

    private void initLineProperties(StackPane line) {
        boolean isFocusedLine = line == focusedLine;
        line.setPrefHeight(isFocusedLine ? 2d : 1d);
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
            getMaterialAnimation().addEaseOut(focusedLineScale.xProperty(), 1d);
    }

    public void layoutChildren(double x, double y, double w, double h) {
        double yLine = y + h -1;
        line.resizeRelocate(x, yLine, w, 1);
        focusedLine.resizeRelocate(x, yLine - 1, w, 2);
        focusedLineScale.setPivotX(w / 2);
    }

}
