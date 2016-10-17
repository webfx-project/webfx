package naga.providers.toolkit.javafx.drawing;

import javafx.collections.ListChangeListener;
import javafx.scene.Group;
import naga.providers.toolkit.javafx.drawing.view.FxShapeView;
import naga.providers.toolkit.javafx.nodes.FxNode;
import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.spi.Drawing;
import naga.toolkit.drawing.spi.DrawingMixin;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.drawing.spi.impl.DrawingImpl;
import naga.toolkit.util.ObservableLists;

/**
 * @author Bruno Salmon
 */
public class FxDrawingNode extends FxNode<Group> implements DrawingNode<Group>, DrawingMixin {

    private final DrawingImpl drawing = new DrawingImpl(FxShapeViewFactory.SINGLETON);

    public FxDrawingNode() {
        this(new Group());
    }

    public FxDrawingNode(Group node) {
        super(node);
        drawing.getChildrenShapes().addListener(new ListChangeListener<Shape>() {
            @Override
            public void onChanged(Change<? extends Shape> c) {
                drawing.draw();
                ObservableLists.setAllConverted(drawing.getChildrenShapes(), shape -> ((FxShapeView) drawing.getOrCreateShapeView(shape)).getFxShapeNode(), node.getChildren());
            }
        });
    }

    @Override
    public Drawing getDrawing() {
        return drawing;
    }
}
