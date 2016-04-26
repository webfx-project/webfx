package naga.core.spi.gui.swing.nodes;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.gui.GuiNode;
import naga.core.spi.gui.nodes.Window;

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
        nodeProperty.addListener((observable, oldValue, newValue) -> setWindowContent(newValue.unwrapToToolkitNode()));
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
