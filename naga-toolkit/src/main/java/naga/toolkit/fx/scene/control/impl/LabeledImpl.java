package naga.toolkit.fx.scene.control.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.Parent;
import naga.toolkit.fx.scene.control.Labeled;
import naga.toolkit.fx.scene.impl.NodeImpl;
import naga.toolkit.fx.scene.impl.SceneImpl;
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

    private final Property<Node> graphicProperty = new SimpleObjectProperty<Node>() {
        @Override
        protected void invalidated() {
            setScene((SceneImpl) getScene()); // This will propagate the drawing into the graphic
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
    public void setScene(SceneImpl scene) {
        super.setScene(scene);
        Node graphic = getGraphic();
        if (graphic != null)
            ((NodeImpl) graphic).setScene(scene);
    }
}
