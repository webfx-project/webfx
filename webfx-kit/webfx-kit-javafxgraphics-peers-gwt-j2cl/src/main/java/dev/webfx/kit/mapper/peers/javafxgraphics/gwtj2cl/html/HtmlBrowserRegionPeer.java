package dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html;

import dev.webfx.kit.mapper.peers.javafxgraphics.HasNoChildrenPeers;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.RegionPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.RegionPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.layoutmeasurable.HtmlLayoutMeasurable;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.HtmlUtil;
import javafx.scene.layout.Region;

/**
 * @author Bruno Salmon
 */
public final class HtmlBrowserRegionPeer
        <N extends Region, NB extends RegionPeerBase<N, NB, NM>, NM extends RegionPeerMixin<N, NB, NM>>

        extends HtmlRegionPeer<N, NB, NM> implements HtmlLayoutMeasurable, HasNoChildrenPeers {

    public HtmlBrowserRegionPeer(String tagName) {
        super((NB) new RegionPeerBase(), HtmlUtil.createElement(tagName));
    }

}
