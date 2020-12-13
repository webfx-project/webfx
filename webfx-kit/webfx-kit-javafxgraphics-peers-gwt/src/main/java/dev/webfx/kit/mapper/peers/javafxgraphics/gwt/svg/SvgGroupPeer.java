package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.svg;

import elemental2.dom.Element;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.SvgUtil;
import javafx.scene.Group;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.GroupPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.GroupPeerBase;

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
