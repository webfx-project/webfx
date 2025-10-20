package dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.base;

import com.sun.javafx.tk.TKStageListener;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.StagePeer;
import dev.webfx.platform.uischeduler.UiScheduler;
import javafx.stage.Stage;

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
        lastWidth = lastHeight = 0; // to force the listener to be call on first changedWindowSize() invocation
        changedWindowSize();
    }

    @Override
    public void setBounds(float x, float y, boolean xSet, boolean ySet, float w, float h, float cw, float ch, float xGravity, float yGravity) {
        //Console.log("x = " + x + ", y = " + y + ", w = " + w + ", h = " + h + ", cw = " + cw + ", ch = " + ch);
        changedWindowSize();
    }

    public void changedWindowSize() {
        double width = getPeerWindowWidth();
        double height = getPeerWindowHeight();
        if (width == lastWidth && height == lastHeight)
            return;
        //Console.log("Window size changed. Width: " + lastWidth + " -> " + width + ". Height: " + lastHeight + " -> " + height);
        getWindow().notifySizeChanged(width, height);
        if (listener != null)
            listener.changedSize((float) width, (float) height);
        ScenePeerBase scenePeer = getScenePeer();
        if (scenePeer != null)
            scenePeer.changedWindowSize(width, height);
        lastWidth = width;
        lastHeight = height;
        // Workaround for a browser bug observed on iPad where the window width/height properties were still not final
        // after rotating the iPad despite the resize event being fired in JS. So we schedule a later update to get
        // the final values (this won't create an infinite loop, because these values will stabilize).
        UiScheduler.scheduleDelay(500, this::changedWindowSize); // 500 ms seem enough
    }

    protected abstract double getPeerWindowWidth();

    protected abstract double getPeerWindowHeight();
}
