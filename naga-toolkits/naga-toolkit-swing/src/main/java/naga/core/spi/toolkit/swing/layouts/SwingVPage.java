package naga.core.spi.toolkit.swing.layouts;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.toolkit.node.GuiNode;
import naga.core.spi.toolkit.layouts.VPage;
import naga.core.spi.toolkit.swing.node.SwingNode;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingVPage extends SwingNode<JPanel> implements VPage<JPanel, Component> {

    public SwingVPage() {
        super(new JPanel(new BorderLayout()));
        headerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                node.remove(oldValue.unwrapToNativeNode());
            node.add(newValue.unwrapToNativeNode(), BorderLayout.NORTH);
            node.updateUI();
        });
        centerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                node.remove(oldValue.unwrapToNativeNode());
            node.add(newValue.unwrapToNativeNode(), BorderLayout.CENTER);
            node.updateUI();
        });
        footerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                node.remove(oldValue.unwrapToNativeNode());
            node.add(newValue.unwrapToNativeNode(), BorderLayout.SOUTH);
            node.updateUI();
        });
    }


    private final Property<GuiNode<Component>> headerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<Component>> headerProperty() {
        return headerProperty;
    }

    private final Property<GuiNode<Component>> centerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<Component>> centerProperty() {
        return centerProperty;
    }

    private final Property<GuiNode<Component>> footerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<Component>> footerProperty() {
        return footerProperty;
    }
}
