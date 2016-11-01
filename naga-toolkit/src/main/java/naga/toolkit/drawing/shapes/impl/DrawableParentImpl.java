package naga.toolkit.drawing.shapes.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.shapes.DrawableParent;

/**
 * @author Bruno Salmon
 */
public class DrawableParentImpl implements DrawableParent {

    private final ObservableList<Drawable> drawableChildren = FXCollections.observableArrayList();
    @Override
    public ObservableList<Drawable> getDrawableChildren() {
        return drawableChildren;
    }

}
