package naga.framework.ui.controls.material;


import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;

/**
 * @author Bruno Salmon
 */
public class MaterialLabel extends Region {

    private MaterialLabelSkinPart materialLabelSkinPart;

    public MaterialLabel() {
    }

    public MaterialLabel(Region content) {
        this();
        setContent(content);
    }

    public MaterialLabel(Region content, ObservableValue emptyContentProperty) {
        this();
        setContent(content, emptyContentProperty);
    }

    public void setContent(Region content) {
        setContent(content, null);
    }

    public void setContent(Region content, ObservableValue emptyContentProperty) {
        Region oldContent = getContent();
        if (content != oldContent) {
            if (oldContent != null)
                getChildren().remove(oldContent);
            if (content != null)
                getChildren().add(content);
            materialLabelSkinPart = new MaterialLabelSkinPart(getChildren());
            materialLabelSkinPart.setContent(content, emptyContentProperty);
        }
    }

    public Region getContent() {
        return materialLabelSkinPart == null ? null : materialLabelSkinPart.getContent();
    }

    @Override
    protected void layoutChildren() {
        materialLabelSkinPart.layoutChildren(0, 0, getWidth(), getHeight(), this::layoutContent);
    }

    private void layoutContent(double x, double y, double w, double h) {
        layoutInArea(getContent(), x, y, w, h, 0, HPos.CENTER, VPos.CENTER);
    }

    @Override
    protected double computeMinHeight(double width) {
        return materialLabelSkinPart.computeMinHeight(width, getInsets(), this::computeContentMinHeight);
    }

    private double computeContentMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + getContent().minHeight(width) + bottomInset;
    }

    @Override
    protected double computePrefHeight(double width) {
        return materialLabelSkinPart.computePrefHeight(width, getInsets(), this::computeContentPrefHeight);
    }

    private double computeContentPrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + getContent().prefHeight(width) + bottomInset;
    }

    @Override
    protected double computeMaxHeight(double width) {
        return materialLabelSkinPart.computeMaxHeight(width, getInsets(), this::computeContentMaxHeight);
    }

    private double computeContentMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + getContent().maxHeight(width) + bottomInset;
    }
}
