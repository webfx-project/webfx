package dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling;

import javafx.stage.Stage;

/**
 * @author Bruno Salmon
 */
public interface StagePeer extends WindowPeer {

    default Stage getStage() {
        return (Stage) getWindow();
    }

    void changedWindowSize();
}
