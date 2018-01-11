package naga.framework.ui.controls.material;

import javafx.animation.KeyValue;
import javafx.collections.ObservableList;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import naga.fx.properties.Properties;
import naga.util.Strings;

/**
 * @author Bruno Salmon
 */
public class MaterialLabel {

    private final static Color PROMPT_FOCUSED_COLOR = Color.BLACK;
    private final static Color PROMPT_UNFOCUSED_COLOR = Color.GRAY;
    private final static double PROMPT_FOCUSED_SCALE_FACTOR = 0.85;
    private static double TOP_OFFSET_HEIGHT = 10;

    private final MaterialAnimation materialAnimation;

    private final TextInputControl textInputControl;
    private final Region textBox;
    private final Text labelText = new Text();
    private final Scale labelTextScale = new Scale(1, 1);
    private double floatingLabelTextLayoutY; // floating label = when up
    private double restingLabelTextLayoutY; // resting label = when down
    private boolean inited;

    public MaterialLabel(TextInputControl textInputControl, Region textBox, ObservableList<Node> skinChildren, MaterialAnimation materialAnimation) {
        this.textInputControl = textInputControl;
        this.textBox = textBox;
        this.materialAnimation = materialAnimation;

        labelText.setManaged(false);
        labelText.setMouseTransparent(true);
        labelText.getTransforms().add(labelTextScale);
        labelText.fontProperty().bind(textInputControl.fontProperty());
        labelText.textProperty().bind(textInputControl.promptTextProperty());
        materialAnimation.runNowAndOnPropertiesChange(p -> {
            if (inited)
                positionOrAnimatePromptText();
        }, textInputControl.focusedProperty(), textInputControl.editableProperty(), textInputControl.textProperty());

        skinChildren.add(labelText);
    }

    private void positionOrAnimatePromptText() {
        boolean empty = Strings.isEmpty(textInputControl.getText());
        boolean editable = textInputControl.isEditable();
        boolean focused = textInputControl.isFocused();
        labelText.setTextOrigin(focused || !empty || !editable ? VPos.TOP : VPos.CENTER);
        if (empty && editable && inited)
            materialAnimation.play(
                    new KeyValue(labelTextScale.xProperty(), focused ? PROMPT_FOCUSED_SCALE_FACTOR : 1, Properties.EASE_OUT_INTERPOLATOR),
                    new KeyValue(labelTextScale.yProperty(), focused ? PROMPT_FOCUSED_SCALE_FACTOR : 1, Properties.EASE_OUT_INTERPOLATOR),
                    new KeyValue(labelText.layoutYProperty(), focused ? floatingLabelTextLayoutY : restingLabelTextLayoutY, Properties.EASE_OUT_INTERPOLATOR),
                    new KeyValue(labelText.fillProperty(), focused ? PROMPT_FOCUSED_COLOR : PROMPT_UNFOCUSED_COLOR, Properties.EASE_OUT_INTERPOLATOR)
            );
        else {
            labelTextScale.setX(empty ? 1 : PROMPT_FOCUSED_SCALE_FACTOR);
            labelTextScale.setY(empty ? 1 : PROMPT_FOCUSED_SCALE_FACTOR);
            labelText.setLayoutY(empty ? restingLabelTextLayoutY : floatingLabelTextLayoutY);
            labelText.setFill(empty ? PROMPT_UNFOCUSED_COLOR : PROMPT_FOCUSED_COLOR);
        }
    }

    public double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset, ComputeHeightFunction textBoxComputeMinHeightFunction) {
        return TOP_OFFSET_HEIGHT + textBoxComputeMinHeightFunction.computeHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    public double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset, ComputeHeightFunction textBoxComputePrefHeightFunction) {
        return TOP_OFFSET_HEIGHT + textBoxComputePrefHeightFunction.computeHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    public double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset, ComputeHeightFunction textBoxComputeMaxHeightFunction) {
        return TOP_OFFSET_HEIGHT + textBoxComputeMaxHeightFunction.computeHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    public double computeBaselineOffset(double topInset, double rightInset, double bottomInset, double leftInset, ComputeBaselineOffsetFunction textBoxComputeBaselineOffsetFunction) {
        return TOP_OFFSET_HEIGHT + textBoxComputeBaselineOffsetFunction.computeBaselineOffset(topInset, rightInset, bottomInset, leftInset);
    }

    public void layoutChildren(double x, double y, double w, double h, LayoutChildrenFunction textBoxLayoutChildrenFunction) {
        double yTextBox = y + TOP_OFFSET_HEIGHT - 1;
        double hTextBox = h - TOP_OFFSET_HEIGHT - 1;
        textBoxLayoutChildrenFunction.layoutChildren(x, yTextBox, w, hTextBox);
        if (!inited) {
            labelText.setLayoutX(textBox.getLayoutX() + 1);
            floatingLabelTextLayoutY = 0;
            restingLabelTextLayoutY = yTextBox + hTextBox / 2;
            positionOrAnimatePromptText();
            inited = true;
            //getSkinnable().setBackground(BackgroundUtil.newBackground(Color.YELLOW));
        }
    }


}
