package naga.fx.spi.peer.base;

import com.sun.javafx.tk.TKSceneListener;
import javafx.scene.Scene;
import naga.fx.spi.peer.NodePeerFactory;
import naga.fx.spi.peer.ScenePeer;

/**
 * @author Bruno Salmon
 */
public abstract class ScenePeerBase implements ScenePeer {

    protected final Scene scene;
    private final NodePeerFactory nodePeerFactory;
    protected TKSceneListener listener;

    public ScenePeerBase(Scene scene, NodePeerFactory nodePeerFactory) {
        this.scene = scene;
        this.nodePeerFactory = nodePeerFactory;
    }

    @Override
    public void setTKSceneListener(TKSceneListener listener) {
        this.listener = listener;
    }

    public Scene getScene() {
        return scene;
    }

    public NodePeerFactory getNodePeerFactory() {
        return nodePeerFactory;
    }
}
