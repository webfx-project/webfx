package naga.toolkit.fx.scene.control;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.Parent;
import naga.toolkit.fx.scene.Scene;
import naga.toolkit.properties.markers.HasGraphicProperty;
import naga.toolkit.properties.markers.HasTextProperty;
import naga.toolkit.util.Properties;

/**
 * @author Bruno Salmon
 */
public abstract class Labeled extends Control implements
        HasTextProperty,
        HasGraphicProperty {

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }

    private final Property<Node> graphicProperty = new SimpleObjectProperty<Node>() {
        @Override
        protected void invalidated() {
            setScene(getScene()); // This will propagate the scene reference into the graphic
        }
    };
    @Override
    public Property<Node> graphicProperty() {
        return graphicProperty;
    }

    {
        // Requesting a new layout pass on text and image properties change
        Properties.runOnPropertiesChange(property -> {
            Parent parent = getParent();
            if (parent != null)
                parent.requestLayout();
        }, textProperty, graphicProperty);
    }

    @Override
    public void setScene(Scene scene) {
        super.setScene(scene);
        Node graphic = getGraphic();
        if (graphic != null)
            graphic.setScene(scene);
    }
}
