package naga.framework.ui.controls.material.skins;

import com.sun.javafx.scene.control.skin.TextFieldSkin;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import naga.framework.ui.controls.material.MaterialAnimation;
import naga.framework.ui.controls.material.MaterialInputLine;
import naga.framework.ui.controls.material.MaterialLabel;
import naga.util.collection.Collections;


/**
 * @author Bruno Salmon
 */
public class MaterialTextFieldSkin extends TextFieldSkin {

    private final MaterialLabel materialLabel;
    private final MaterialInputLine materialInputLine;

    public MaterialTextFieldSkin(TextField textField) {
        super(textField);
        ObservableList<Node> children = getChildren();
        Region textBox = (Region) Collections.first(children);
        //textBox.setBorder(BorderUtil.newBorder(Color.RED));
        MaterialAnimation materialAnimation = new MaterialAnimation();
        materialInputLine = new MaterialInputLine(textField, children, materialAnimation);
        materialLabel = new MaterialLabel(textField, textBox, children, materialAnimation);
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return materialLabel.computeMinHeight(width, topInset, rightInset, bottomInset, leftInset, this::superComputeMinHeight);
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return materialLabel.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset, this::superComputePrefHeight);
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return materialLabel.computeMaxHeight(width, topInset, rightInset, bottomInset, leftInset, this::superComputeMaxHeight);
    }

    @Override
    public double computeBaselineOffset(double topInset, double rightInset, double bottomInset, double leftInset) {
        return materialLabel.computeBaselineOffset(topInset, rightInset, bottomInset, leftInset, super::computeBaselineOffset);
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        materialLabel.layoutChildren(x, y, w, h, this::superLayoutChildren);
        materialInputLine.layoutChildren(x, y, w, h);
    }

    private double superComputeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return super.computeMinHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    private double superComputePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    private double superComputeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return super.computeMaxHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    private void superLayoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);
    }
}
