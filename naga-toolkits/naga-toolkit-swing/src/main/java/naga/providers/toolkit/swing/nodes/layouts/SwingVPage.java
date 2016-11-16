package naga.providers.toolkit.swing.nodes.layouts;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.layouts.VPage;
import naga.providers.toolkit.swing.nodes.SwingNode;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingVPage extends SwingNode<JPanel> implements VPage {

    public SwingVPage() {
        super(new JPanel(new BorderLayout()));
        headerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                node.remove((Component) oldValue.unwrapToNativeNode());
            node.add(newValue.unwrapToNativeNode(), BorderLayout.NORTH);
            node.updateUI();
        });
        centerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                node.remove((Component) oldValue.unwrapToNativeNode());
            node.add(newValue.unwrapToNativeNode(), BorderLayout.CENTER);
            node.updateUI();
        });
        footerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                node.remove((Component) oldValue.unwrapToNativeNode());
            node.add(newValue.unwrapToNativeNode(), BorderLayout.SOUTH);
            node.updateUI();
        });
    }


    private final Property<GuiNode> headerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode> headerProperty() {
        return headerProperty;
    }

    private final Property<GuiNode> centerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode> centerProperty() {
        return centerProperty;
    }

    private final Property<GuiNode> footerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode> footerProperty() {
        return footerProperty;
    }
}
