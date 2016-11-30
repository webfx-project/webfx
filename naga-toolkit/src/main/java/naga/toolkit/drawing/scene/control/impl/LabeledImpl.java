package naga.toolkit.drawing.scene.control.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.drawing.scene.Parent;
import naga.toolkit.drawing.scene.control.Labeled;
import naga.toolkit.spi.nodes.controls.Image;
import naga.toolkit.util.Properties;

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

    {
        // Requesting a new layout pass on text and image properties change
        Properties.runOnPropertiesChange(property -> {
            Parent parent = getParent();
            if (parent != null)
                parent.requestLayout();
        }, textProperty, imageProperty);
    }
}
