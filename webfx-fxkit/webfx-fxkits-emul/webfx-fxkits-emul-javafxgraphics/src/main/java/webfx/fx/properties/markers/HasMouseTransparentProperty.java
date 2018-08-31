package webfx.fx.properties.markers;


import emul.javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasMouseTransparentProperty {

    Property<Boolean> mouseTransparentProperty();
    default void setMouseTransparent(Boolean mouseTransparent) { mouseTransparentProperty().setValue(mouseTransparent); }
    default Boolean isMouseTransparent() { return mouseTransparentProperty().getValue(); }

}
