package webfx.extras.materialdesign.textfield;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import webfx.extras.materialdesign.util.ComputeBaselineOffsetWithInsetsFunction;
import webfx.extras.materialdesign.util.ComputeHeightWithInsetsFunction;
import webfx.extras.materialdesign.util.LayoutChildrenFunction;
import webfx.extras.materialdesign.util.MaterialAnimation;
import webfx.extras.materialdesign.util.background.BackgroundUtil;
import webfx.extras.materialdesign.util.layout.LayoutUtil;
import webfx.extras.materialdesign.util.scene.SceneUtil;
import webfx.kit.util.properties.Unregisterable;
import webfx.platform.shared.util.Strings;

/**
 * @author Bruno Salmon
 */
public final class MaterialTextFieldImpl implements MaterialTextField {

    private final static double BOTTOM_PADDING_BELOW_FLOATING_LABEL = 8;
    private final static double BOTTOM_PADDING_BELOW_INPUT = 8;
    private final static double PLACEHOLDER_LEFT_PADDING_FOR_NON_INPUT_TEXT = 8;
    private final static double FLOATING_LABEL_SCALE_FACTOR = 0.85;

    private ObservableValue inputProperty;
    @Override
    public ObservableValue inputProperty() {
        return inputProperty;
    }

    private StringProperty placeholderTextProperty;
    @Override
    public StringProperty placeholderTextProperty() {
        return placeholderTextProperty;
    }

    private final StringProperty labelTextProperty = new SimpleStringProperty();
    @Override
    public StringProperty labelTextProperty() {
        return labelTextProperty;
    }

    private final StringProperty helperTextProperty = new SimpleStringProperty();
    @Override
    public StringProperty helperTextProperty() {
        return helperTextProperty;
    }

    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    @Override
    public StringProperty errorMessageProperty() {
        return errorMessageProperty;
    }

    private final BooleanProperty disabledProperty = new SimpleBooleanProperty(false);
    @Override
    public BooleanProperty disabledProperty() {
        return disabledProperty;
    }

    private final BooleanProperty requiredProperty = new SimpleBooleanProperty(false);
    @Override
    public BooleanProperty requiredProperty() {
        return requiredProperty;
    }

    private ReadOnlyBooleanProperty focusedProperty;
    @Override
    public ReadOnlyBooleanProperty focusedProperty() {
        return focusedProperty;
    }

    private final BooleanProperty denseSpacingProperty = new SimpleBooleanProperty(false);
    @Override
    public BooleanProperty denseSpacingProperty() {
        return denseSpacingProperty;
    }

    private final static Color DEFAULT_DISABLED_COLOR = Color.web("#8a8a8a");
    private final Property<Paint> disabledFillProperty = new SimpleObjectProperty<>(DEFAULT_DISABLED_COLOR);
    @Override
    public Property<Paint> disabledFillProperty() {
        return disabledFillProperty;
    }

    private final static Color DEFAULT_INPUT_TEXT_COLOR = Color.web("#1e1e1e");
    private final Property<Paint> inputTextFillProperty = new SimpleObjectProperty<>(DEFAULT_INPUT_TEXT_COLOR);
    @Override
    public Property<Paint> inputTextFillProperty() {
        return inputTextFillProperty;
    }

    private final static Color DEFAULT_INVALID_TEXT_COLOR = Color.web("#fc3259");
    private final Property<Paint> invalidTextFillProperty = new SimpleObjectProperty<>(DEFAULT_INVALID_TEXT_COLOR);
    @Override
    public Property<Paint> invalidTextFillProperty() {
        return invalidTextFillProperty;
    }

    private final static Color DEFAULT_INVALID_LINE_COLOR = Color.web("#ff1744");
    private final Property<Paint> invalidLineFillProperty = new SimpleObjectProperty<>(DEFAULT_INVALID_LINE_COLOR);
    @Override
    public Property<Paint> invalidLineFillProperty() {
        return invalidLineFillProperty;
    }

    private final static Color DEFAULT_FOCUS_LABEL_COLOR = Color.web("#4273fc");
    private final Property<Paint> focusLabelFillProperty = new SimpleObjectProperty<>(DEFAULT_FOCUS_LABEL_COLOR);
    @Override
    public Property<Paint> focusLabelFillProperty() {
        return focusLabelFillProperty;
    }

