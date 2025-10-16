package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.svg;

import javafx.geometry.Insets;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.SvgUtil;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.RegionPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.RegionPeerMixin;

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
