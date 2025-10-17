package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Background;

/**
 * @author Bruno Salmon
 */
public interface HasBackgroundProperty {

    ObjectProperty<Background> backgroundProperty();
    default void setBackground(Background background) { backgroundProperty().setValue(background); }
    default Background getBackground() { return backgroundProperty().getValue(); }

}
