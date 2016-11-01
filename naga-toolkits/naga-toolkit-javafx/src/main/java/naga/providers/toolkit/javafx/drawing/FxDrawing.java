package naga.providers.toolkit.javafx.drawing;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import naga.providers.toolkit.javafx.drawing.view.FxDrawableView;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.shapes.DrawableParent;
import naga.toolkit.drawing.spi.impl.DrawingImpl;
import naga.toolkit.util.ObservableLists;

import java.lang.reflect.Method;

/**
 * @author Bruno Salmon
 */
class FxDrawing extends DrawingImpl {

    FxDrawing(FxDrawingNode fxDrawingNode) {
        super(fxDrawingNode, FxDrawableViewFactory.SINGLETON);
    }

    @Override
    protected void updateDrawableParentAndChildrenViews(DrawableParent drawableParent) {
        Parent parent = (Parent) (isDrawableParentRoot(drawableParent) ? drawingNode.unwrapToNativeNode() : getFxDrawableNode((Drawable) drawableParent));
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
        return ((FxDrawableView) getOrCreateAndBindDrawableView(drawable)).getFxDrawableNode();
    }
}
