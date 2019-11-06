package webfx.kit.mapper.peers.javafxgraphics.gwt.svg;

import javafx.geometry.Insets;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.SvgUtil;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import webfx.kit.mapper.peers.javafxgraphics.base.RegionPeerBase;
import webfx.kit.mapper.peers.javafxgraphics.base.RegionPeerMixin;

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
