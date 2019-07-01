package webfx.fxkit.javafxgraphics.mapper.highcoupling.spi.impl;

import com.sun.javafx.tk.TKStageListener;
import javafx.stage.Stage;
import webfx.fxkit.javafxgraphics.mapper.highcoupling.spi.StagePeer;
import webfx.platform.shared.services.log.Logger;

/**
 * @author Bruno Salmon
 */
public abstract class StagePeerBase extends WindowPeerBase implements StagePeer {

    private double lastWidth;
    private double lastHeight;

    protected StagePeerBase(Stage stage) {
        super(stage);
    }

    @Override
    public void setTKStageListener(TKStageListener listener) {
        super.setTKStageListener(listener);
        listener.changedLocation(0, 0);
        lastWidth = lastHeight = 0; // to force listener call in changedWindowSize()
        //UiScheduler.requestNextScenePulse(); // to ensure changedWindowSize() will be called very soon
        changedWindowSize();
    }

    @Override
    public void setBounds(float x, float y, boolean xSet, boolean ySet, float w, float h, float cw, float ch, float xGravity, float yGravity) {
        Logger.log("x = " + x + ", y = " + y + ", w = " + w + ", h = " + h + ", cw = " + cw + ", ch = " + ch);
        changedWindowSize();
    }

    public void changedWindowSize() {
        double width = getPeerWindowWidth();
        double height = getPeerWindowHeight();
        if (width == lastWidth && height == lastHeight)
            return;
        getWindow().notifySizeChanged(width, height);
        if (listener != null)
            listener.changedSize((float) width, (float) height);
        ScenePeerBase scenePeer = getScenePeer();
        if (scenePeer != null)
            scenePeer.changedWindowSize(width, height);
        lastWidth = width;
        lastHeight = height;
    }

    protected abstract double getPeerWindowWidth();

    protected abstract double getPeerWindowHeight();
}
