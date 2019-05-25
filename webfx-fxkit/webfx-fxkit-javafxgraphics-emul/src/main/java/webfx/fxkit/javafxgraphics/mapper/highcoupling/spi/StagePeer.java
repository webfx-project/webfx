package webfx.fxkit.javafxgraphics.mapper.highcoupling.spi;

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