    private final static Color DEFAULT_FOCUS_LINE_COLOR = Color.web("#2962ff");
    private final Property<Paint> focusLineFillProperty = new SimpleObjectProperty<>(DEFAULT_FOCUS_LINE_COLOR);
    @Override
    public Property<Paint> focusLineFillProperty() {
        return focusLineFillProperty;
    }

    private final static Color DEFAULT_IDLE_TEXT_COLOR = Color.web("#6d6d6d");
    private final Property<Paint> idleTextFillProperty = new SimpleObjectProperty<>(DEFAULT_IDLE_TEXT_COLOR);
    @Override
    public Property<Paint> idleTextFillProperty() {
        return idleTextFillProperty;
    }

    private final ObservableList<Node> skinChildren;
    private Region content;
    private TextInputControl textInputControl;
    private boolean recomputeLabelPositionOnNextLayoutPass;

    private final Text labelText = new Text("W"); // not empty for first layout pass
    private final Scale labelScale = new Scale();
    private double labelTextHeight;
    // floating label = when above the text input (or content)
    private double floatingLabelLayoutX;
    private double floatingLabelLayoutY;
    // resting label = when inside the text input (or content)
    private double restingLabelLayoutX;
    private double restingLabelLayoutY;

    private StackPane line;
    private StackPane focusedLine;
    private Scale focusedLineScale;

    private final Text bottomText = new Text();

    private final MaterialAnimation materialAnimation = new MaterialAnimation();
    private Unregisterable animationTriggers;

    MaterialTextFieldImpl(ObservableList<Node> skinChildren) {
        this.skinChildren = skinChildren;
        labelText.setManaged(false);
        labelText.setMouseTransparent(true);
        labelText.getTransforms().add(labelScale);
    }

    public void setContent(Region content, ObservableValue inputProperty) {
        setContent(content, null, inputProperty);
    }

    public void setContent(Region content, TextInputControl textInputControl) {
        setContent(content, textInputControl, textInputControl.textProperty());
    }

    private void setContent(Region content, TextInputControl textInputControl, ObservableValue inputProperty) {
        this.content = content;
        this.textInputControl = textInputControl;
        recomputeLabelPositionOnNextLayoutPass = true;
        this.inputProperty = inputProperty;
        if (textInputControl != null) {
            placeholderTextProperty = textInputControl.promptTextProperty();
            focusedProperty = textInputControl.focusedProperty();
            labelText.fontProperty().bind(textInputControl.fontProperty());
            skinChildren.addAll(line = newLine(1), focusedLine = newLine(2));
            focusedLine.getTransforms().add(focusedLineScale = new Scale());
        } else {
            placeholderTextProperty = new SimpleStringProperty();
            focusedProperty = new SimpleBooleanProperty(false);
        }
        skinChildren.add(labelText);
        //content.setBackground(BackgroundUtil.newBackground(Color.YELLOW));
        setUpMaterialAnimation();
    }

    private static StackPane newLine(double height) {
        StackPane line = new StackPane();
        line.setManaged(false);
        line.setPrefHeight(height);
        return line;
    }

    public Region getContent() {
        return content;
    }

    private void setUpMaterialAnimation() {
        if (animationTriggers != null)
            animationTriggers.unregister();
        SceneUtil.onSceneReady(content, scene -> {
            animationTriggers = materialAnimation.runNowAndOnPropertiesChange(this::updateMaterialUiIfReady,
                    // Listing all properties used by updateMaterialUi()
                    disabledProperty(),
                    focusedProperty(),
                    inputProperty(),
                    disabledFillProperty(),
                    inputTextFillProperty(),
                    invalidTextFillProperty(),
                    invalidLineFillProperty(),
                    focusLabelFillProperty(),
                    focusLineFillProperty(),
                    idleTextFillProperty(),
                    labelTextProperty(),
                    placeholderTextProperty(),
                    errorMessageProperty(),
                    // also focus owner property used in SceneUtil.isFocusInside() (not necessary for text input control)
                    textInputControl == null ? scene.focusOwnerProperty() : null
            );
            // Releasing focus owner listener on scene change to prevent overload
            if (textInputControl == null)
                content.sceneProperty().addListener((observable, oldValue, newScene) -> {
                    if (newScene != scene) // including null (most probable change when node is removed)
                        animationTriggers.unregister();
                    else {
                        animationTriggers.register();
                        updateMaterialUiIfReady(); // since we stopped listening, some properties may have changed so updating ui
                    }
                }
            );
        });
    }

