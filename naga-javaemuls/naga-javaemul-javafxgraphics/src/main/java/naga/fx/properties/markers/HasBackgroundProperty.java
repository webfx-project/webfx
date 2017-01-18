package naga.fx.properties.markers;

import emul.javafx.beans.property.Property;
import emul.javafx.scene.layout.Background;

/**
 * @author Bruno Salmon
 */
public interface HasBackgroundProperty {

    Property<Background> backgroundProperty();
    default void setBackground(Background background) { backgroundProperty().setValue(background); }
    default Background getBackground() { return backgroundProperty().getValue(); }

}
