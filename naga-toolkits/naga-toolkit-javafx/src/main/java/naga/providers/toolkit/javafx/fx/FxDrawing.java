package naga.providers.toolkit.javafx.fx;

import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import naga.providers.toolkit.javafx.fx.view.FxNodeView;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.Parent;
import naga.toolkit.fx.spi.impl.DrawingImpl;
import naga.toolkit.fx.spi.view.NodeView;
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
            ObservableLists.setAllNonNulls(children, getFxNode(rootNode));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateParentAndChildrenViews(Parent parent) {
        javafx.scene.Parent fxParent = (javafx.scene.Parent) getFxNode(parent);
        try {
            Method getChildren = javafx.scene.Parent.class.getDeclaredMethod("getChildren");
            getChildren.setAccessible(true);
            ObservableList<javafx.scene.Node> children = (ObservableList<javafx.scene.Node>) getChildren.invoke(fxParent);
            ObservableLists.setAllNonNullsConverted(parent.getChildren(), this::getFxNode, children);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private javafx.scene.Node getFxNode(Node node) {
        NodeView nodeView = getOrCreateAndBindNodeView(node);
        if (nodeView instanceof FxNodeView) // Should be a FxNodeView
            return((FxNodeView) nodeView).getFxNode();
        return null; // Shouldn't happen...
    }
}
