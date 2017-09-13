package naga.fx.spi.gwt.html.peer;

import emul.javafx.geometry.Insets;
import naga.fx.spi.gwt.util.HtmlUtil;
import emul.javafx.scene.layout.Region;
import naga.fx.spi.peer.base.RegionPeerBase;
import naga.fx.spi.peer.base.RegionPeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlLayoutPeer
        <N extends Region, NB extends RegionPeerBase<N, NB, NM>, NM extends RegionPeerMixin<N, NB, NM>>

        extends HtmlRegionPeer<N, NB, NM> {

    public HtmlLayoutPeer() {
        super((NB) new RegionPeerBase<N, NB, NM>(), HtmlUtil.createSpanElement());
    }

    @Override
    public void updatePadding(Insets padding) {
        // It's important to not apply the padding into css here because it is already considered by the layout
        // when computing the children positions
    }
}
