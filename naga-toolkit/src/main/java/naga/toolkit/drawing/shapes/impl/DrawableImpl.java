package naga.toolkit.drawing.shapes.impl;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.spi.events.MouseEvent;
import naga.toolkit.spi.events.UiEventHandler;
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

    private final ObjectProperty onMouseClickedProperty = new SimpleObjectProperty();
    @Override
    public ObjectProperty<UiEventHandler<? super MouseEvent>> onMouseClickedProperty() {
        return onMouseClickedProperty;
    }
}
