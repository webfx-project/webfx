package webfx.fxkit.mapper.spi.impl.peer;

import com.sun.javafx.tk.TKSceneListener;
import javafx.scene.Scene;
import javafx.stage.Window;
import webfx.fxkit.mapper.spi.ScenePeer;

/**
 * @author Bruno Salmon
 */
public abstract class ScenePeerBase implements ScenePeer {

    protected final Scene scene;
    private TKSceneListener listener;

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
        if (listener != null)
            listener.changedSize((float) width, (float) height);
    }

    public Scene getScene() {
        return scene;
    }

}
