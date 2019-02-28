package webfx.fxkit.mapper.spi.impl.peer.javafxcontrols;

import javafx.scene.control.ScrollPane;
import webfx.fxkit.mapper.spi.impl.peer.javafxgraphics.RegionPeerMixin;

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
