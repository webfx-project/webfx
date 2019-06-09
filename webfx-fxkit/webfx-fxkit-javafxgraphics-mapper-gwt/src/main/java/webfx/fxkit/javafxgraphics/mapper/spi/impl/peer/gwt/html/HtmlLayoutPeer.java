package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.html;

import javafx.geometry.Insets;
import javafx.scene.layout.Region;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.util.HtmlUtil;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.RegionPeerBase;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.RegionPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlLayoutPeer
        <N extends Region, NB extends RegionPeerBase<N, NB, NM>, NM extends RegionPeerMixin<N, NB, NM>>

        extends HtmlRegionPeer<N, NB, NM>
        implements NoWrapWhiteSpacePeer {

    public HtmlLayoutPeer(String tag) {
        super((NB) new RegionPeerBase<N, NB, NM>(), HtmlUtil.createElement(tag));
        subtractCssPaddingBorderWhenUpdatingSize = true;
    }

    @Override
    public void updatePadding(Insets padding) {
        // We do not apply the padding into css here because it is already considered by the JavaFx layout system
    }
}
