package naga.fx.spi.javafx.fx;

import javafx.collections.ObservableList;
import naga.fx.naga.tk.ScenePeerBase;
import naga.fx.properties.ObservableLists;
import naga.fx.properties.Properties;
import naga.fx.scene.Node;
import naga.fx.scene.Parent;
import naga.fx.scene.Scene;
import naga.fx.spi.Toolkit;
import naga.fx.spi.javafx.fx.viewer.FxNodeViewer;
import naga.fx.spi.viewer.NodeViewer;
import naga.fx.spi.viewer.NodeViewerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author Bruno Salmon
 */
public class FxScenePeer extends ScenePeerBase {

    private final static Method getChildrenMethod;

    static {
        Method getChildren = null;
        try {
            getChildren = javafx.scene.Parent.class.getDeclaredMethod("getChildren");
            getChildren.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        getChildrenMethod = getChildren;
    }

    private javafx.scene.Scene fxScene;

    public FxScenePeer(Scene scene) {
        this(scene, FxNodeViewerFactory.SINGLETON);
    }

    public FxScenePeer(Scene scene, NodeViewerFactory nodeViewerFactory) {
        super(scene, nodeViewerFactory);
    }

    public void setFxScene(javafx.scene.Scene fxScene) {
        this.fxScene = fxScene;
        Properties.runOnPropertiesChange(p -> listener.changedLocation((float) fxScene.getX(), (float) fxScene.getY()), fxScene.xProperty(), fxScene.yProperty());
        Properties.runOnPropertiesChange(p -> listener.changedSize((float) fxScene.getWidth(), (float) fxScene.getHeight()), fxScene.widthProperty(), fxScene.heightProperty());
/*
        fxScene.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
            }
        });
*/
    }

    @Override
    public void updateParentAndChildrenViewers(Parent parent) {
        Toolkit.get().scheduler().runInUiThread(() -> { // Because this method might be called from a non UI thread
            javafx.scene.Parent fxParent = (javafx.scene.Parent) getFxNode(parent);
            if (fxParent == null)
                Toolkit.get().scheduler().scheduleDeferred(() -> updateParentAndChildrenViewers(parent));
            else
                try {
                    ObservableList<javafx.scene.Node> children = (ObservableList<javafx.scene.Node>) getChildrenMethod.invoke(fxParent);
                    ObservableLists.setAllNonNullsConverted(new ArrayList<>(parent.getChildren()), this::getFxNode, children);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        });
    }

    private javafx.scene.Node getFxNode(Node node) {
        NodeViewer nodeViewer = getScene().getOrCreateAndBindNodeViewer(node);
        if (nodeViewer instanceof FxNodeViewer) // Should be a FxNodeViewer
            return ((FxNodeViewer) nodeViewer).getFxNode();
        return null; // Shouldn't happen...
    }
}
