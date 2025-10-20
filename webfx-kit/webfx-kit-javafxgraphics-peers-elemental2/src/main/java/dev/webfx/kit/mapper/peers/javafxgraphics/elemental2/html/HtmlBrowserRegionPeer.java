package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html;

import dev.webfx.kit.mapper.peers.javafxgraphics.HasNoChildrenPeers;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.RegionPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.RegionPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.layoutmeasurable.HtmlMeasurable;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;
import javafx.scene.layout.Region;

/**
 * @author Bruno Salmon
 */
public final class HtmlBrowserRegionPeer
        <N extends Region, NB extends RegionPeerBase<N, NB, NM>, NM extends RegionPeerMixin<N, NB, NM>>

        extends HtmlRegionPeer<N, NB, NM> implements HtmlMeasurable, HasNoChildrenPeers {

    public HtmlBrowserRegionPeer(String tagName) {
        super((NB) new RegionPeerBase(), HtmlUtil.createElement(tagName));
    }

}
