package naga.toolkit.fx.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.layout.Background;

/**
 * @author Bruno Salmon
 */
public interface HasBackgroundProperty {

    Property<Background> backgroundProperty();
    default void setBackground(Background background) { backgroundProperty().setValue(background); }
    default Background getBackground() { return backgroundProperty().getValue(); }

}
