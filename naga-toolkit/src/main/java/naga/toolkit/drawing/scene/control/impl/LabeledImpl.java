package naga.toolkit.drawing.scene.control.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.drawing.scene.control.Labeled;
import naga.toolkit.spi.nodes.controls.Image;

/**
 * @author Bruno Salmon
 */
public class LabeledImpl extends ControlImpl implements Labeled {

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }

    private final Property<Image> imageProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Image> imageProperty() {
        return imageProperty;
    }

}
