package naga.toolkit.drawing.shapes;

import javafx.collections.ObservableList;

/**
 * @author Bruno Salmon
 */
public interface DrawableParent extends Drawable {

    ObservableList<Drawable> getDrawableChildren();
}
