package naga.toolkit.drawing.shapes.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.shapes.DrawableParent;
import naga.toolkit.util.ObservableLists;

/**
 * @author Bruno Salmon
 */
public class DrawableParentImpl extends DrawableImpl implements DrawableParent {

    public DrawableParentImpl() {
    }

    public DrawableParentImpl(Drawable... drawables) {
        ObservableLists.setAllNonNulls(getDrawableChildren(), drawables);
    }

    private final ObservableList<Drawable> drawableChildren = FXCollections.observableArrayList();
    @Override
    public ObservableList<Drawable> getDrawableChildren() {
        return drawableChildren;
    }

}
