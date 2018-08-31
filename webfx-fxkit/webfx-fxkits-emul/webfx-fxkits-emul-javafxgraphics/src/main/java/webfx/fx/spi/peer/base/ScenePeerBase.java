package webfx.fx.spi.peer.base;

import emul.javafx.scene.Scene;
import webfx.fx.spi.peer.NodePeerFactory;
import webfx.fx.spi.peer.ScenePeer;
import emul.com.sun.javafx.tk.TKSceneListener;

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
