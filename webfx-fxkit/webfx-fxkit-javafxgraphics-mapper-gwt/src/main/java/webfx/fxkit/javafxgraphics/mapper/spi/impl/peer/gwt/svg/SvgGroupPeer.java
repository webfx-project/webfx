package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.svg;

import elemental2.dom.Element;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.util.SvgUtil;
import javafx.scene.Group;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.GroupPeerMixin;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.GroupPeerBase;

/**
 * @author Bruno Salmon
 */
public final class SvgGroupPeer
        <N extends Group, NB extends GroupPeerBase<N, NB, NM>, NM extends GroupPeerMixin<N, NB, NM>>

        extends SvgNodePeer<N, NB, NM>
        implements GroupPeerMixin<N, NB, NM> {

    public SvgGroupPeer() {
        this((NB) new GroupPeerBase(), SvgUtil.createSvgGroup());
    }

    public SvgGroupPeer(NB base, Element element) {
        super(base, element);
    }
}
