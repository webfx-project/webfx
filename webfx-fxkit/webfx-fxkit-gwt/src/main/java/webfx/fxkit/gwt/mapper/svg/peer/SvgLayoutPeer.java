package webfx.fxkit.gwt.mapper.svg.peer;

import emul.javafx.geometry.Insets;
import webfx.fxkit.gwt.mapper.util.SvgUtil;
import emul.javafx.scene.layout.Background;
import emul.javafx.scene.layout.Border;
import emul.javafx.scene.layout.Region;
import webfx.fxkit.mapper.spi.impl.peer.RegionPeerBase;
import webfx.fxkit.mapper.spi.impl.peer.RegionPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class SvgLayoutPeer
        <N extends Region, NB extends RegionPeerBase<N, NB, NM>, NM extends RegionPeerMixin<N, NB, NM>>

        extends SvgRegionPeer<N, NB, NM> {

    public SvgLayoutPeer() {
        super((NB) new RegionPeerBase<N, NB, NM>(), SvgUtil.createSvgGroup());
    }

    @Override
    public void updateBackground(Background background) {
        // Not yet implemented
    }

    @Override
    public void updateBorder(Border border) {
        // Not yet implemented
    }

    @Override
    public void updatePadding(Insets padding) {
        // Not yet implemented
    }
}
