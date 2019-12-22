package webfx.extras.materialdesign.textfield;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.TextFieldSkin;
import javafx.scene.layout.Region;
import javafx.scene.text.HitInfo;
import webfx.extras.materialdesign.util.layout.LayoutUtil;
import webfx.platform.shared.util.collection.Collections;


/**
 * @author Bruno Salmon
 */
public final class MaterialTextFieldSkin extends TextFieldSkin implements MaterialTextFieldMixin {

    private final MaterialTextFieldImpl materialTextField;

    public MaterialTextFieldSkin(TextField textField) {
        super(LayoutUtil.removePadding(textField));
        ObservableList<Node> children = getChildren();
        Region textBox = (Region) Collections.first(children);
        materialTextField = new MaterialTextFieldImpl(children);
        materialTextField.setContent(textBox, textField);
    }

    @Override
    public MaterialTextField getMaterialTextField() {
        return materialTextField;
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        materialTextField.layoutChildren(x, y, w, h, this::superLayoutChildren);
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (superCall)
            return super.computeMinHeight(width, topInset, rightInset, bottomInset, leftInset);
        return materialTextField.computeMinHeight(width, topInset, rightInset, bottomInset, leftInset, this::superComputeMinHeight);
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (superCall)
            return super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
        return materialTextField.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset, this::superComputePrefHeight);
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (superCall)
            return super.computeMaxHeight(width, topInset, rightInset, bottomInset, leftInset);
        return materialTextField.computeMaxHeight(width, topInset, rightInset, bottomInset, leftInset, this::superComputeMaxHeight);
    }

    @Override
    public double computeBaselineOffset(double topInset, double rightInset, double bottomInset, double leftInset) {
        return materialTextField.computeBaselineOffset(topInset, rightInset, bottomInset, leftInset, super::computeBaselineOffset);
    }

    /* "Super" methods to be used for method reference when super::method is not possible because of protected access */

    private double lastTextFieldX, lastTextFieldY; // used for getIndex() - see last method
    private void superLayoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(lastTextFieldX = x, lastTextFieldY = y, w, h);
    }

    private boolean superCall; // required flag because these super methods might call another height method (ex: pref height) and this should execute the super implementation

    private double superComputeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        superCall = true;
        double h = super.computeMinHeight(width, topInset, rightInset, bottomInset, leftInset);
        superCall = false;
        return h;
    }

    private double superComputePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        superCall = true;
        double h = super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
        superCall = false;
        return h;
    }

    private double superComputeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        superCall = true;
        double h = super.computeMaxHeight(width, topInset, rightInset, bottomInset, leftInset);
        superCall = false;
        return h;
    }

    // Overriding getIndex() to transmit the correct coordinates (ie related to the text field) for the caret position hit detection
    @Override @GwtIncompatible // Marked as GwtIncompatible because not required on html version (and HitInfo not emulated)
    public HitInfo getIndex(double x, double y) {
        return super.getIndex(x - lastTextFieldX, y - lastTextFieldY);
    }
    @interface GwtIncompatible {}
}
