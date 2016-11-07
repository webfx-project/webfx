package naga.providers.toolkit.javafx.drawing;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Region;
import naga.providers.toolkit.javafx.nodes.FxNode;
import naga.toolkit.drawing.spi.Drawing;
import naga.toolkit.drawing.spi.DrawingMixin;
import naga.toolkit.drawing.spi.DrawingNode;

/**
 * @author Bruno Salmon
 */
public class FxDrawingNode extends FxNode<Region> implements DrawingNode<Region>, DrawingMixin {

    private final Drawing drawing;

    public FxDrawingNode() {
        this(new Region());
    }

    public FxDrawingNode(Region node) {
        super(node);
        drawing = new FxDrawing(this);
        //widthProperty.bind(node.widthProperty());
        node.widthProperty().addListener((observable, oldValue, newWidth) -> widthProperty.setValue(newWidth.doubleValue()));
        node.prefHeightProperty().bind(heightProperty());
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