    private void updateMaterialUiIfReady() {
        if (!recomputeLabelPositionOnNextLayoutPass)
            updateMaterialUi();
    }

    private void updateMaterialUi() {
        boolean focused = isFocused() || textInputControl == null && SceneUtil.isFocusInside(content);
        boolean disabled = isDisabled(); // TODO: isDisabled() || isEditable() (requires new editable property)
        boolean empty = isInputEmpty();
        boolean invalid = Strings.isNotEmpty(getErrorMessage());
        boolean floating = !empty || focused && textInputControl != null;
        double labelScaleFactor = floating ? FLOATING_LABEL_SCALE_FACTOR : 1;
        boolean animate = labelScale.getX() != labelScaleFactor;
        Paint labelFill = focused ? getFocusLabelFill() : getIdleTextFill(),
                placeholderFill = getDisabledFill(),
                inputTextFill = getInputTextFill(),
                lineFill = getIdleTextFill(),
                focusedLineFill = getFocusLineFill(),
                bottomTextFill = labelFill;
        String bottomString = getHelperText();
        if (invalid) {
            labelFill = bottomTextFill = getInvalidTextFill();
            lineFill = focusedLineFill = getInvalidLineFill();
            bottomString = getErrorMessage();
        } else if (disabled)
            labelFill = inputTextFill = lineFill = bottomTextFill = getDisabledFill();
        if (focused && focusedLineScale != null && focusedLineScale.getX() < 1) {
            materialAnimation.addEaseOut(focusedLineScale.xProperty(), 1d);
            animate = true;
        }
        String label = getLabelText();
        String placeholder = getPlaceholderText();
        String labelString = floating ? label : placeholder;
        if (labelString == null)
            labelString = !floating ? label : placeholder;
        if (animate && labelString == placeholder && label != null) {
            labelString = label;
            materialAnimation.setOnFinished(() -> labelText.setText(placeholder));
        }
        labelText.setText(labelString);
        labelText.setTextOrigin(floating ? VPos.TOP : VPos.CENTER);
        labelText.setFill(labelFill);
        if (line != null) {
            line.setBorder(disabled ? new Border(new BorderStroke(lineFill, BorderStrokeStyle.DOTTED, null, new BorderWidths(1))) : null);
            line.setBackground(disabled ? BackgroundUtil.TRANSPARENT_BACKGROUND : BackgroundUtil.newBackground(lineFill));
            focusedLine.setBackground(BackgroundUtil.newBackground(focusedLineFill));
            if (disabled || !focused)
                if (focusedLineScale.getX() >= 1)
                    focusedLineScale.setX(0d);
                else
                    materialAnimation.addEaseOut(focusedLineScale.xProperty(), 0d);
        }
        bottomText.setFill(bottomTextFill);
        bottomText.setText(bottomString);
        materialAnimation
                .addEaseOut(labelScale.xProperty(), labelScaleFactor)
                .addEaseOut(labelScale.yProperty(), labelScaleFactor)
                .addEaseOut(labelText.layoutXProperty(), floating ? floatingLabelLayoutX : restingLabelLayoutX)
                .addEaseOut(labelText.layoutYProperty(), floating ? floatingLabelLayoutY : restingLabelLayoutY)
                .play(animate && !recomputeLabelPositionOnNextLayoutPass);
    }

    // Layout methods to be called by embedding class (MaterialTextFieldPane or MaterialTextFieldSkin)

