package naga.fx.spi.javafx.fx;

import javafx.collections.ObservableList;
import naga.fx.spi.javafx.fx.viewer.FxNodeViewer;
import naga.fx.scene.Node;
import naga.fx.scene.Parent;
import naga.fx.scene.Scene;
import naga.fx.spi.viewer.NodeViewer;
import naga.fx.spi.viewer.NodeViewerFactory;
import naga.fx.spi.Toolkit;
import naga.fx.properties.ObservableLists;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author Bruno Salmon
 */
public class FxScene extends Scene {

    public FxScene() {
        this(FxNodeViewerFactory.SINGLETON);
    }

    public FxScene(NodeViewerFactory nodeViewerFactory) {
        super(nodeViewerFactory);
    }

    @Override
    protected void updateParentAndChildrenViewers(Parent parent) {
        Toolkit.get().scheduler().runInUiThread(() -> { // Because this method might be called from a non UI thread
            javafx.scene.Parent fxParent = (javafx.scene.Parent) getFxNode(parent);
            try {
                Method getChildren = javafx.scene.Parent.class.getDeclaredMethod("getChildren");
                getChildren.setAccessible(true);
                ObservableList<javafx.scene.Node> children = (ObservableList<javafx.scene.Node>) getChildren.invoke(fxParent);
                ObservableLists.setAllNonNullsConverted(new ArrayList<>(parent.getChildren()), this::getFxNode, children);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private javafx.scene.Node getFxNode(Node node) {
        NodeViewer nodeViewer = getOrCreateAndBindNodeViewer(node);
        if (nodeViewer instanceof FxNodeViewer) // Should be a FxNodeViewer
            return((FxNodeViewer) nodeViewer).getFxNode();
        return null; // Shouldn't happen...
    }
}
