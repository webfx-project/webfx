package naga.providers.toolkit.swing.nodes.layouts;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.layouts.Window;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingWindow implements Window<Component> {

    private final JFrame frame;

    public SwingWindow() {
        this(new JFrame());
    }

    public SwingWindow(JFrame frame) {
        this.frame = frame;
        nodeProperty.addListener((observable, oldValue, newValue) -> setWindowContent(newValue.unwrapToNativeNode()));
        titleProperty().addListener((observable, oldValue, newValue) -> frame.setTitle(newValue));
    }

    private void setWindowContent(Component rootComponent) {
        Container contentPane = frame.getContentPane();
        boolean firstShown = contentPane.getComponents().length == 0;
        contentPane.removeAll();
        contentPane.add(rootComponent);
        if (firstShown) {
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    private final Property<GuiNode<Component>> nodeProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<Component>> nodeProperty() {
        return nodeProperty;
    }

    private final Property<String> titleProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> titleProperty() {
        return titleProperty;
    }
}
