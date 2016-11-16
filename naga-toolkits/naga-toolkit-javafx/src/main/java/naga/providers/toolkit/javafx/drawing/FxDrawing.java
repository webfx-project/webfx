package naga.providers.toolkit.javafx.drawing;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import naga.providers.toolkit.javafx.drawing.view.FxDrawableView;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.shapes.DrawableParent;
import naga.toolkit.drawing.spi.impl.DrawingImpl;
import naga.toolkit.drawing.spi.view.DrawableView;
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
    protected void createAndBindRootDrawableViewAndChildren(Drawable rootDrawable) {
        super.createAndBindRootDrawableViewAndChildren(rootDrawable);
        Region parent = ((FxDrawingNode) drawingNode).unwrapToNativeNode();
        try {
            Method getChildren = Parent.class.getDeclaredMethod("getChildren");
            getChildren.setAccessible(true);
            ObservableList<Node> children = (ObservableList<Node>) getChildren.invoke(parent);
            ObservableLists.setAllNonNulls(children, getFxDrawableNode(rootDrawable));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateDrawableParentAndChildrenViews(DrawableParent drawableParent) {
        Parent parent = (Parent) getFxDrawableNode(drawableParent);
        try {
            Method getChildren = Parent.class.getDeclaredMethod("getChildren");
            getChildren.setAccessible(true);
            ObservableList<Node> children = (ObservableList<Node>) getChildren.invoke(parent);
            ObservableLists.setAllNonNullsConverted(drawableParent.getDrawableChildren(), this::getFxDrawableNode, children);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Node getFxDrawableNode(Drawable drawable) {
        DrawableView drawableView = getOrCreateAndBindDrawableView(drawable);
        if (drawableView instanceof FxDrawableView) // Should be a FxDrawableView
            return((FxDrawableView) drawableView).getFxDrawableNode();
        // Shouldn't happen unless no view factory is registered for this drawable (probably UnimplementedDrawableView was returned)
        return null; // returning null in this case to indicate there is no node to show
    }
}
