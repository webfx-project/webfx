package naga.providers.toolkit.javafx.drawing;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import naga.providers.toolkit.javafx.drawing.view.FxShapeView;
import naga.providers.toolkit.javafx.nodes.FxNode;
import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.shapes.ShapeParent;
import naga.toolkit.drawing.spi.Drawing;
import naga.toolkit.drawing.spi.DrawingMixin;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.drawing.spi.impl.DrawingImpl;
import naga.toolkit.util.ObservableLists;

import java.lang.reflect.Method;

/**
 * @author Bruno Salmon
 */
public class FxDrawingNode extends FxNode<Group> implements DrawingNode<Group>, DrawingMixin {

    private final Drawing drawing = new DrawingImpl(FxShapeViewFactory.SINGLETON) {
        @Override
        protected void syncChildrenShapesWithVisual(ShapeParent shapeParent) {
            Parent parent = shapeParent == this ? node : (Parent) getFxShapeNode((Shape) shapeParent);
            try {
                Method getChildren = Parent.class.getDeclaredMethod("getChildren");
                getChildren.setAccessible(true);
                ObservableList<Node> children = (ObservableList<Node>) getChildren.invoke(parent);
                ObservableLists.setAllConverted(shapeParent.getChildrenShapes(), this::getFxShapeNode, children);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private Node getFxShapeNode(Shape shape) {
            return ((FxShapeView) getOrCreateAndBindShapeView(shape)).getFxShapeNode();
        }
    };


    public FxDrawingNode() {
        this(new Group());
    }

    public FxDrawingNode(Group node) {
        super(node);
    }

    @Override
    public Drawing getDrawing() {
        return drawing;
    }
}
