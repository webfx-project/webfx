package naga.providers.toolkit.javafx.drawing;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import naga.providers.toolkit.javafx.drawing.view.FxShapeView;
import naga.providers.toolkit.javafx.nodes.FxNode;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.shapes.DrawableParent;
import naga.toolkit.drawing.spi.Drawing;
import naga.toolkit.drawing.spi.DrawingMixin;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.drawing.spi.impl.DrawingImpl;
import naga.toolkit.util.ObservableLists;

import java.lang.reflect.Method;

/**
 * @author Bruno Salmon
 */
public class FxDrawingNode extends FxNode<Region> implements DrawingNode<Region>, DrawingMixin {

    private final Drawing drawing = new DrawingImpl(FxDrawableViewFactory.SINGLETON) {
        @Override
        protected void syncParentNodeFromDrawableParent(DrawableParent drawableParent) {
            Parent parent = drawableParent == this ? node : (Parent) getFxDrawableNode((Drawable) drawableParent);
            try {
                Method getChildren = Parent.class.getDeclaredMethod("getChildren");
                getChildren.setAccessible(true);
                ObservableList<Node> children = (ObservableList<Node>) getChildren.invoke(parent);
                ObservableLists.setAllConverted(drawableParent.getDrawableChildren(), this::getFxDrawableNode, children);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private Node getFxDrawableNode(Drawable drawable) {
            return ((FxShapeView) getOrCreateAndBindDrawableView(drawable)).getFxDrawableNode();
        }
    };


    public FxDrawingNode() {
        this(new Region());
    }

    public FxDrawingNode(Region node) {
        super(node);
    }

    @Override
    public Drawing getDrawing() {
        return drawing;
    }
}
