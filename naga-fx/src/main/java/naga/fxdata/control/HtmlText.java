package naga.fxdata.control;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import naga.fx.scene.LayoutMeasurable;
import naga.fx.spi.peer.NodePeer;

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
    void setText(String text) {
        textProperty.setValue(text);
    }
    String getText() {
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
