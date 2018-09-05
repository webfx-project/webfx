package webfx.fxkits.core.spi.peer.base;

import javafx.scene.Scene;
import webfx.fxkits.core.spi.peer.NodePeerFactory;
import webfx.fxkits.core.spi.peer.ScenePeer;
import com.sun.javafx.tk.TKSceneListener;

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
