package naga.framework.ui.controls.skins;

import com.sun.javafx.scene.control.skin.TextFieldSkin;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.VPos;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import naga.framework.ui.controls.BackgroundUtil;
import naga.fx.properties.Properties;
import naga.util.Strings;
import naga.util.collection.Collections;

import java.util.ArrayList;
import java.util.Collection;


/**
 * @author Bruno Salmon
 */
public class MaterialTextFieldSkin extends TextFieldSkin {

    private final static Color LINE_FOCUSED_COLOR = Color.valueOf("#4059A9");
    private final static Color LINE_UNFOCUSED_COLOR = Color.grayRgb(77);
    private final static Color PROMPT_FOCUSED_COLOR = Color.BLACK;
    private final static Color PROMPT_UNFOCUSED_COLOR = Color.GRAY;
    private final static double PROMPT_FOCUSED_SCALE_FACTOR = 0.85;
    private final static Background FOCUSED_BACKGROUND = BackgroundUtil.newBackground(LINE_FOCUSED_COLOR);
    private final static Background UNFOCUSED_BACKGROUND = BackgroundUtil.newBackground(LINE_UNFOCUSED_COLOR);
    private final static Border DASHED_BORDER = new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.DASHED, null, new BorderWidths(1)));
    private final static Interpolator EASE_OUT_INTERPOLATOR = Interpolator.SPLINE(0, .75, .25, 1);

    private final Region textBox;
    private final Text promptText = new Text();

    private final StackPane line = new StackPane();
    private final StackPane focusedLine = new StackPane();
    private final Scale focusedLineScale = new Scale();
    private final Scale promptTextScale = new Scale(1, 1);
    private double promptTextUpLayoutY;
    private double promptTextDownLayoutY;
    private Timeline animation;
    private boolean inited;

    public MaterialTextFieldSkin(TextField textField) {
        super(textField);

        textBox = (Region) Collections.first(this.getChildren());
        //textBox.setBorder(BorderUtil.newBorder(Color.RED));

        promptText.setManaged(false);
        promptText.fontProperty().bind(textField.fontProperty());
        promptText.textProperty().bind(textField.promptTextProperty());
        promptText.getTransforms().add(promptTextScale);

        // draw lines
        initLineProperties(line);
        initLineProperties(focusedLine);
        Properties.runNowAndOnPropertiesChange(p -> {
            stopAnimation();
            updateLineProperties(line);
            updateLineProperties(focusedLine);
            if (promptTextDownLayoutY > 0)
                positionOrAnimateFocusedLineAndPromptText();
        }, textField.focusedProperty(), textField.editableProperty(), textField.textProperty());

        getChildren().addAll(line, focusedLine, promptText);
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
        TextField textField = getSkinnable();
        boolean editable = textField.isEditable();
        if (line == this.line) {
            line.setBorder(editable ? null : DASHED_BORDER);
            line.setBackground(editable ? UNFOCUSED_BACKGROUND : BackgroundUtil.TRANSPARENT_BACKGROUND);
        } else if (!editable || !textField.isFocused())
            focusedLineScale.setX(0d);
    }

    private void stopAnimation() {
        if (animation != null)
            animation.stop();
        animation = null;
    }

    private void positionOrAnimateFocusedLineAndPromptText() {
        TextField textField = getSkinnable();
        boolean empty = Strings.isEmpty(textField.getText());
        boolean editable = textField.isEditable();
        boolean focused = textField.isFocused();
        promptText.setTextOrigin(focused || !empty || !editable ? VPos.TOP : VPos.CENTER);
        Collection<KeyValue> animationKeyValues = new ArrayList<>();
        if (focused && editable && focusedLineScale.getX() < 1)
            animationKeyValues.add(new KeyValue(focusedLineScale.xProperty(), 1d, EASE_OUT_INTERPOLATOR));
        if (empty && editable && inited)
            java.util.Collections.addAll(animationKeyValues,
                    new KeyValue(promptTextScale.xProperty(), focused ? PROMPT_FOCUSED_SCALE_FACTOR : 1, EASE_OUT_INTERPOLATOR),
                    new KeyValue(promptTextScale.yProperty(), focused ? PROMPT_FOCUSED_SCALE_FACTOR : 1, EASE_OUT_INTERPOLATOR),
                    new KeyValue(promptText.layoutYProperty(), focused ? promptTextUpLayoutY : promptTextDownLayoutY, EASE_OUT_INTERPOLATOR),
                    new KeyValue(promptText.fillProperty(), focused ? PROMPT_FOCUSED_COLOR : PROMPT_UNFOCUSED_COLOR, EASE_OUT_INTERPOLATOR)
            );
        else {
            promptTextScale.setX(empty ? 1 : PROMPT_FOCUSED_SCALE_FACTOR);
            promptTextScale.setY(empty ? 1 : PROMPT_FOCUSED_SCALE_FACTOR);
            promptText.setLayoutY(empty ? promptTextDownLayoutY : promptTextUpLayoutY);
            promptText.setFill(empty ? PROMPT_UNFOCUSED_COLOR : PROMPT_FOCUSED_COLOR);
        }
        if (!animationKeyValues.isEmpty()) {
            animation = new Timeline(new KeyFrame(Duration.millis(400), Collections.toArray(animationKeyValues, KeyValue[]::new)));
            animation.play();
        }
    }

    private static double TOP_OFFSET_HEIGHT = 10;

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return TOP_OFFSET_HEIGHT + super.computeMinHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return TOP_OFFSET_HEIGHT +  super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return TOP_OFFSET_HEIGHT +  super.computeMaxHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    @Override
    public double computeBaselineOffset(double topInset, double rightInset, double bottomInset, double leftInset) {
        return TOP_OFFSET_HEIGHT +  super.computeBaselineOffset(topInset, rightInset, bottomInset, leftInset);
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        double yTextBox = y + TOP_OFFSET_HEIGHT - 1;
        double hTextBox = h - TOP_OFFSET_HEIGHT - 1;
        super.layoutChildren(x, yTextBox, w, hTextBox);
        line.resizeRelocate(x, h, w, 1);
        focusedLine.resizeRelocate(x, h - 1, w, 2);
        focusedLineScale.setPivotX(w / 2);
        if (!inited) {
            promptText.setLayoutX(textBox.getLayoutX() + 1);
            promptTextUpLayoutY = 0;
            promptTextDownLayoutY = yTextBox + hTextBox / 2;
            positionOrAnimateFocusedLineAndPromptText();
            inited = true;
            //getSkinnable().setBackground(BackgroundUtil.newBackground(Color.YELLOW));
        }
    }
}
