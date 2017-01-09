package naga.fx.spi.javafx.peer;

import javafx.collections.ObservableList;
import naga.fx.properties.ObservableLists;
import naga.fx.properties.Properties;
import naga.fx.scene.Node;
import naga.fx.scene.Parent;
import naga.fx.scene.Scene;
import naga.fx.spi.Toolkit;
import naga.fx.spi.javafx.FxNodePeerFactory;
import naga.fx.spi.peer.NodePeer;
import naga.fx.spi.peer.NodePeerFactory;
import naga.fx.spi.peer.base.ScenePeerBase;

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
        this(scene, FxNodePeerFactory.SINGLETON);
    }

    public FxScenePeer(Scene scene, NodePeerFactory nodePeerFactory) {
        super(scene, nodePeerFactory);
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
    public void updateParentAndChildrenPeers(Parent parent) {
        Toolkit.get().scheduler().runInUiThread(() -> { // Because this method might be called from a non UI thread
            javafx.scene.Parent fxParent = (javafx.scene.Parent) getFxNode(parent);
            if (fxParent == null)
                Toolkit.get().scheduler().scheduleDeferred(() -> updateParentAndChildrenPeers(parent));
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
        NodePeer nodePeer = getScene().getOrCreateAndBindNodePeer(node);
        if (nodePeer instanceof FxNodePeer) // Should be a FxNodePeer
            return ((FxNodePeer) nodePeer).getFxNode();
        return null; // Shouldn't happen...
    }
}
