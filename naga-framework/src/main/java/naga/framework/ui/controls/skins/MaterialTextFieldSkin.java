package naga.framework.ui.controls.skins;

import com.sun.javafx.scene.control.skin.TextFieldSkin;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import naga.framework.ui.controls.BackgroundUtil;
import naga.fx.properties.Properties;


/**
 * @author Bruno Salmon
 */
public class MaterialTextFieldSkin extends TextFieldSkin {

    private final StackPane line = new StackPane();
    private final StackPane focusedLine = new StackPane();
    private final Scale focusedLineScale = new Scale();
    private Timeline focusedLineAnimation;

    public MaterialTextFieldSkin(TextField textField) {
        super(textField);

        // draw lines
        initLineProperties(line);
        initLineProperties(focusedLine);
        Properties.runNowAndOnPropertiesChange(p -> {
            updateLineProperties(line);
            updateLineProperties(focusedLine);
        }, textField.focusedProperty(), textField.editableProperty());
        getChildren().addAll(line, focusedLine);
    }

    private final static Color FOCUSED_COLOR = Color.valueOf("#4059A9");
    private final static Color UNFOCUSED_COLOR = Color.grayRgb(77);
    private final static Background FOCUSED_BACKGROUND = BackgroundUtil.newBackground(FOCUSED_COLOR);
    private final static Background UNFOCUSED_BACKGROUND = BackgroundUtil.newBackground(UNFOCUSED_COLOR);
    private final static Border DASHED_BORDER = new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.DASHED, null, new BorderWidths(1)));
    private final static Interpolator EASE_OUT_INTERPOLATOR = Interpolator.SPLINE(0, .75, .25, 1);

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
        if (line == focusedLine) {
            if (focusedLineAnimation != null)
                focusedLineAnimation.stop();
            if (textField.isFocused() && editable) {
                focusedLineAnimation = new Timeline(new KeyFrame(Duration.millis(400), new KeyValue(focusedLineScale.xProperty(), 1d, EASE_OUT_INTERPOLATOR)));
                focusedLineAnimation.play();
            } else
                focusedLineScale.setX(0d);

        } else {
            line.setBorder(editable ? null : DASHED_BORDER);
            line.setBackground(editable ? UNFOCUSED_BACKGROUND : BackgroundUtil.TRANSPARENT_BACKGROUND);
        }
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);
        double height = getSkinnable().getHeight();
        line.resizeRelocate(x, height, w, line.prefHeight(-1));
        focusedLine.resizeRelocate(x, height - 1, w, focusedLine.prefHeight(-1));
        focusedLineScale.setPivotX(w / 2);
    }
}
