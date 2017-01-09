package naga.fx.spi.peer.base;

import naga.fx.scene.layout.Background;
import naga.fx.scene.layout.Border;
import naga.fx.scene.layout.Region;

/**
 * @author Bruno Salmon
 */
public interface RegionPeerMixin
        <N extends Region, NB extends RegionPeerBase<N, NB, NM>, NM extends RegionPeerMixin<N, NB, NM>>

        extends NodePeerMixin<N, NB, NM> {

    void updateWidth(Double width);

    void updateHeight(Double height);

    void updateBackground(Background background);

    void updateBorder(Border border);
}
