package webfx.fx.spi.peer.base;

import emul.javafx.scene.control.ScrollPane;

/**
 * @author Bruno Salmon
 */
public interface ScrollPanePeerMixin
        <N extends ScrollPane, NB extends ScrollPanePeerBase<N, NB, NM>, NM extends ScrollPanePeerMixin<N, NB, NM>>

        extends RegionPeerMixin<N, NB, NM> {

    void updateHbarPolicy(ScrollPane.ScrollBarPolicy hbarPolicy);

    void updateVbarPolicy(ScrollPane.ScrollBarPolicy vbarPolicy);

    void updateHmin(Number hmin);
    void updateHvalue(Number hValue);
    void updateHmax(Number hmax);

    void updateVmin(Number vmin);
    void updateVvalue(Number vValue);
    void updateVmax(Number vmax);

}
