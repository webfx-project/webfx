package webfx.fxkit.mapper.spi.impl.peer;

import com.sun.javafx.tk.TKSceneListener;
import javafx.scene.Scene;
import webfx.fxkit.mapper.spi.ScenePeer;

/**
 * @author Bruno Salmon
 */
public abstract class ScenePeerBase implements ScenePeer {

    protected final Scene scene;
    protected TKSceneListener listener;

    public ScenePeerBase(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void setTKSceneListener(TKSceneListener listener) {
        this.listener = listener;
    }

    public Scene getScene() {
        return scene;
    }

}
