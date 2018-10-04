package webfx.fxkit.mapper.spi.impl.peer.markers;


import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasMouseTransparentProperty {

    Property<Boolean> mouseTransparentProperty();
    default void setMouseTransparent(Boolean mouseTransparent) { mouseTransparentProperty().setValue(mouseTransparent); }
    default Boolean isMouseTransparent() { return mouseTransparentProperty().getValue(); }

}
