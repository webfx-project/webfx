package webfx.fxkits.extra.control;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import webfx.fxkits.core.scene.LayoutMeasurable;
import webfx.fxkits.core.spi.peer.NodePeer;

/**
 * @author Bruno Salmon
 */
public class HtmlText extends Control {

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

    private NodePeer getPeer() {
        return (NodePeer) getProperties().get("nodePeer");
    }

    private LayoutMeasurable getLayoutMeasurable() {
        return (LayoutMeasurable) getPeer();
    }

    @Override
    protected double computePrefWidth(double height) {
        return getLayoutMeasurable().prefWidth(height);
    }

    @Override
    protected double computePrefHeight(double width) {
        return getLayoutMeasurable().prefHeight(width);
    }
}
