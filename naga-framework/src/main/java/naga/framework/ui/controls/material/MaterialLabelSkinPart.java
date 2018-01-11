package naga.framework.ui.controls.material;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import naga.fx.properties.Unregistrable;
import naga.util.Objects;
import naga.util.Strings;

/**
 * @author Bruno Salmon
 */
public class MaterialLabelSkinPart implements HasMaterialAnimation {

    private final static double BOTTOM_PADDING_BELOW_FLOATING_LABEL = 8;

    private final static Color FLOATING_LABEL_COLOR = Color.BLACK;
    private final static Color RESTING_LABEL_COLOR = Color.GRAY;
    private final static double FLOATING_LABEL_SCALE_FACTOR = 0.85;

    private MaterialAnimation materialAnimation;
    private Unregistrable animationTriggers;

    private final Text labelText = new Text();
    private final Scale labelScale = new Scale();
    private double floatingLabelLayoutY; // floating label = when up
    private double restingLabelLayoutY; // resting label = when down

    private Region content;
    private TextInputControl textInputControl;
    private ObservableValue emptyContentProperty;
    private boolean recomputeLabelPositionOnNextLayoutPass;

    public MaterialLabelSkinPart(ObservableList<Node> skinChildren) {
        labelText.setManaged(false);
        labelText.setMouseTransparent(true);
        labelText.getTransforms().add(labelScale);
        skinChildren.add(labelText);
    }

    public MaterialLabelSkinPart(Region content, ObservableList<Node> skinChildren) {
        this(skinChildren);
        setContent(content);
    }

    public MaterialLabelSkinPart(Region content, TextInputControl textInputControl, ObservableList<Node> skinChildren) {
        this(skinChildren);
        setContent(content, textInputControl);
    }

    public void setContent(Region content) {
        setContent(content, null, null);
    }

    public void setContent(Region content, ObservableValue emptyContentProperty) {
        setContent(content, null, emptyContentProperty);
    }

    public void setContent(Region content, TextInputControl textInputControl) {
        setContent(content, textInputControl, null);
    }

    private void setContent(Region content, TextInputControl textInputControl, ObservableValue emptyContentProperty) {
        this.content = content;
        this.textInputControl = textInputControl;
        recomputeLabelPositionOnNextLayoutPass = true;
        this.emptyContentProperty = emptyContentProperty;
        if (textInputControl != null) {
            labelText.fontProperty().bind(textInputControl.fontProperty());
            labelText.textProperty().bind(textInputControl.promptTextProperty());
        } else
            labelText.setText("Label...");
        //content.setBackground(BackgroundUtil.newBackground(Color.YELLOW));
        setUpTextInputLabelAnimation();
    }

    public Region getContent() {
        return content;
    }

    public MaterialAnimation getMaterialAnimation() {
        if (materialAnimation == null)
            setMaterialAnimation(new MaterialAnimation());
        return materialAnimation;
    }

    public void setMaterialAnimation(MaterialAnimation materialAnimation) {
        this.materialAnimation = materialAnimation;
        setUpTextInputLabelAnimation();
    }

    private void setUpTextInputLabelAnimation() {
        if (materialAnimation != null) {
            if (animationTriggers != null)
                animationTriggers.unregister();
            if (textInputControl != null)
                animationTriggers = materialAnimation.runNowAndOnPropertiesChange(p -> positionOrAnimateLabelIfReady(),
                        textInputControl.focusedProperty(),
                        textInputControl.editableProperty(),
                        textInputControl.textProperty());
            else if (emptyContentProperty != null)
                animationTriggers = materialAnimation.runNowAndOnPropertiesChange(p -> positionOrAnimateLabelIfReady(),
                        emptyContentProperty);
        }
    }

    private void positionOrAnimateLabelIfReady() {
        if (!recomputeLabelPositionOnNextLayoutPass)
            positionOrAnimateLabel();
    }

    private void positionOrAnimateLabel() {
        boolean editable, focused, empty;
        if (textInputControl != null) {
            editable = textInputControl.isEditable();
            focused = textInputControl.isFocused();
            empty = Strings.isEmpty(textInputControl.getText());
        } else {
            editable = true;
            focused = content.isFocused();
            empty = emptyContentProperty != null && Boolean.TRUE.equals(Objects.coalesce(emptyContentProperty.getValue(), Boolean.TRUE));
        }
        boolean floating = focused || !empty || !editable;
        labelText.setTextOrigin(floating ? VPos.TOP : VPos.CENTER);
        double scaleFactor = floating ? FLOATING_LABEL_SCALE_FACTOR : 1;
        boolean animate = labelScale.getX() != scaleFactor && !recomputeLabelPositionOnNextLayoutPass;
        getMaterialAnimation()
                .addEaseOut(labelScale.xProperty(), scaleFactor)
                .addEaseOut(labelScale.yProperty(), scaleFactor)
                .addEaseOut(labelText.layoutYProperty(), floating ? floatingLabelLayoutY : restingLabelLayoutY)
                .addEaseOut(labelText.fillProperty(), floating ? FLOATING_LABEL_COLOR : RESTING_LABEL_COLOR)
                .play(animate);
    }

    private double getFloatingLabelHeight() {
        return snapSize(labelText.prefHeight(-1) * FLOATING_LABEL_SCALE_FACTOR);
    }

    private static double snapSize(double value) {
        return Math.ceil(value);
    }

    private double computeHeightAboveContent() {
        return getFloatingLabelHeight() + BOTTOM_PADDING_BELOW_FLOATING_LABEL;
    }

    private double computeHeightWithoutContent() {
        return computeHeightAboveContent();
    }

    private double computeHeightWithoutContent(double topInset, double bottomInset) {
        return topInset + computeHeightWithoutContent() + bottomInset;
    }

    private double computeHeightWithContent(double width, double topInset, double rightInset, double bottomInset, double leftInset, ComputeHeightWithInsetsFunction contentMinHeightFunction) {
        double v1 = computeHeightWithoutContent(topInset, bottomInset);
        double v2 = contentMinHeightFunction.computeHeight(width, 0, 0, 0, 0);
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

    public void layoutChildren(double x, double y, double w, double h, LayoutChildrenFunction contentLayoutChildrenFunction) {
        double heightAboveContent = computeHeightAboveContent();
        double yContent = y + heightAboveContent;
        double hContent = h - heightAboveContent;
        contentLayoutChildrenFunction.layoutChildren(x, yContent, w, hContent);
        if (recomputeLabelPositionOnNextLayoutPass) {
            labelText.setLayoutX(content.getLayoutX() + (textInputControl != null ? 1 : 8));
            floatingLabelLayoutY = y;
            restingLabelLayoutY = heightAboveContent + hContent / 2;
            positionOrAnimateLabel();
            recomputeLabelPositionOnNextLayoutPass = false;
        }
    }
}
