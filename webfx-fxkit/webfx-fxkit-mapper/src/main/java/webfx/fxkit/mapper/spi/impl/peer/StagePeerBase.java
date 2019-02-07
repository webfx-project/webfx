package webfx.fxkit.mapper.spi.impl.peer;

import com.sun.javafx.tk.TKStageListener;
import javafx.stage.Stage;
import javafx.stage.Window;
import webfx.fxkit.mapper.spi.StagePeer;

/**
 * @author Bruno Salmon
 */
public abstract class StagePeerBase implements StagePeer {
    protected final Stage stage;
    private TKStageListener listener;
    private double lastWidth;
    private double lastHeight;

    protected StagePeerBase(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setTKStageListener(TKStageListener listener) {
        this.listener = listener;
        listener.changedLocation(0, 0);
        lastWidth = lastHeight = 0; // to force listener call in changedWindowSize()
        //UiScheduler.requestNextScenePulse(); // to ensure changedWindowSize() will be called very soon
        changedWindowSize();
    }

    @Override
    public void setBounds(float x, float y, boolean xSet, boolean ySet, float w, float h, float cw, float ch, float xGravity, float yGravity) {
        //Logger.log("x = " + x + ", y = " + y + ", w = " + w + ", h = " + h + ", cw = " + cw + ", ch = " + ch);
        changedWindowSize();
    }

    public void changedWindowSize() {
        double width = getPeerWindowWidth();
        double height = getPeerWindowHeight();
        if (width == lastWidth && height == lastHeight)
            return;
        stage.setWidth(width);
        stage.setHeight(height);
        if (listener != null)
            listener.changedSize((float) width, (float) height);
        ScenePeerBase scenePeer = getScenePeer();
        if (scenePeer != null)
            scenePeer.changedWindowSize(width, height);
        lastWidth = width;
        lastHeight = height;
    }

    protected abstract ScenePeerBase getScenePeer();

    @Override
    public Window getWindow() {
        return stage;
    }

    protected abstract double getPeerWindowWidth();

    protected abstract double getPeerWindowHeight();
}
