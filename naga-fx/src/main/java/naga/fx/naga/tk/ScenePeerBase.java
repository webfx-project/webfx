package naga.fx.naga.tk;

import naga.fx.scene.Scene;
import naga.fx.spi.viewer.NodeViewerFactory;
import naga.fx.sun.tk.TKSceneListener;

/**
 * @author Bruno Salmon
 */
public abstract class ScenePeerBase implements ScenePeer {

    protected final Scene scene;
    private final NodeViewerFactory nodeViewerFactory;
    protected TKSceneListener listener;

    public ScenePeerBase(Scene scene, NodeViewerFactory nodeViewerFactory) {
        this.scene = scene;
        this.nodeViewerFactory = nodeViewerFactory;
    }

    @Override
    public void setTKSceneListener(TKSceneListener listener) {
        this.listener = listener;
    }

    public Scene getScene() {
        return scene;
    }

    @Override
    public NodeViewerFactory getNodeViewerFactory() {
        return nodeViewerFactory;
    }
}
