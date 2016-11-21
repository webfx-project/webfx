package naga.providers.toolkit.javafx.drawing;

import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import naga.providers.toolkit.javafx.drawing.view.FxNodeView;
import naga.toolkit.drawing.shapes.Node;
import naga.toolkit.drawing.shapes.Parent;
import naga.toolkit.drawing.spi.impl.DrawingImpl;
import naga.toolkit.drawing.spi.view.NodeView;
import naga.toolkit.util.ObservableLists;

import java.lang.reflect.Method;

/**
 * @author Bruno Salmon
 */
class FxDrawing extends DrawingImpl {

    FxDrawing(FxDrawingNode fxDrawingNode) {
        super(fxDrawingNode, FxNodeViewFactory.SINGLETON);
    }

    @Override
    protected void createAndBindRootNodeViewAndChildren(Node rootNode) {
        super.createAndBindRootNodeViewAndChildren(rootNode);
        Region fxParent = ((FxDrawingNode) drawingNode).unwrapToNativeNode();
        try {
            Method getChildren = javafx.scene.Parent.class.getDeclaredMethod("getChildren");
            getChildren.setAccessible(true);
            ObservableList<javafx.scene.Node> children = (ObservableList<javafx.scene.Node>) getChildren.invoke(fxParent);
            ObservableLists.setAllNonNulls(children, getFxDrawableNode(rootNode));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateParentAndChildrenViews(Parent parent) {
        javafx.scene.Parent fxParent = (javafx.scene.Parent) getFxDrawableNode(parent);
        try {
            Method getChildren = javafx.scene.Parent.class.getDeclaredMethod("getChildren");
            getChildren.setAccessible(true);
            ObservableList<javafx.scene.Node> children = (ObservableList<javafx.scene.Node>) getChildren.invoke(fxParent);
            ObservableLists.setAllNonNullsConverted(parent.getNodeChildren(), this::getFxDrawableNode, children);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private javafx.scene.Node getFxDrawableNode(Node node) {
        NodeView nodeView = getOrCreateAndBindNodeView(node);
        if (nodeView instanceof FxNodeView) // Should be a FxNodeView
            return((FxNodeView) nodeView).getFxNode();
        // Shouldn't happen unless no view factory is registered for this node (probably UnimplementedNodeView was returned)
        return null; // returning null in this case to indicate there is no node to show
    }
}
