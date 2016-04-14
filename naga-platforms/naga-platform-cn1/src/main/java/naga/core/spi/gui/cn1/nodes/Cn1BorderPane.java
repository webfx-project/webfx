package naga.core.spi.gui.cn1.nodes;

import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.layouts.BorderLayout;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.gui.GuiNode;
import naga.core.spi.gui.cn1.Cn1Node;
import naga.core.spi.gui.nodes.BorderPane;


/**
 * @author Bruno Salmon
 */
public class Cn1BorderPane extends Cn1Node<Container> implements BorderPane<Container, Component> {

    public Cn1BorderPane() {
        super(new Container(new BorderLayout()));
        topProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                oldValue.unwrapToToolkitNode().remove();
            node.add(BorderLayout.NORTH, newValue.unwrapToToolkitNode());
        });
        centerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                oldValue.unwrapToToolkitNode().remove();
            node.add(BorderLayout.CENTER, newValue.unwrapToToolkitNode());
        });
        bottomProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                oldValue.unwrapToToolkitNode().remove();
            node.add(BorderLayout.SOUTH, newValue.unwrapToToolkitNode());
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