    public void layoutChildren(double x, double y, double w, double h, LayoutChildrenFunction contentLayoutChildrenFunction) {
        double heightAboveContent = computeHeightAboveContent();
        double yContent = y + heightAboveContent;
        double hContent = h - heightAboveContent;
        if (line != null) {
            double yLine = y + h - 1;
            line.resizeRelocate(x, yLine, w, 1);
            focusedLine.resizeRelocate(x, yLine - 1, w, 2);
            focusedLineScale.setPivotX(w / 2);
        }
        contentLayoutChildrenFunction.layoutChildren(x, yContent, w, hContent);
        if (recomputeLabelPositionOnNextLayoutPass) {
            floatingLabelLayoutX = LayoutUtil.snapPosition(content.getLayoutX() + 1);
            floatingLabelLayoutY = LayoutUtil.snapPosition(y);
            restingLabelLayoutX = floatingLabelLayoutX + (textInputControl != null ? 0 : PLACEHOLDER_LEFT_PADDING_FOR_NON_INPUT_TEXT);
            restingLabelLayoutY = LayoutUtil.snapPosition(yContent + hContent / 2);
            updateMaterialUi();
            if (Strings.isNotEmpty(labelText.getText()))
                recomputeLabelPositionOnNextLayoutPass = false;
        }
    }

    private double getFloatingLabelHeight() {
        if (recomputeLabelPositionOnNextLayoutPass)
            labelTextHeight = labelText.prefHeight(-1);
        return LayoutUtil.snapSize(labelTextHeight * FLOATING_LABEL_SCALE_FACTOR);
    }

    private double computeHeightAboveContent() {
        return getFloatingLabelHeight() + BOTTOM_PADDING_BELOW_FLOATING_LABEL;
    }

    private double computeHeightBelowContent() {
        return line == null ? 0 : BOTTOM_PADDING_BELOW_INPUT;
    }

    private double computeHeightWithoutContent() {
        return computeHeightAboveContent() + computeHeightBelowContent();
    }

    private double computeHeightWithoutContent(double topInset, double bottomInset) {
        return topInset + computeHeightWithoutContent() + bottomInset;
    }

    private double computeHeightWithContent(double width, double topInset, double rightInset, double bottomInset, double leftInset, ComputeHeightWithInsetsFunction contentHeightFunction) {
        double v1 = computeHeightWithoutContent(topInset, bottomInset);
        double v2 = contentHeightFunction.computeHeight(width, 0, 0, 0, 0);
        return v1 + v2;
    }

    public double computeMinHeight(double width, Insets insets, ComputeHeightWithInsetsFunction contentMinHeightFunction) {
        return computeMinHeight(width, insets.getTop(), insets.getRight(), insets.getBottom(), insets.getLeft(), contentMinHeightFunction);
    }

    public double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset, ComputeHeightWithInsetsFunction contentMinHeightFunction) {
        return computeHeightWithContent(width, topInset, rightInset, bottomInset, leftInset, contentMinHeightFunction);
    }

    public double computePrefHeight(double width, Insets insets, ComputeHeightWithInsetsFunction contentPrefHeightFunction) {
        return computePrefHeight(width, insets.getTop(), insets.getRight(), insets.getBottom(), insets.getLeft(), contentPrefHeightFunction);
    }

    public double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset, ComputeHeightWithInsetsFunction contentPrefHeightFunction) {
        return computeHeightWithContent(width, topInset, rightInset, bottomInset, leftInset, contentPrefHeightFunction);
    }

    public double computeMaxHeight(double width, Insets insets, ComputeHeightWithInsetsFunction contentMaxHeightFunction) {
        return computeMaxHeight(width, insets.getTop(), insets.getRight(), insets.getBottom(), insets.getLeft(), contentMaxHeightFunction);
    }

    public double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset, ComputeHeightWithInsetsFunction contentMaxHeightFunction) {
        return computeHeightWithContent(width, topInset, rightInset, bottomInset, leftInset, contentMaxHeightFunction);
    }

    public double computeBaselineOffset(double topInset, double rightInset, double bottomInset, double leftInset, ComputeBaselineOffsetWithInsetsFunction contentBaselineOffsetFunction) {
        return computeHeightWithoutContent(topInset, bottomInset) + contentBaselineOffsetFunction.computeBaselineOffset(0, 0, 0, 0);
    }

    public double computePrefWidth(double height) {
        Insets insets = content.getInsets();
        double prefWidth = insets.getLeft() + insets.getRight();
        boolean resting = !isFocused() && isInputEmpty();
        if (resting) {
            String labelString = getPlaceholderText();
            if (labelString == null)
                labelString = getLabelText();
            labelText.setText(labelString);
            prefWidth += labelText.prefWidth(height);
            if (textInputControl == null)
                prefWidth += PLACEHOLDER_LEFT_PADDING_FOR_NON_INPUT_TEXT;
        }
        return prefWidth;
    }
}
