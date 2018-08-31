package webfx.fx.spi.gwt.svg.peer;

import elemental2.dom.Element;
import emul.javafx.scene.layout.Region;
import webfx.fx.spi.peer.base.RegionPeerBase;
import webfx.fx.spi.peer.base.RegionPeerMixin;

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
    public void updateWidth(Double width) {
        setElementAttribute("width", width);
    }

    @Override
    public void updateHeight(Double height) {
        setElementAttribute("height", height);
    }
}
