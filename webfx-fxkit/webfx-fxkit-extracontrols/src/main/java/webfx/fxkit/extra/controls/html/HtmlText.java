package webfx.fxkit.extra.controls.html;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import webfx.fxkit.extra.controls.registry.ExtraControlsRegistry;
import webfx.fxkit.javafxgraphics.mapper.spi.HasNoChildrenPeers;

/**
 * @author Bruno Salmon
 */
public class HtmlText extends Control implements HasNoChildrenPeers {

    public HtmlText() {
    }

    public HtmlText(String text) {
        setText(text);
    }

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    public Property<String> textProperty() {
        return textProperty;
    }
    public void setText(String text) {
        textProperty.setValue(text);
    }
    public String getText() {
        return textProperty.getValue();
    }

/*
    private NodePeer getPeer() {
        return (NodePeer) getProperties().get("nodePeer");
    }

    @Override
    protected double computePrefWidth(double height) {
        return layoutMeasurable().prefWidth(height);
    }

    @Override
    protected double computePrefHeight(double width) {
        return layoutMeasurable().prefHeight(width);
    }

    private LayoutMeasurable layoutMeasurable() {
        return (LayoutMeasurable) getPeer();
    }
*/

    static {
        ExtraControlsRegistry.registerHtmlText();
    }
}
