package naga.core.spi.toolkit.pivot.nodes;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.toolkit.GuiNode;
import naga.core.spi.toolkit.nodes.BorderPane;
import naga.core.spi.toolkit.pivot.PivotNode;
import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Orientation;


/**
 * @author Bruno Salmon
 */
public class PivotBorderPane extends PivotNode<BoxPane> implements BorderPane<BoxPane, Component> {

    public PivotBorderPane() {
        super(new BoxPane());
        node.setOrientation(Orientation.VERTICAL);
        node.getStyles().put("fill", true);
        topProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                node.remove(oldValue.unwrapToNativeNode());
            node.add(newValue.unwrapToNativeNode());
        });
        centerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                node.remove(oldValue.unwrapToNativeNode());
            node.add(newValue.unwrapToNativeNode());
        });
        bottomProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                node.remove(oldValue.unwrapToNativeNode());
            node.add(newValue.unwrapToNativeNode());
        });
    }


    private final Property<GuiNode<Component>> topProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<Component>> topProperty() {
        return topProperty;
    }

    private final Property<GuiNode<Component>> centerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<Component>> centerProperty() {
        return centerProperty;
    }

    private final Property<GuiNode<Component>> bottomProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<Component>> bottomProperty() {
        return bottomProperty;
    }
}
