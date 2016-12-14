package naga.providers.toolkit.javafx.fx;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import naga.providers.toolkit.javafx.nodes.FxNode;
import naga.toolkit.fx.spi.Drawing;
import naga.toolkit.fx.spi.DrawingMixin;
import naga.toolkit.fx.spi.DrawingNode;

/**
 * @author Bruno Salmon
 */
public class FxDrawingNode extends FxNode<Node> implements DrawingNode, DrawingMixin {

    private final Drawing drawing;

    public FxDrawingNode() {
        this(new BorderPane());
    }

    public FxDrawingNode(Node node) {
        super(node);
        drawing = new FxDrawing(this);
        //widthProperty.bind(node.widthProperty());
/*
        node.widthProperty().addListener((observable, oldValue, newWidth) -> widthProperty.setValue(newWidth.doubleValue()));
        node.heightProperty().addListener((observable, oldValue, newWidth) -> widthProperty.setValue(newWidth.doubleValue()));
*/
    }

    private final Property<Double> widthProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> widthProperty() {
        return widthProperty;
    }

    private final Property<Double> heightProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> heightProperty() {
        return heightProperty;
    }

    @Override
    public Drawing getDrawing() {
        return drawing;
    }
}
