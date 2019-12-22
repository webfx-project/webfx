package webfx.extras.materialdesign.textfield;

import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 * @author Bruno Salmon
 */
public final class MaterialTextFieldPane extends Pane implements MaterialTextFieldMixin {

    private MaterialTextFieldImpl materialTextField;

    public MaterialTextFieldPane() {
    }

    public MaterialTextFieldPane(Region content) {
        this();
        setContent(content);
    }

    public MaterialTextFieldPane(Region content, ObservableValue inputProperty) {
        this();
        setContent(content, inputProperty);
    }

    @Override
    public MaterialTextField getMaterialTextField() {
        return materialTextField;
    }

    public void setContent(Region content) {
        setContent(content, null);
    }

    public void setContent(Region content, ObservableValue inputProperty) {
        Region oldContent = getContent();
        if (content != oldContent) {
            if (oldContent != null)
                getChildren().remove(oldContent);
            if (content != null)
                getChildren().add(content);
            materialTextField = new MaterialTextFieldImpl(getChildren());
            materialTextField.setContent(content, inputProperty);
        }
    }

    public Region getContent() {
        return materialTextField == null ? null : materialTextField.getContent();
    }

    @Override
    protected void layoutChildren() {
        if (isManaged())
            materialTextField.layoutChildren(0, 0, getWidth(), getHeight(), this::layoutContent);
    }

    private void layoutContent(double x, double y, double w, double h) {
        layoutInArea(getContent(), x, y, w, h, 0, HPos.CENTER, VPos.CENTER);
    }

    @Override
    protected double computeMinHeight(double width) {
        return materialTextField.computeMinHeight(width, getInsets(), this::computeContentMinHeight);
    }

    private double computeContentMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + getContent().minHeight(width) + bottomInset;
    }

    @Override
    protected double computePrefHeight(double width) {
        return materialTextField.computePrefHeight(width, getInsets(), this::computeContentPrefHeight);
    }

    private double computeContentPrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + getContent().prefHeight(width) + bottomInset;
    }

    @Override
    protected double computeMaxHeight(double width) {
        return materialTextField.computeMaxHeight(width, getInsets(), this::computeContentMaxHeight);
    }

    private double computeContentMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + getContent().maxHeight(width) + bottomInset;
    }

    @Override
    protected double computePrefWidth(double height) {
        return Math.max(super.computePrefWidth(height), materialTextField.computePrefWidth(height));
    }
}
