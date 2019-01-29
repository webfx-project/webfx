package webfx.fxkit.gwt.mapper.html.peer;

import javafx.geometry.Insets;
import javafx.scene.layout.Region;
import webfx.fxkit.gwt.mapper.util.HtmlUtil;
import webfx.fxkit.mapper.spi.impl.peer.RegionPeerBase;
import webfx.fxkit.mapper.spi.impl.peer.RegionPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlLayoutPeer
        <N extends Region, NB extends RegionPeerBase<N, NB, NM>, NM extends RegionPeerMixin<N, NB, NM>>

        extends HtmlRegionPeer<N, NB, NM> {

    public HtmlLayoutPeer() {
        super((NB) new RegionPeerBase<N, NB, NM>(), HtmlUtil.createSpanElement());
        subtractCssPaddingBorderWhenUpdatingSize = true;
    }

    @Override
    public void updatePadding(Insets padding) {
        // We do not apply the padding into css here because it is already considered by the JavaFx layout system
    }
}
