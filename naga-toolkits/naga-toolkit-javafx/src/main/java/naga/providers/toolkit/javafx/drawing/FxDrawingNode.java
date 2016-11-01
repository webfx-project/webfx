package naga.providers.toolkit.javafx.drawing;

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
    }

    @Override
    public Drawing getDrawing() {
        return drawing;
    }
}
