package naga.providers.toolkit.javafx.fx;

import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import naga.providers.toolkit.javafx.fx.viewer.FxNodeViewer;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.Parent;
import naga.toolkit.fx.spi.DrawingNode;
import naga.toolkit.fx.spi.impl.DrawingImpl;
import naga.toolkit.fx.spi.viewer.NodeViewer;
import naga.toolkit.fx.spi.viewer.NodeViewerFactory;
import naga.toolkit.util.ObservableLists;

import java.lang.reflect.Method;

/**
 * @author Bruno Salmon
 */
class FxDrawing extends DrawingImpl {

    FxDrawing(FxDrawingNode fxDrawingNode) {
        this(fxDrawingNode, FxNodeViewerFactory.SINGLETON);
    }

    public FxDrawing(DrawingNode drawingNode, NodeViewerFactory nodeViewerFactory) {
        super(drawingNode, nodeViewerFactory);
    }

    @Override
    protected void createAndBindRootNodeViewerAndChildren(Node rootNode) {
        super.createAndBindRootNodeViewerAndChildren(rootNode);
        BorderPane fxParent = (BorderPane) ((FxDrawingNode) drawingNode).unwrapToNativeNode();
        fxParent.setCenter(getFxNode(rootNode));
/*
        try {
            Method getChildren = javafx.scene.Parent.class.getDeclaredMethod("getChildren");
            getChildren.setAccessible(true);
            ObservableList<javafx.scene.Node> children = (ObservableList<javafx.scene.Node>) getChildren.invoke(fxParent);
            ObservableLists.setAllNonNulls(children, getFxNode(rootNode));
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
    }

    @Override
    protected void updateParentAndChildrenViewers(Parent parent) {
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
        NodeViewer nodeViewer = getOrCreateAndBindNodeViewer(node);
        if (nodeViewer instanceof FxNodeViewer) // Should be a FxNodeViewer
            return((FxNodeViewer) nodeViewer).getFxNode();
        return null; // Shouldn't happen...
    }
}
