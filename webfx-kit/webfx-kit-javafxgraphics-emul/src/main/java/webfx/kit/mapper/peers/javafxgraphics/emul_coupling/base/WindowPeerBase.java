package webfx.kit.mapper.peers.javafxgraphics.emul_coupling.base;

import com.sun.javafx.tk.TKStageListener;
import javafx.stage.Window;
import webfx.kit.mapper.peers.javafxgraphics.emul_coupling.WindowPeer;

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
