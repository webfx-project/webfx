package naga.providers.toolkit.swing.nodes.layouts;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.swing.fx.SwingDrawingNode;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.spi.DrawingNode;
import naga.toolkit.spi.nodes.layouts.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * @author Bruno Salmon
 */
public class SwingWindow implements Window {

    private final JFrame frame;

    public SwingWindow() {
        this(new JFrame());
    }

    SwingWindow(JFrame frame) {
        this.frame = frame;
        nodeProperty.addListener((observable, oldValue, node) -> setWindowContent(node));
        titleProperty().addListener((observable, oldValue, newValue) -> frame.setTitle(newValue));
    }

    private void setWindowContent(Node node) {
        DrawingNode drawingNode = naga.toolkit.spi.Toolkit.get().createDrawingNode();
        drawingNode.setRootNode(node);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                drawingNode.setWidth((double) frame.getContentPane().getWidth());
                drawingNode.setHeight((double) frame.getContentPane().getHeight());
            }
        });
        setWindowContent(((SwingDrawingNode) drawingNode).unwrapToNativeNode());
    }

    private void setWindowContent(Component rootComponent) {
        Container contentPane = frame.getContentPane();
        boolean firstShown = contentPane.getComponents().length == 0;
        contentPane.removeAll();
        contentPane.add(rootComponent);
        if (firstShown) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setSize((int) (screenSize.getWidth() * 0.8), (int) (screenSize.getHeight() * 0.9));
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    private final Property<Node> nodeProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Node> nodeProperty() {
        return nodeProperty;
    }

    private final Property<String> titleProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> titleProperty() {
        return titleProperty;
    }
}
