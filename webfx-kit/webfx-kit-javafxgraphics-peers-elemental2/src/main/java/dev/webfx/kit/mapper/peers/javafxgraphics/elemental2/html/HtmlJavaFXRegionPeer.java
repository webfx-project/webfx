package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html;

import javafx.geometry.Insets;
import javafx.scene.layout.Region;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.RegionPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.RegionPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlJavaFXRegionPeer
        <N extends Region, NB extends RegionPeerBase<N, NB, NM>, NM extends RegionPeerMixin<N, NB, NM>>

        extends HtmlRegionPeer<N, NB, NM>
        implements NoWrapWhiteSpacePeer {

    public HtmlJavaFXRegionPeer(String tag) {
        super((NB) new RegionPeerBase<N, NB, NM>(), HtmlUtil.createElement(tag));
    }

    @Override
    public void updatePadding(Insets padding) {
        // We do not apply the padding into css here because it is already considered by the JavaFX layout system
    }
}
