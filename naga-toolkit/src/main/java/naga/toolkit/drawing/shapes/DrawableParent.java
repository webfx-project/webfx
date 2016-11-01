package naga.toolkit.drawing.shapes;

import javafx.collections.ObservableList;

/**
 * @author Bruno Salmon
 */
public interface DrawableParent {

    ObservableList<Drawable> getDrawableChildren();
}
