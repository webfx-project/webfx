package webfx.fxkit.gwt.mapper.svg.peer.javafxgraphics;

import javafx.geometry.Insets;
import webfx.fxkit.gwt.mapper.util.SvgUtil;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import webfx.fxkit.mapper.spi.impl.peer.javafxgraphics.RegionPeerBase;
import webfx.fxkit.mapper.spi.impl.peer.javafxgraphics.RegionPeerMixin;

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
