package dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.base;

import com.sun.javafx.tk.TKSceneListener;
import javafx.scene.Scene;
import javafx.stage.Window;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.ScenePeer;

/**
 * @author Bruno Salmon
 */
public abstract class ScenePeerBase implements ScenePeer {

    protected final Scene scene;
    public TKSceneListener listener;

    public ScenePeerBase(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void setTKSceneListener(TKSceneListener listener) {
        this.listener = listener;
        Window window = scene.getWindow();
        changedWindowSize(window.getWidth(), window.getHeight());
    }

    public void changedWindowSize(double width, double height) {
        if (listener != null && !Double.isNaN(width) && !Double.isNaN(height)) {
            listener.changedSize((float) width, (float) height);
        }
    }

    public Scene getScene() {
        return scene;
    }

}
