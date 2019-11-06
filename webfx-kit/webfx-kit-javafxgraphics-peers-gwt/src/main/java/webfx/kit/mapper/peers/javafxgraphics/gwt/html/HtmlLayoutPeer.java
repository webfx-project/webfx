package webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import javafx.geometry.Insets;
import javafx.scene.layout.Region;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import webfx.kit.mapper.peers.javafxgraphics.base.RegionPeerBase;
import webfx.kit.mapper.peers.javafxgraphics.base.RegionPeerMixin;

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
