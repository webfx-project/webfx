package naga.framework.ui.controls.material.skins;

import com.sun.javafx.scene.control.skin.TextFieldSkin;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import naga.framework.ui.controls.material.MaterialInputLineSkinPart;
import naga.framework.ui.controls.material.MaterialLabelSkinPart;
import naga.framework.ui.controls.material.MaterialUtil;
import naga.util.collection.Collections;


/**
 * @author Bruno Salmon
 */
public class MaterialTextFieldSkin extends TextFieldSkin {

    private final MaterialLabelSkinPart materialLabelSkinPart;
    private final MaterialInputLineSkinPart materialInputLineSkinPart;

    public MaterialTextFieldSkin(TextField textField) {
        super(textField);
        ObservableList<Node> children = getChildren();
        Region textBox = (Region) Collections.first(children);
        materialLabelSkinPart = new MaterialLabelSkinPart(textBox, textField, children);
        materialInputLineSkinPart = new MaterialInputLineSkinPart(textField, children);
        MaterialUtil.shareMaterialAnimation(materialLabelSkinPart, materialInputLineSkinPart);
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        materialLabelSkinPart.layoutChildren(x, y, w, h, this::layoutTextBoxAndLine);
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (superCall)
            return super.computeMinHeight(width, topInset, rightInset, bottomInset, leftInset);
        return MaterialInputLineSkinPart.BOTTOM_PADDING_BELOW_INPUT + materialLabelSkinPart.computeMinHeight(width, topInset, rightInset, bottomInset, leftInset, this::superComputeMinHeight);
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (superCall)
            return super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
        return MaterialInputLineSkinPart.BOTTOM_PADDING_BELOW_INPUT + materialLabelSkinPart.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset, this::superComputePrefHeight);
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (superCall)
            return super.computeMaxHeight(width, topInset, rightInset, bottomInset, leftInset);
        return MaterialInputLineSkinPart.BOTTOM_PADDING_BELOW_INPUT + materialLabelSkinPart.computeMaxHeight(width, topInset, rightInset, bottomInset, leftInset, this::superComputeMaxHeight);
    }

    @Override
    public double computeBaselineOffset(double topInset, double rightInset, double bottomInset, double leftInset) {
        return materialLabelSkinPart.computeBaselineOffset(topInset, rightInset, bottomInset, leftInset, super::computeBaselineOffset);
    }

    /* "Super" methods to be used for method reference when super::method is not possible because of protected access */

    private void layoutTextBoxAndLine(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h - MaterialInputLineSkinPart.BOTTOM_PADDING_BELOW_INPUT);
        materialInputLineSkinPart.layoutChildren(x, y, w, h);
    }

    private boolean superCall;

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
}
