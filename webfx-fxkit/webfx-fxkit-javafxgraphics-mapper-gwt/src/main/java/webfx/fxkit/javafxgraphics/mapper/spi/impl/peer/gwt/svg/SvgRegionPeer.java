package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.svg;

import elemental2.dom.Element;
import javafx.scene.layout.Region;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.RegionPeerBase;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.RegionPeerMixin;

/**
 * @author Bruno Salmon
 */
abstract class SvgRegionPeer
        <N extends Region, NB extends RegionPeerBase<N, NB, NM>, NM extends RegionPeerMixin<N, NB, NM>>

        extends SvgNodePeer<N, NB, NM>
        implements RegionPeerMixin<N, NB, NM> {

    SvgRegionPeer(NB base, Element element) {
        super(base, element);
    }

    @Override
    public void updateWidth(Number width) {
        setElementAttribute("width", width);
    }

    @Override
    public void updateHeight(Number height) {
        setElementAttribute("height", height);
    }
}
