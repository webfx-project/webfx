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
        lastWidth = lastHeight = 0; // to force listener call in changedWindowSize()
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
        // Workaround for a browser bug observed on Chrome on iPad where the window width/height properties were still
        // not final after rotating the iPad despite the resize event being fired in JS. So we schedule a subsequent
        // update to get the final values (this won't create an infinite loop, because these values will stabilize).
        UiScheduler.scheduleInAnimationFrame(this::changedWindowSize, 5); // 5 animation frames seem enough
    }

    protected abstract double getPeerWindowWidth();

    protected abstract double getPeerWindowHeight();
}
