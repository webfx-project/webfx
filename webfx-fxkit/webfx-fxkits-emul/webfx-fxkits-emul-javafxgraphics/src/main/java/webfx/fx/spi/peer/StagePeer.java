package webfx.fx.spi.peer;

import emul.javafx.stage.Stage;

/**
 * @author Bruno Salmon
 */
public interface StagePeer extends WindowPeer {

    default Stage getStage() {
        return (Stage) getWindow();
    }

}
