package naga.toolkit.drawing.shapes.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.transform.Transform;

/**
 * @author Bruno Salmon
 */
public class DrawableImpl implements Drawable {

    private final ObservableList<Transform> transforms = FXCollections.observableArrayList();
    @Override
    public ObservableList<Transform> getTransforms() {
        return transforms;
    }
}
