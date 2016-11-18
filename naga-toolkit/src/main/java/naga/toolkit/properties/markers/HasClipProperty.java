package naga.toolkit.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.drawing.shapes.Drawable;

/**
 * @author Bruno Salmon
 */
public interface HasClipProperty {

    Property<Drawable> clipProperty();
    default void setClip(Drawable node) { clipProperty().setValue(node); }
    default Drawable getClip() { return clipProperty().getValue(); }
}
