package naga.providers.toolkit.javafx.fx;

import javafx.collections.ObservableList;
import naga.providers.toolkit.javafx.fx.viewer.FxNodeViewer;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.Parent;
import naga.toolkit.fx.scene.Scene;
import naga.toolkit.fx.spi.viewer.NodeViewer;
import naga.toolkit.fx.spi.viewer.NodeViewerFactory;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.util.ObservableLists;

import java.lang.reflect.Method;

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
                ObservableLists.setAllNonNullsConverted(parent.getChildren(), this::getFxNode, children);
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
