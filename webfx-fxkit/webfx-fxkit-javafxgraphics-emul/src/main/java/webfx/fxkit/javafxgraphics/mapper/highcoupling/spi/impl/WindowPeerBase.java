package webfx.fxkit.javafxgraphics.mapper.highcoupling.spi.impl;

import com.sun.javafx.tk.TKStageListener;
import javafx.stage.Window;
import webfx.fxkit.javafxgraphics.mapper.highcoupling.spi.WindowPeer;

public abstract class WindowPeerBase implements WindowPeer {

    private final Window window;
    TKStageListener listener;

    protected WindowPeerBase(Window window) {
        this.window = window;
    }

    @Override
    public Window getWindow() {
        return window;
    }

    @Override
    public void setTKStageListener(TKStageListener listener) {
        this.listener = listener;
    }

    protected abstract ScenePeerBase getScenePeer();

}
