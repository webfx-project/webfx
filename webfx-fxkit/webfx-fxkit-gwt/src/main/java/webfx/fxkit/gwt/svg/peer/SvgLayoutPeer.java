package webfx.fxkit.gwt.svg.peer;

import emul.javafx.geometry.Insets;
import webfx.fxkit.gwt.util.SvgUtil;
import emul.javafx.scene.layout.Background;
import emul.javafx.scene.layout.Border;
import emul.javafx.scene.layout.Region;
import webfx.fxkits.core.spi.peer.base.RegionPeerBase;
import webfx.fxkits.core.spi.peer.base.RegionPeerMixin;

/**
 * @author Bruno Salmon
 */
public class SvgLayoutPeer
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
