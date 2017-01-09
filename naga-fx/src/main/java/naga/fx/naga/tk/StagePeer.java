package naga.fx.naga.tk;

import naga.fx.stage.Stage;

/**
 * @author Bruno Salmon
 */
public interface StagePeer extends WindowPeer {

    default Stage getStage() {
        return (Stage) getWindow();
    }

}
