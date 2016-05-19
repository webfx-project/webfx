package naga.core.spi.toolkit.swing.nodes;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.toolkit.GuiNode;
import naga.core.spi.toolkit.nodes.BorderPane;
import naga.core.spi.toolkit.swing.SwingNode;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingBorderPane extends SwingNode<JPanel> implements BorderPane<JPanel, Component> {

    public SwingBorderPane() {
        super(new JPanel(new BorderLayout()));
        topProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                node.remove(oldValue.unwrapToNativeNode());
            node.add(newValue.unwrapToNativeNode(), BorderLayout.NORTH);
        });
        centerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                node.remove(oldValue.unwrapToNativeNode());
            node.add(newValue.unwrapToNativeNode(), BorderLayout.CENTER);
        });
        bottomProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                node.remove(oldValue.unwrapToNativeNode());
            node.add(newValue.unwrapToNativeNode(), BorderLayout.SOUTH);
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
